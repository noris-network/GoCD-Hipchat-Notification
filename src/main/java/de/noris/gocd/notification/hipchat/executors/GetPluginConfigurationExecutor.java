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

public class GetPluginConfigurationExecutor implements RequestExecutor {

    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static final Field HIPCHAT_SERVER_URL = new NonBlankField("hipchat_server_url", "Server URL", "https://api.hipchat.com", true, true, "0");
    public static final Field HIPCHAT_ROOM = new NonBlankField("hipchat_room", "Room", null, true, false, "1");
    public static final Field HIPCHAT_TOKEN = new NonBlankField("hipchat_token", "API Token", null, true, true, "2");

    public static final Field NOTIFY_START   = new NonBlankField("notify_start", "Notify on job start", "true", true, false, "3");
    public static final Field MESSAGE_START  = new Field("message_start", "Start message", "Job started", true, false, "4");
    public static final Field COLOR_START    = new Field("color_start", "Color for start message", "gray", true, false, "5");

    public static final Field NOTIFY_SUCCESS  = new NonBlankField("notify_success", "Notify on success", "true", true, false, "6");
    public static final Field MESSAGE_SUCCESS = new Field("message_success", "Success message", "Job succeeded", true, false, "7");
    public static final Field COLOR_SUCCESS   = new Field("color_success", "Color for success message", "green", true, false, "8");

    public static final Field NOTIFY_FAILURE  = new NonBlankField("notify_failure", "Notify on failure", "true", true, false, "9");
    public static final Field MESSAGE_FAILURE = new Field("message_failure", "Failure message", "Job failed", true, false, "10");
    public static final Field COLOR_FAILURE   = new Field("color_failure", "Color for failure message", "red", true, false, "11");


    public static final Map<String, Field> FIELDS = new LinkedHashMap<>();

    static {
        FIELDS.put(HIPCHAT_SERVER_URL.key(), HIPCHAT_SERVER_URL);
        FIELDS.put(HIPCHAT_ROOM.key(), HIPCHAT_ROOM);
        FIELDS.put(HIPCHAT_TOKEN.key(), HIPCHAT_TOKEN);
        FIELDS.put(NOTIFY_START.key(), NOTIFY_START);
        FIELDS.put(MESSAGE_START.key(), MESSAGE_START);
        FIELDS.put(COLOR_START.key(), COLOR_START);
        FIELDS.put(NOTIFY_SUCCESS.key(), NOTIFY_SUCCESS);
        FIELDS.put(MESSAGE_SUCCESS.key(), MESSAGE_SUCCESS);
        FIELDS.put(COLOR_SUCCESS.key(), COLOR_SUCCESS);
        FIELDS.put(NOTIFY_FAILURE.key(), NOTIFY_FAILURE);
        FIELDS.put(MESSAGE_FAILURE.key(), MESSAGE_FAILURE);
        FIELDS.put(COLOR_FAILURE.key(), COLOR_FAILURE);
    }

    public GoPluginApiResponse execute() {
        return new DefaultGoPluginApiResponse(200, GSON.toJson(FIELDS));
    }
}

