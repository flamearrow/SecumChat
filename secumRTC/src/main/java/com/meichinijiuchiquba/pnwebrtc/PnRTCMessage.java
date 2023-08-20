package com.meichinijiuchiquba.pnwebrtc;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by flamearrow on 2/26/17.
 */

public class PnRTCMessage extends JSONObject {
    public static final String JSON_TYPE = "type";
    public static final String JSON_PACKET = "packet";
    public static final String JSON_ID = "id";
    public static final String JSON_SENDER_ID = "senderId";
    public static final String JSON_SENDER_NICK_NAME = "senderNickName";
    public static final String JSON_SENDER_GENDER = "senderGender";
    public static final String JSON_CALLER_ID = "callerId";
    public static final String JSON_MESSAGE = "message";
    public static final String JSON_TIME = "time";
    public static final String JSON_USERMSG = "usermsg";
    public static final String JSON_HANGUP = "hangup";
    public static final String JSON_ADDTIME = "addtime";
    public static final String JSON_DIAL = "dial";
    public static final String JSON_THUMBNAIL = "thumbnail";
    public static final String JSON_SDP = "sdp";
    public static final String JSON_ICE = "candidate"; // Identify ICE
    public static final String JSON_MESSAGE_GROUP_ID = "msgGroupId"; // Identify ICE

    // gate all incoming messages
    public static final String META_MSG = "META_MSG";

    private String message;
    private JSONObject json;

    public PnRTCMessage(String message) {
        super();
        try {
            this.put(JSON_MESSAGE, message);
        } catch (JSONException e) {
            throw new RuntimeException("Invalid JSON Payload");
        }
        this.message = message;
        this.json = this;
    }

    public PnRTCMessage(JSONObject json) {
        super();
        try {
            if (json == null) {
                json = new JSONObject("{error:1}");
            }
            this.put(JSON_MESSAGE, json);
        } catch (JSONException e) {
            throw new RuntimeException("Invalid JSON Payload");
        }
        this.message = json.toString();
        this.json = this;
    }

    public String getMessage() {
        return message;
    }

    public JSONObject getJSON() {
        return this.json;
    }
}
