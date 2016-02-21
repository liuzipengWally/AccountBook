package com.accountbook.view.activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.accountbook.R;
import com.accountbook.presenter.LoginPresenter;
import com.accountbook.viewAPI.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView{

    TextInputLayout usernameWrapper;
    TextInputLayout passwordWrapper;

    EditText usernameInput;
    EditText passwordInput;

    Button loginBtn;

    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPresenter = new LoginPresenter(this);

        initView();
    }

    public void initView(){
        usernameWrapper = (TextInputLayout)findViewById(R.id.username_wrapper);
        passwordWrapper = (TextInputLayout)findViewById(R.id.password_wrapper);

        usernameInput = usernameWrapper.getEditText();
        passwordInput = passwordWrapper.getEditText();

        usernameWrapper.setErrorEnabled(true);
        passwordWrapper.setErrorEnabled(true);

        loginBtn = (Button)findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameWrapper.setErrorEnabled(false);
                passwordWrapper.setErrorEnabled(false);
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
}
