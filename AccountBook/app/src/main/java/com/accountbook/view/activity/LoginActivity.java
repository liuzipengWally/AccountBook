package com.accountbook.view.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.accountbook.R;
import com.accountbook.presenter.LoginPresenter;
import com.accountbook.view.api.ILoginView;

public class LoginActivity extends BaseActivity implements ILoginView,View.OnFocusChangeListener {

    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;

    private EditText usernameInput;
    private EditText passwordInput;

    private Button loginBtn;

    private LoginPresenter loginPresenter;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPresenter = new LoginPresenter(this);

        initView();
    }

    public void initView() {
        //绑定控件
        usernameWrapper = (TextInputLayout) findViewById(R.id.username_wrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.password_wrapper);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        usernameInput = usernameWrapper.getEditText();
        passwordInput = passwordWrapper.getEditText();

        //设置获得焦点失去焦点的点击事件更好的控制错误提示
        usernameInput.setOnFocusChangeListener(this);
        passwordInput.setOnFocusChangeListener(this);

        //设置TextInputLayout显示错误
        usernameWrapper.setErrorEnabled(true);
        passwordWrapper.setErrorEnabled(true);

        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameInput.clearFocus();
                passwordInput.clearFocus();
                loginPresenter.dologin();
            }
        });
    }


    @Override
    public String getUsername() {
        return usernameInput.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordInput.getText().toString();
    }

    @Override
    public void loginSuccess() {
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginFailed(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUsernameError(String message) {
        usernameWrapper.setError(message);
    }

    @Override
    public void showPasswordError(String message) {
        passwordWrapper.setError(message);
    }

    /**
     * 当输入框失去焦点时判断内容是否为空，不是则清空错误消息
     * @param view 输入框
     * @param hasFocus 当前有没有获得焦点
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(!hasFocus){
            if(!((EditText)view).getText().toString().equals("")){
                //parent指两个输入框的各自外层的TextInputLayout，错误信息显示它们上面
                ((TextInputLayout)view.getParent()).setError("");
            }
        }
    }
}
