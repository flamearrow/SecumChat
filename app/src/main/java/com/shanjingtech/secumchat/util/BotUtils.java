package com.shanjingtech.secumchat.util;

public class BotUtils {
    private static String CHATGPT_NICK_NAME = "Bot:phone_+6661";
    private static String CHATGPT_ID = "13";
    private static String CHATGPT = "ChatGPT";
    private static String MIDJOURNEY_NICK_NAME = "Bot:phone_+6662";
    private static String MIDJOURNEY_ID = "18";
    private static String MIDJOURNEY = "Midjourney";


    public static String nickNameToBotName(String nickName) {
        if (CHATGPT_NICK_NAME.equals(nickName)) {
            return CHATGPT;
        } else if (MIDJOURNEY_NICK_NAME.equals(nickName)) {
            return MIDJOURNEY;
        } else {
            return nickName;
        }
    }

    public static String idToBotName(String id) {
        if (CHATGPT_ID.equals(id)) {
            return CHATGPT;
        } else if (MIDJOURNEY_ID.equals(id)) {
            return MIDJOURNEY;
        } else {
            return id;
        }
    }
}
