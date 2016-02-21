package com.accountbook.model;

import com.accountbook.modelAPI.IUserModel;
import com.accountbook.modelAPI.OnLoginListener;

/**
 * Created by Grady on 2016.2.21.
 */
public class UserModel implements IUserModel{
    @Override
    public void login(String username, String password, OnLoginListener listener) {
//        listener.loginSuccess();
        listener.loginFailed("用户名密码错误");
    }
}
