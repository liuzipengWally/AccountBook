package com.accountbook.biz.impl;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.api.OnLoginListener;
import com.accountbook.entity.leancloud.User;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;


public class UserBiz implements IUserBiz {

    private static final UserBiz instance = new UserBiz();

    private LeanCloud leanCloud;
    private SQLite sqlite;

    private UserBiz(){
        leanCloud = LeanCloud.getInstance();
    }

    public static UserBiz getInstance(){
        return instance;
    }

    @Override
    public void login(final String username, final String password, final OnLoginListener listener) {

        if(leanCloud.available()){

            AVUser.logInInBackground(username, password, new LogInCallback<User>() {
                @Override
                public void done(User avUser, AVException e) {
                    if(avUser==null){
                        listener.loginFailed("用户名密码错误");
                    }else{
//                        System.out.println(avUser.getUsername());
//                        System.out.println(avUser.getObjectId());
//                        System.out.println(avUser.getDouble(User.MONEY));
                        listener.loginSuccess();

                    }
                }
            },User.class);

        }else{
            listener.loginFailed("请联网后重试");
        }
    }

}
