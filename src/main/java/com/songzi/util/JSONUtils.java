package com.songzi.util;

public class JSONUtils {
    /**
     * @param json
     * @return
     */
    public static boolean isJSONFormat(String json) {
        if (json == null && json.length() == 0)
            return false;

        if (json.startsWith("{") || json.startsWith("["))
            if (json.endsWith("{") || json.endsWith("]"))
                return true;

        return false;
    }
}
