package de.noris.gocd.notification.hipchat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by frank on 21.04.17.
 */
public class HipchatRequest {

        // see https://www.hipchat.com/docs/apiv2/method/send_room_notification for possible extensions

        public HipchatRequest(String color, String message, boolean notify, String messageFormat) {
            this.color = color;
            this.message = message;
            this.notify = notify;
            this.messageFormat = messageFormat;
        }

        public HipchatRequest(String message) {
            this("gray", message, false, "html");
        }

        public HipchatRequest(String color, String message) {
            this(color, message, false, "html");
        }

        @SerializedName("color")
        public String color;

        @SerializedName("message")
        public String message;

        @SerializedName("notify")
        public boolean notify = false;

        @SerializedName("message_format")
        public String messageFormat = "html";

}
