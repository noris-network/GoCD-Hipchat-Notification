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
import java.util.HashMap;

public class StageStatusRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

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
            ExecutionResult result = sendNotification();
            if (result.isSuccessful()) {
                responseJson.put("status", "success");
            } else {
                responseJson.put("status", "failure");
                responseJson.put("messages", result.getMessages());
            }
        } catch (Exception e) {
            responseJson.put("status", "failure");
            responseJson.put("messages", Arrays.asList(e.getMessage()));
        }
        return new DefaultGoPluginApiResponse(200, GSON.toJson(responseJson));
    }

    protected ExecutionResult sendNotification() throws Exception {
        String pipelineName    = request.pipeline.name;
        String pipelineCounter = request.pipeline.counter;
        String pipelineStage   = request.pipeline.stage.name;

        PluginSettings settings = pluginRequest.getPluginSettings();

        String roomName = settings.getRoom();
        String token    = settings.getToken();
        String message  = settings.getMessage();
        String serverUrl = settings.getServerUrl();

        HipchatNotificationPlugin.LOG.debug("Sending notification to "+roomName+" "+message);

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
        httpPost.setEntity(new StringEntity(GSON.toJson(new HipchatRequest("green", message))));

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
}
