package com.accountbook.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.accountbook.R;
import com.accountbook.presenter.RegistryPresenter;
import com.accountbook.view.api.IRegistryView;

public class RegistryActivity extends AppCompatActivity implements IRegistryView{

    RegistryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        presenter = new RegistryPresenter(this);
        presenter.doRegistry();
    }

    /**
     * 提供用户名
     * @return 填入的用户名
     */
    @Override
    public String getUsername() {
        return "admin2";
    }

    /**
     * 提供密码
     * @return 填入的密码
     */
    @Override
    public String getPassword() {
        return "123456";
    }

    /**
     * 提供重复密码
     * @return 填入的密码确认
     */
    @Override
    public String getPasswordConfirm() {
        return "123456";
    }

    /**
     * 显示用户名错误
     * @param message 错误信息
     */
    @Override
    public void showUsernameError(String message) {

    }

    /**
     * 显示密码错误
     * @param message 错误信息
     */
    @Override
    public void showPasswordError(String message) {

    }

    /**
     * 显示重复密码错误
     * @param message 错误信息
     */
    @Override
    public void showPasswordConfirmError(String message) {

    }

    /**
     * 注册成功
     */
    @Override
    public void registerSuccess() {

    }

    /**
     * 注册失败
     * @param message 错误信息
     */
    @Override
    public void registerFailed(String message) {

    }
}
