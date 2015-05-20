package com.reader.dialysis.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dorothy on 15/5/17.
 */
public class UserCache {
    private static final String USER_PREFERENCE = "USER_PREFERENCE";

    public static void cacheUser(Context context, String username, int userId) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.putInt("user_id", userId);
        editor.commit();
    }

    public static String getUsername(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context
                .MODE_PRIVATE);
        return sharedPref.getString("username", "");
    }

    public static int getUserId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context
                .MODE_PRIVATE);
        return sharedPref.getInt("user_id", 0);
    }
}
