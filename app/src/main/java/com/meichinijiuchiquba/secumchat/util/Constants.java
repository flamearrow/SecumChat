package com.meichinijiuchiquba.secumchat.util;

/**
 * Created by flamearrow on 2/26/17.
 */

public class Constants {
    public static final String IS_CALLER = "isCaller";
    public static final String IS_RECEIVER = "isReceiver";
    public static final String USER_NAME = "me.kg.androidrtc.SHARED_PREFS.USER_NAME";
    public static final String CALL_USER = "me.kg.androidrtc.SHARED_PREFS.CALL_USER";

    // ours
    public static final String PUB_KEY = "pub-c-f8c4ff9c-5f67-4e3f-8500-2ba2c5f783f0";
    public static final String SUB_KEY = "sub-c-65ab1c78-f6fd-11e6-ac91-02ee2ddab7fe";
    public static final String SEC_KEY = "sec-c-N2U1NTRlY2YtZjI3NC00NDdlLWEwMWItY2I3M2ZmM2I1ZmVk";

    public static final String JSON_CALL_USER = "call_user";
    public static final String JSON_CALL_TIME = "call_time";
    public static final String JSON_OCCUPANCY = "occupancy";
    public static final String JSON_STATUS = "status";

    // JSON for user messages
    public static final String JSON_USER_MSG = "user_message";
    public static final String JSON_MSG_UUID = "msg_uuid";
    public static final String JSON_MSG = "msg_message";
    public static final String JSON_TIME = "msg_timestamp";

    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_OFFLINE = "Offline";
    public static final String STATUS_BUSY = "Busy";


    public static final String VIDEO_TRACK_ID = "videoSecum";
    public static final String AUDIO_TRACK_ID = "audioSecum";
    public static final String LOCAL_MEDIA_STREAM_ID = "localStreamSecum";

    // logcat
    public static final String MLGB = "MLGB";
    public static final String RACE_CONDITION_TAG = "SECUM_RACE";

    // permission
    public static final int PERMISSION_CAMERA = 1;
    public static final int PERMISSION_RECORD_AUDIO = 2;
    public static final int PERMISSION_PHONE_STATE = 3;
    public static final int PERMISSION_ACCESS_FINE_LOCATION = 4;

    // intents
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String PROFILE_USER_NAME = "PROFILE_USER_NAME";

    public static final String PROFILE_USER_ID = "PROFILE_USER_ID";
    public static final String ACCESS_CODE = "CURRENT_USER";
    public static final String MY_NAME = "USER_NAME";
    public static final String MY_AGE = "USER_AGE";
    public static final String ME_MALE = "USER_IS_MALE";

    public static final String USER_NAME_PREVIX = "phone_";

    // shared preference
    public static final String PREF_FILE = "com.meichinijiuchiquba.secumchat.PREF_FILE";

    // counter
    public static final int TORA = 90;
    // after this point, notify user it's about to expire
    public static final int HANG_UP_TIME = 5;

    // grace time for caller to time out
    public static final int GRACE = 2;
    public static final int SECONDS_TO_ADD = 30;
    public static final long MILLIS_IN_SEC = 1000;

    //xir
    public static final String XIR_DOMAIN = "www.9miao.tv";
    public static final String XIR_ROOM = "default";
    public static final String XIR_APPLICATION = "9miao";
    public static final String XIR_USER = "gavinxyang";
    public static final String XIR_SECRET = "d4362d68-fbe9-11e6-a2ad-8345664c2cf3";

    // SecumAPI
    public static final String PASSWORD = "password";
    public static final String SHARED_PREF_ACCESS_TOKEN = "secumOAuth2Token";
    public static final String PATH_REGISTER_USER = "/api/users/";
    public static final String PATH_GET_ACCESS_CODE = "/api/users/get_access_code/";
    public static final String PATH_GET_ACCESS_TOKEN = "/api/o/token/";
    public static final String PATH_GET_MATCH = "/api/matches/get_match/";
    public static final String PATH_END_MATCH = "/api/matches/end_match/";
    public static final String PATH_PING = "/api/posts/";
    public static final String PATH_GET_PROFILE = "/api/users/get_profile/";
    public static final String PATH_UPDATE_USER = "/api/users/update_user/";
    public static final String PATH_REPORT = "/api/report/";
    public static final String PATH_LIST_CONTACTS = "/api/contacts/list_contact/";
    public static final String PATH_LIST_PENDING_REQUESTS = "/api/contacts/my/";
    public static final String PATH_PROFILE_FROM_USERNAME = "/api/contacts/get_profile/";
    public static final String PATH_ADD_CONTACT = "/api/contacts/add/";
    public static final String PATH_APPROVE_CONTACT = "/api/contacts/resolve/";
    public static final String PATH_BLOCK_CONTACT = "/api/contacts/block/";
    public static final String PATH_DELETE_CONTACT = "/api/contacts/delete/";
    public static final String PATH_REGISTER_NOTIFICATION_TOKEN = "/api/notification_token/register/";
    public static final String PATH_SEND_MESSAGE = "/api/messages/send_msg/";
    public static final String PATH_PULL_MESSAGE = "/api/message/pull/";

    public static final String PATH_PULL_GROUP_MESSAGE = "/api/messages/pull/";

    public static final String PATH_CREATE_GROUP = "/api/messages/create_grp/";
    public static final String PATH_GET_INFO = "/api/users/get_info/";

    public static final String LOAD_BOT_CHATS = "/api/users/load_bot_chats/";
    public static final String MALE = "male";
    public static final String FEMALE = "female";

    // urls
    public static final String PRIVACY_URL = "http://www.9miao.tv/policy.html";

    // contacts
    public static final String CONTACT_STATUS_ACTIVE = "ACTIVE";
    public static final String CONTACT_STATUS_REQUESTED = "REQUESTED";
    public static final String CONTACT_STATUS_BLOCKED = "BLOCKED";
    public static final String CONTACT_STATUS_PENDING = "PENDING";

    public static final String ACCOUNT_PREFIX = "phone_";
}
