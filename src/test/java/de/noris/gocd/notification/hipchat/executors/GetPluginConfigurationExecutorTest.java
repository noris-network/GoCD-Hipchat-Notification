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

import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class GetPluginConfigurationExecutorTest {

    @Test
    public void shouldSerializeAllFields() throws Exception {
        GoPluginApiResponse response = new GetPluginConfigurationExecutor().execute();
        HashMap hashMap = new Gson().fromJson(response.responseBody(), HashMap.class);
        assertEquals("Are you using anonymous inner classes — see https://github.com/google/gson/issues/298",
                hashMap.size(),
                GetPluginConfigurationExecutor.FIELDS.size()
        );
    }

    @Test
    public void assertJsonStructure() throws Exception {
        GoPluginApiResponse response = new GetPluginConfigurationExecutor().execute();

        assertThat(response.responseCode(), CoreMatchers.is(200));
        String expectedJSON = "{ \"hipchat_server_url\":" +
                              "      {\"display-name\":\"Server URL\"," +
                              "       \"default-value\":\"https://api.hipchat.com\"," +
                              "       \"required\":true," +
                              "       \"secure\":true," +
                              "       \"display-order\":\"0\"" +
                              "      }," +
                              "      \"hipchat_room\":" +
                              "        {\"display-name\":\"Room\"," +
                              "         \"required\":true," +
                              "         \"secure\":false," +
                              "         \"display-order\":\"1\"" +
                              "        }," +
                              "     \"hipchat_token\":" +
                              "        {\"display-name\":\"API Token\"," +
                              "         \"required\":true," +
                              "         \"secure\":true," +
                              "         \"display-order\":\"2\"" +
                              "        }," +
                              "     \"notify_start\":" +
                              "        {\"display-name\":\"Notify on job start\"," +
                              "         \"default-value\":\"true\"," +
                              "         \"required\":true," +
                              "         \"secure\":false," +
                              "         \"display-order\":\"4\"" +
                              "        }," +
                              "     \"message_start\":" +
                              "       {\"display-name\":\"Start message\"," +
                              "        \"default-value\":\"Job started\"," +
                              "        \"required\":true," +
                              "        \"secure\":false," +
                              "        \"display-order\":\"5\"" +
                              "       }," +
                              "     \"color_start\":" +
                              "       {\"display-name\":\"Color for start message\"," +
                              "        \"default-value\":\"gray\"," +
                              "        \"required\":true," +
                              "        \"secure\":false," +
                              "        \"display-order\":\"6\"" +
                              "       }," +
                              "     \"notify_success\":" +
                              "       {\"display-name\":\"Notify on success\"," +
                              "        \"default-value\":\"true\"," +
                              "        \"required\":true," +
                              "        \"secure\":false," +
                              "        \"display-order\":\"7\"" +
                              "       }," +
                              "     \"message_success\":" +
                              "       {\"display-name\":\"Success message\"," +
                              "        \"default-value\":\"Job succeeded\"," +
                              "        \"required\":true," +
                              "        \"secure\":false," +
                              "        \"display-order\":\"8\"" +
                              "       }," +
                              "     \"color_success\":" +
                              "       {\"display-name\":\"Color for success message\"," +
                              "        \"default-value\":\"green\"," +
                              "        \"required\":true," +
                              "        \"secure\":false," +
                              "        \"display-order\":\"9\"" +
                              "      }," +
                              "     \"notify_failure\":" +
                              "       {\"display-name\":\"Notify on failure\"," +
                              "        \"default-value\":\"true\"," +
                              "        \"required\":true," +
                              "        \"secure\":false," +
                              "        \"display-order\":\"10\"" +
                              "       }," +
                              "     \"message_failure\":" +
                              "       {\"display-name\":\"Failure message\"," +
                              "        \"default-value\":\"Job failed\"," +
                              "        \"required\":true," +
                              "        \"secure\":false," +
                              "        \"display-order\":\"11\"" +
                              "       }," +
                              "     \"color_failure\":" +
                              "       {\"display-name\":\"Color for failure message\"," +
                              "        \"default-value\":\"red\"," +
                              "        \"required\":true," +
                              "        \"secure\":false," +
                              "        \"display-order\":\"12\"" +
                              "       }" +
                              "} \" " +
                              "}";
        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);

    }
}
