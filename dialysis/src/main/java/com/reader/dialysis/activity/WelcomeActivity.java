package com.reader.dialysis.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import test.dorothy.graduation.activity.R;


public class WelcomeActivity extends Activity implements View.OnClickListener {

    public final static int REQUEST_CODE_LOGIN = 0x22;
    public final static int REQUEST_CODE_REGISTER = 0x23;
    public final static int RESULT_CODE_SUCCESS = 0x24;

    private Button mBtnLogin;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnRegister = (Button) findViewById(R.id.register);

        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE_LOGIN);

        } else if (v.getId() == R.id.register) {
            startActivityForResult(new Intent(this, RegisterActivity.class), REQUEST_CODE_REGISTER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean isLoginSuccess = requestCode == REQUEST_CODE_LOGIN && resultCode ==
                RESULT_CODE_SUCCESS;
        boolean isRegisterSuccess = requestCode == REQUEST_CODE_REGISTER && resultCode ==
                RESULT_CODE_SUCCESS;

        if (isLoginSuccess || isRegisterSuccess) {
            finish();
        }
    }
}
