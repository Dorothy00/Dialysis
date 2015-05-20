package com.reader.dialysis.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dorothy on 15/5/18.
 */
public class ReadHistoryUtil {

    private static final String HISTORY_KEY = "READ_HISTORY_KEY";

    public static void saveReadHistory(Context context,int bookId,int chapterId,long usedTime){
        SharedPreferences sharedPref = context.getSharedPreferences(HISTORY_KEY, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
    }
}
