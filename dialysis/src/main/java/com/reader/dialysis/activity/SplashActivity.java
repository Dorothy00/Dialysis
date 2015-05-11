package com.reader.dialysis.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.avos.avoscloud.AVUser;

import test.dorothy.graduation.activity.R;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AVUser currentUser = AVUser.getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(this,WelcomeActivity.class));
        }else{
            startActivity(new Intent(this,HomeActivity.class));
        }
        finish();
    }

}
