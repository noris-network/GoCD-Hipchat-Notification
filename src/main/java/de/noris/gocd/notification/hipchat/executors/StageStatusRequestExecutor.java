/*
 * Copyright 2016 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.noris.gocd.notification.hipchat.executors;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import de.noris.gocd.notification.hipchat.*;
import de.noris.gocd.notification.hipchat.requests.StageStatusRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StageStatusRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    private static final Logger LOG = Logger.getLoggerFor(HipchatNotificationPlugin.class);

    private final StageStatusRequest request;
    private final PluginRequest pluginRequest;

    public StageStatusRequestExecutor(StageStatusRequest request, PluginRequest pluginRequest) {
        this.request = request;
        this.pluginRequest = pluginRequest;
    }


    @Override
    public GoPluginApiResponse execute() throws Exception {
        HashMap<String, Object> responseJson = new HashMap<>();
        try {
            ExecutionResult result = maybeSendNotification();
            if (result.isSuccessful()) {
                responseJson.put("status", "success");
            } else {
                responseJson.put("status", "failure");
                List<String> messages = result.getMessages();
                if (messages != null && !messages.isEmpty()) {
                    responseJson.put("messages", messages);
                }
            }
        } catch (Exception e) {
            responseJson.put("status", "failure");
            List<String> messages = new ArrayList<String>();
            messages.add(e.getMessage());
            responseJson.put("messages", messages);
        }
        String json = GSON.toJson(responseJson);
        LOG.info("StageStatus: Returning \n"+json);
        return new DefaultGoPluginApiResponse(200, json);
    }

    /**
     * This method decides whether to send a notification (determined via plugin configuration).
     * If a notification is to be sent, it assembles the message
     * (resolves environment variables) and passes it to doSendNotification
     * for actual sending.
     *
     */
    protected ExecutionResult maybeSendNotification() throws Exception {
        String pipelineName    = request.pipeline.name;
        String pipelineCounter = request.pipeline.counter;
        String pipelineStage   = request.pipeline.stage.name;

        PluginSettings settings = pluginRequest.getPluginSettings();

        String roomName  = settings.getRoom();
        String token     = settings.getToken();
        String serverUrl = settings.getServerUrl();

        boolean doNotify = false;
        String message   = "";
        String color     = "";
        HipchatNotificationPlugin.LOG.debug("state is "+request.pipeline.stage.state);
        if (request.pipeline.stage.state.equals("Building")) {
            doNotify = settings.isNotifyStart();
            message  = fillTemplate(settings.getMessageStart());
            color    = settings.getColorStart();
        } else if (request.pipeline.stage.state.equals("Passed")) {
            doNotify = settings.isNotifySuccess();
            message  = fillTemplate(settings.getMessageSuccess());
            color    = settings.getColorSuccess();
        } else if (request.pipeline.stage.state.equals("Failed")) {
            doNotify = settings.isNotifyFailure();
            message  = fillTemplate(settings.getMessageFailure());
            color    = settings.getColorFailure();
        } else {
            HipchatNotificationPlugin.LOG.warn("Unknown stage state: "+request.pipeline.stage.state);
        }

        if (!doNotify) {
            HipchatNotificationPlugin.LOG.debug("Hipchat notification for this event is turned off in configuration");
            return ExecutionResult.success("Hipchat notification for this event is turned off in configuration");
        }

        HipchatNotificationPlugin.LOG.debug("Sending notification to "+roomName+" "+message);

        return doSendNotification(roomName, message, color, serverUrl, token);

    }

    /**
     * This method does the actual sending of a notification to HipChat.
     *
     * @throws Exception
     */
    protected ExecutionResult doSendNotification(String roomName, String message, String color, String serverUrl, String token) throws Exception {
        String url = serverUrl + "/v2/room/" + roomName + "/notification";

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, new SecureRandom());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

        URI uri = new URIBuilder()
                .setScheme("https")
                .setHost(serverUrl)
                .setPath("/v2/room/"+roomName+"/notification")
                .build();

        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("Authorization", "Bearer "+token);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Charset", "UTF-8");
        httpPost.setEntity(new StringEntity(GSON.toJson(new HipchatRequest(color, message))));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        ResponseHandler<ExecutionResult> handler = new ResponseHandler<ExecutionResult>() {
            @Override
            public ExecutionResult handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                if (response.getStatusLine().getStatusCode() == 204) {
                    return ExecutionResult.success("Hipchat notified");
                } else {
                    HipchatNotificationPlugin.LOG.warn("Hipchat notification failed ("+response.getStatusLine().getStatusCode()+", "+response.getStatusLine().getReasonPhrase()+")");
                    return ExecutionResult.failure("Hipchat notification failed ("+response.getStatusLine().getStatusCode()+")");
                }
            }
        };

        return httpClient.execute(httpPost, handler);
    }

    private String replaceVariables(String msg, Map<String, String> vars) {
        String result = msg;
        for (Map.Entry<String, String> var: vars.entrySet()) {
            HipchatNotificationPlugin.LOG.info("Var: "+var.getKey()+"="+var.getValue());
            result = result.replace("${"+var.getKey()+"}", var.getValue());
        }
        return result;
    }

    private String fillTemplate(String msg) {
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("pipeline.name",    request.pipeline.name);
        vars.put("pipeline.counter", request.pipeline.counter);
        vars.put("pipeline.group",   request.pipeline.group);
        vars.put("stage.name",       request.pipeline.stage.name);
        vars.put("stage.counter",    request.pipeline.stage.counter);
        vars.put("stage.state",      request.pipeline.stage.state);
        vars.put("stage.result",     request.pipeline.stage.result);
        return replaceVariables(msg, vars);
    }

}
