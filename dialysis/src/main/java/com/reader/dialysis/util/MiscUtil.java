package com.reader.dialysis.util;

import android.content.Context;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

/**
 * Created by dorothy on 15/5/12.
 */
public class MiscUtil {
    public static void forceShowOverLap(Context context) {
        try {
            ViewConfiguration config = ViewConfiguration.get(context);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            // presumably, not relevant
        }
    }
}
