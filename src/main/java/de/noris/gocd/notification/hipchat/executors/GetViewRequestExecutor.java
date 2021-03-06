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
import de.noris.gocd.notification.hipchat.HipchatNotificationPlugin;
import de.noris.gocd.notification.hipchat.RequestExecutor;
import de.noris.gocd.notification.hipchat.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class GetViewRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLoggerFor(HipchatNotificationPlugin.class);

    @Override
    public GoPluginApiResponse execute() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("template", Util.readResource("/plugin-settings.template.html"));
        DefaultGoPluginApiResponse defaultGoPluginApiResponse = new DefaultGoPluginApiResponse(200);
        String json = GSON.toJson(jsonObject);
        defaultGoPluginApiResponse.setResponseBody(json);
        LOG.info("ViewRequest: Returning \n"+json);
        return defaultGoPluginApiResponse;
    }

}
