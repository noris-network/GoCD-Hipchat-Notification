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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PluginSettingsTest {
    @Test
    public void shouldDeserializeFromJSON() throws Exception {
        PluginSettings pluginSettings = PluginSettings.fromJSON("{" +
                "\"hipchat_server_url\": \"http://foo.bar/hipchat\", " +
                "\"hipchat_token\": \"s3cr3t\", " +
                "\"hipchat_room\": \"aRoom\", " +
                "\"notify_start\": \"true\", " +
                "\"notify_success\": \"true\", " +
                "\"notify_failure\": \"false\", " +
                "\"message_start\": \"started\", " +
                "\"message_success\": \"passed\", " +
                "\"message_failure\": \"failed\", " +
                "\"color_start\": \"gray\", " +
                "\"color_success\": \"green\", " +
                "\"color_failure\": \"red\" " +
                "}");

        assertThat(pluginSettings.getServerUrl(), is("http://foo.bar/hipchat"));
        assertThat(pluginSettings.getRoom(), is("aRoom"));
        assertThat(pluginSettings.getToken(), is("s3cr3t"));
        assertThat(pluginSettings.isNotifyStart(), is(true));
        assertThat(pluginSettings.isNotifySuccess(), is(true));
        assertThat(pluginSettings.isNotifyFailure(), is(false));
        assertThat(pluginSettings.getMessageStart(), is("started"));
        assertThat(pluginSettings.getMessageSuccess(), is("passed"));
        assertThat(pluginSettings.getMessageFailure(), is("failed"));
        assertThat(pluginSettings.getColorStart(), is("gray"));
        assertThat(pluginSettings.getColorSuccess(), is("green"));
        assertThat(pluginSettings.getColorFailure(), is("red"));
    }
}
