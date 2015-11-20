package com.reader.dialysis.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.avos.avoscloud.AVUser;
import com.reader.dialysis.util.UserCache;

import test.dorothy.graduation.activity.R;

public class SettingActivity extends DialysisActivity implements OnClickListener {

    private Button mBtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBtnLogout = (Button) findViewById(R.id.logout);
        mBtnLogout.setOnClickListener(this);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 0.1f;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout) {
            AVUser.getCurrentUser().logOut();
            setResult(HomeActivity.RESULT_CODE_HOME);
            UserCache.clear(this);
            finish();
        }
    }
}
