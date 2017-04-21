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

import de.noris.gocd.notification.hipchat.RequestExecutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * TODO: add any additional configuration fields here.
 */
public class GetPluginConfigurationExecutor implements RequestExecutor {

    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static final Field NOTIFICATION_TYPE = new NonBlankField("notification_type", "Notification type", null, true, false, "0");
    public static final Field HIPCHAT_MESSAGE = new Field("hipchat_message", "Message", null, true, false, "1");
    public static final Field HIPCHAT_SERVER_URL = new NonBlankField("hipchat_server_url", "Server URL", "https://api.hipchat.com", true, true, "2");
    public static final Field HIPCHAT_ROOM = new NonBlankField("hipchat_room", "Room", null, true, false, "3");
    public static final Field HIPCHAT_TOKEN = new NonBlankField("hipchat_token", "API Token", null, true, true, "4");


    public static final Map<String, Field> FIELDS = new LinkedHashMap<>();

    static {
        FIELDS.put(NOTIFICATION_TYPE.key(), NOTIFICATION_TYPE);

        FIELDS.put(HIPCHAT_MESSAGE.key(), HIPCHAT_MESSAGE);
        FIELDS.put(HIPCHAT_SERVER_URL.key(), HIPCHAT_SERVER_URL);
        FIELDS.put(HIPCHAT_ROOM.key(), HIPCHAT_ROOM);
        FIELDS.put(HIPCHAT_TOKEN.key(), HIPCHAT_TOKEN);
    }

    public GoPluginApiResponse execute() {
        return new DefaultGoPluginApiResponse(200, GSON.toJson(FIELDS));
    }
}

