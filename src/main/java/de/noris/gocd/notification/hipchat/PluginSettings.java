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

public class PluginSettings {
    private static final Gson GSON = new GsonBuilder().
            excludeFieldsWithoutExposeAnnotation().
            create();

    @Expose
    @SerializedName("hipchat_server_url")
    private String serverUrl;

    @Expose
    @SerializedName("hipchat_token")
    private String token;

    @Expose
    @SerializedName("hipchat_room")
    private String room;

    @Expose
    @SerializedName("notify_start")
    private boolean notifyStart;

    @Expose
    @SerializedName("message_start")
    private String messageStart;

    @Expose
    @SerializedName("color_start")
    private String colorStart;

    @Expose
    @SerializedName("notify_success")
    private boolean notifySuccess;

    @Expose
    @SerializedName("message_success")
    private String messageSuccess;

    @Expose
    @SerializedName("color_success")
    private String colorSuccess;

    @Expose
    @SerializedName("notify_failure")
    private boolean notifyFailure;

    @Expose
    @SerializedName("message_failure")
    private String messageFailure;

    @Expose
    @SerializedName("color_failure")
    private String colorFailure;

    public static PluginSettings fromJSON(String json) {
        return GSON.fromJson(json, PluginSettings.class);
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

    public boolean isNotifyStart() { return notifyStart; }

    public String getMessageStart() { return messageStart; }

    public boolean isNotifySuccess() { return notifySuccess; }

    public String getMessageSuccess() { return messageSuccess; }

    public String getColorSuccess() { return colorSuccess; }

    public boolean isNotifyFailure() { return notifyFailure; }

    public String getMessageFailure() { return messageFailure; }

    public String getColorFailure() { return colorFailure; }

    public String getColorStart() { return colorStart; }

}
