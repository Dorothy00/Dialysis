package com.reader.dialysis.app;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.reader.dialysis.Model.AVChapter;
import com.reader.dialysis.Model.AVTableContents;
import com.reader.dialysis.Model.AVUserBook;

/**
 * Created by dorothy on 15/5/6.
 */
public class DialysisApplication extends Application {

    private final String leanCloud_AppId = "8y9d8fja6b7d1twphvmtml75a64jq3wpcqwzsobkr0meuxjp";
    private final String leanCloud_AppKey = "b5fu2h20g5dfqpvula3x060sfv9bm3gko1e7joc5hr7afb9u";

    @Override
    public void onCreate() {
        super.onCreate();
        AVObject.registerSubclass(AVUserBook.class);
        AVObject.registerSubclass(AVTableContents.class);
        AVObject.registerSubclass(AVChapter.class);
        AVOSCloud.initialize(this, leanCloud_AppId, leanCloud_AppKey);
        AVOSCloud.setDebugLogEnabled(true);
    }
}
