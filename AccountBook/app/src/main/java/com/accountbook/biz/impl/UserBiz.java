package com.accountbook.biz.impl;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.api.OnLoginListener;

/**
 * Created by Grady on 2016.2.21.
 */
public class UserBiz implements IUserBiz {
    @Override
    public void login(String username, String password, OnLoginListener listener) {
//        listener.loginSuccess();
        listener.loginFailed("用户名密码错误");
    }
}
