package com.accountbook.biz.impl;

import android.content.Context;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.api.OnLoginListener;
import com.accountbook.entity.UserForLeanCloud;
import com.accountbook.tools.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;


public class UserBiz implements IUserBiz {

    private static final UserBiz instance = new UserBiz();

    private UserBiz(){}

    public static UserBiz getInstance(){
        return instance;
    }

    /**
     * 处理登录
     * @param context 上下文对象
     * @param username 用户名
     * @param password 密码
     * @param listener 登录结果回调
     */
    @Override
    public void login(Context context,final String username, final String password, final OnLoginListener listener) {

        if(Util.isNetworkAvailable(context)){

            AVUser.logInInBackground(username, password, new LogInCallback<UserForLeanCloud>() {
                @Override
                public void done(UserForLeanCloud avUser, AVException e) {
                    if(avUser == null){
                        listener.loginFailed("用户名密码错误");
                    }else{
                        listener.loginSuccess();
                    }
                }
            },UserForLeanCloud.class);

        }else{
            listener.loginFailed("请联网后重试");
        }
    }

}
