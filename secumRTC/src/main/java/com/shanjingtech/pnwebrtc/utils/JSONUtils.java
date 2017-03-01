package com.shanjingtech.pnwebrtc.utils;

import android.support.annotation.Nullable;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by flamearrow on 2/28/17.
 */

public class JSONUtils {
    public static final String JSON_TAG = "JSONUtils";

    /**
     * Convert gson to json, we may go fancy later
     *
     * @param jsonElement
     * @return JSONObject or null if error
     */
    @Nullable
    public static JSONObject convertFrom1(JsonElement jsonElement) {
        try {
            return new JSONObject(jsonElement.getAsJsonObject().toString()).getJSONObject
                    ("nameValuePairs");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;

        }
    }

    public static JSONObject convertFrom(JsonElement jsonElement) {
        try {
            JSONObject root = new JSONObject(jsonElement.getAsJsonObject().toString());
            return normalizeRoot(root);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String NVP = "nameValuePairs";

    private static JSONObject normalizeRoot(JSONObject root) throws JSONException {
        JSONObject ret = new JSONObject();
        Iterator<String> keyItr = root.keys();
        while (keyItr.hasNext()) {
            String key = keyItr.next();
            // only one NVP
            if (key.equals(NVP)) {
                JSONObject subJson = normalizeRoot(root.getJSONObject(NVP));
                Iterator<String> subKeyItr = subJson.keys();
                while (subKeyItr.hasNext()) {
                    String subKey = subKeyItr.next();
                    ret.put(subKey, subJson.get(subKey));
                }
            } else {
                Object value = root.get(key);
                if (value instanceof JSONObject) {
                    ret.put(key, normalizeRoot((JSONObject) value));
                } else {
                    ret.put(key, value);
                }
            }
        }
        return ret;
    }
}