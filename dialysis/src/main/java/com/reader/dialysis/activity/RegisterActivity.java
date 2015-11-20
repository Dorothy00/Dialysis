package com.reader.dialysis.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SignUpCallback;
import com.reader.dialysis.util.UserCache;

import test.dorothy.graduation.activity.R;

/**
 * register page
 *
 * @author dorothy
 */
public class RegisterActivity extends DialysisActivity implements View.OnClickListener {

    private EditText mEtUsername;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtConfirmPassword;
    private Button mBtnRegister;
    private AlertDialog.Builder mDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtUsername = (EditText) findViewById(R.id.username);
        mEtEmail = (EditText) findViewById(R.id.email);
        mEtPassword = (EditText) findViewById(R.id.password);
        mEtConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mBtnRegister = (Button) findViewById(R.id.register);

        mEtUsername.setHintTextColor(getResources().getColor(R.color.hint_color));
        mEtEmail.setHintTextColor(getResources().getColor(R.color.hint_color));
        mEtPassword.setHintTextColor(getResources().getColor(R.color.hint_color));
        mEtConfirmPassword.setHintTextColor(getResources().getColor(R.color.hint_color));

        mEtUsername.addTextChangedListener(mTextChangeWatcher);
        mEtEmail.addTextChangedListener(mTextChangeWatcher);
        mEtPassword.addTextChangedListener(mTextChangeWatcher);
        mEtConfirmPassword.addTextChangedListener(mTextChangeWatcher);
        mBtnRegister.setOnClickListener(this);

        mDialogBuilder = new AlertDialog.Builder(this);
        mDialogBuilder.setTitle("注册错误").setCancelable(true).setPositiveButton("好 的", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (!isEmailValid(mEtEmail.getText().toString())) {
            showAlertDialog("邮箱格式不正确");
            return;
        }
        if (mEtPassword.getText().toString().length() < 5) {
            showAlertDialog("密码必须至少为5位");
            return;
        }
        if (!TextUtils.equals(mEtPassword.getText(), mEtConfirmPassword.getText())) {
            showAlertDialog("两次密码输入不一致");
            return;
        }

        doSignUpInBackground();
    }

    private void doSignUpInBackground() {
        AVQuery.doCloudQueryInBackground("select * from _User where username=? or email=?", new
                CloudQueryCallback<AVCloudQueryResult>() {


                    @Override
                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                        if (e == null) {
                            if (avCloudQueryResult.getResults().size() > 0) {
                                String errMsg = "";
                                for (AVObject avUser : avCloudQueryResult.getResults()) {
                                    if (avUser.get("username").equals(mEtUsername.getText()
                                            .toString())) {
                                        errMsg += "用户名已被注册，请重新选择一个。\n";
                                    } else if (avUser.get("email").equals(mEtEmail.getText()
                                            .toString())) {
                                        errMsg += "邮箱已被注册，请重新选择一个。\n";
                                    }
                                }
                                showAlertDialog(errMsg);
                            } else {
                                signUp();
                            }
                        } else {
                            showToast("注册失败 " + e.getMessage() + e.getCode());
                        }

                    }
                }, AVUser.class, mEtUsername.getText().toString(), mEtEmail.getText().toString());
    }


    private void signUp() {
        AVUser user = new AVUser();
        user.setUsername(mEtUsername.getText().toString());
        user.setPassword(mEtPassword.getText().toString());
        user.setEmail(mEtEmail.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("注册成功！");
                    setResult(WelcomeActivity.RESULT_CODE_SUCCESS);
                    cacheUserInBackground(mEtUsername.getText().toString());
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                } else {
                    showToast("注册失败！" + e.getMessage());
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
                    UserCache.cacheUser(RegisterActivity.this, username, userId);
                }
            }
        });
    }

    private boolean isRegisterInfoEmpty() {
        if (TextUtils.isEmpty(mEtUsername.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(mEtEmail.getText()
        )) {
            return false;
        }
        if (TextUtils.isEmpty(mEtPassword.getText())) {
            return false;
        }
        if (TextUtils.isEmpty
                (mEtConfirmPassword.getText())) {
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }

    private void showAlertDialog(String msg) {
        mDialogBuilder.setMessage("\n" + msg + "\n");
        AlertDialog dialog = mDialogBuilder.create();
        dialog.show();
    }

    private TextWatcher mTextChangeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean isValid = isRegisterInfoEmpty();

            int colorResId = R.color.disabled_common_blue;
            if (isValid) {
                colorResId = android.R.color.holo_blue_light;
            }
            mBtnRegister.setBackgroundResource(colorResId);
            mBtnRegister.setEnabled(isValid);

        }
    };
}
