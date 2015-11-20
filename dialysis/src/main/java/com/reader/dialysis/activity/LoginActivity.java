package com.reader.dialysis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.reader.dialysis.util.UserCache;

import test.dorothy.graduation.activity.R;


/**
 * Login Page
 *
 * @author dorothy
 */
public class LoginActivity extends DialysisActivity implements View.OnClickListener {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private TextView mTvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtUsername = (EditText) findViewById(R.id.username);
        mEtPassword = (EditText) findViewById(R.id.password);
        mTvRegister = (TextView) findViewById(R.id.register);
        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnLogin.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);

        mEtUsername.setHintTextColor(getResources().getColor(R.color.hint_color));
        mEtPassword.setHintTextColor(getResources().getColor(R.color.hint_color));

        mEtUsername.addTextChangedListener(mTextWatcher);
        mEtPassword.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            doLoginInBackground();
        } else if (v.getId() == R.id.register) {
            startActivityForResult(new Intent(this, RegisterActivity.class), WelcomeActivity
                    .REQUEST_CODE_REGISTER);
        }

    }

    private void doLoginInBackground() {
        AVUser.logInInBackground(mEtUsername.getText().toString(), mEtPassword.getText()
                .toString(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    if (avUser != null) {
                        showToast("登陆成功！");
                        setResult(WelcomeActivity.RESULT_CODE_SUCCESS);
                        cacheUserInBackground(mEtUsername.getText().toString());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        showToast("用户名或密码错误！");
                        setLoginEnable(false);
                    }
                } else {
                    showToast("登陆失败！" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void cacheUserInBackground(final String username) {
        AVQuery<AVUser> query = new AVQuery<AVUser>("_User");
        query.whereEqualTo("username", username);
        query.getFirstInBackground(new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    int userId = avUser.getInt("user_id");
                    UserCache.cacheUser(LoginActivity.this, username, userId);
                }
            }
        });
    }

    private void setLoginEnable(boolean isValid) {
        int colorResId = R.color.disabled_common_blue;
        if (isValid) {
            colorResId = android.R.color.holo_blue_light;
        }
        mBtnLogin.setBackgroundResource(colorResId);
        mBtnLogin.setEnabled(isValid);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean isValid = !TextUtils.isEmpty(mEtUsername.getText()) && !TextUtils.isEmpty
                    (mEtPassword.getText());
            setLoginEnable(isValid);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WelcomeActivity.REQUEST_CODE_REGISTER && resultCode == WelcomeActivity
                .RESULT_CODE_SUCCESS) {
            setResult(WelcomeActivity.RESULT_CODE_SUCCESS);
            finish();
        }
    }
}
