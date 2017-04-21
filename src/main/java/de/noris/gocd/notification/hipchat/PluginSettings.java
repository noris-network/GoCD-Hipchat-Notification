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

package de.noris.gocd.notification.hipchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Implement any settings that your plugin needs
public class PluginSettings {
    private static final Gson GSON = new GsonBuilder().
            excludeFieldsWithoutExposeAnnotation().
            create();

    @Expose
    @SerializedName("notification_type")
    private String notificationType;

    @Expose
    @SerializedName("hipchat_message")
    private String message;

    @Expose
    @SerializedName("hipchat_room")
    private String room;

    @Expose
    @SerializedName("hipchat_token")
    private String token;

    @Expose
    @SerializedName("hipchat_server_url")
    private String serverUrl;

    public static PluginSettings fromJSON(String json) {
        return GSON.fromJson(json, PluginSettings.class);
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getMessage() {
        return message;
    }

    public String getRoom() {
        return room;
    }

    public String getToken() {
        return token;
    }

    public String getServerUrl() {
        return serverUrl;
    }
}
