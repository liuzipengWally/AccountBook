package com.accountbook.biz.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.api.OnLoginListener;
import com.accountbook.biz.api.OnLogoutListener;
import com.accountbook.biz.api.OnRegistryListener;
import com.accountbook.entity.UserForLeanCloud;
import com.accountbook.tools.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;


public class UserBiz implements IUserBiz {
    private SQLiteDatabase mDatabase;
    private Exception exception;

    public UserBiz() {
        this.mDatabase = SQLite.getInstance().getDatabaseObject();
    }

    /**
     * 处理登录
     *
     * @param context  上下文对象
     * @param username 用户名
     * @param password 密码
     * @param listener 登录结果回调
     */
    @Override
    public void login(final Context context, final String username, final String password, final OnLoginListener listener) {
        if (AVUser.getCurrentUser() != null) {
            listener.loginFailed("请注销当前账户");
            return;
        }

        if (Util.isNetworkAvailable(context)) {
            AVUser.logInInBackground(username, password, new LogInCallback<UserForLeanCloud>() {

                @Override
                public void done(UserForLeanCloud avUser, AVException e) {
                    if (avUser == null) {
                        listener.loginFailed(Util.getLocalizeLeanCloudError(e));
                    } else {
                        listener.loginSuccess();
                        Sync.getInstance().loadUserData(avUser);
                    }
                }
            }, UserForLeanCloud.class);

        } else {
            listener.loginFailed("请联网后重试");
        }
    }


    /**
     * 处理注册
     *
     * @param context  上下文
     * @param username 用户名
     * @param password 密码
     * @param listener 注册结果回调
     */
    @Override
    public void registry(Context context, String username, String password, final OnRegistryListener listener) {
        if (Util.isNetworkAvailable(context)) {
            final UserForLeanCloud user = new UserForLeanCloud();
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        //没错误表示注册成功
                        listener.registrySuccess();
                    } else {
                        listener.registryFailed(Util.getLocalizeLeanCloudError(e));
                    }
                }
            });
        } else listener.registryFailed("请连接网络后重试");
    }

    /**
     * 处理注销
     *
     * @param context          上下文
     * @param isClearLocalData 是否清除现有数据
     */
    @Override
    public void logOut(Context context, boolean isClearLocalData, final OnLogoutListener listener) {

        if (AVUser.getCurrentUser() == null) {
            listener.logoutFailed("未登录");
            return;
        }
        try {
            if (isClearLocalData) {
                SQLite.getInstance().clearData();
                listener.clearComplete();
            }
            AVUser.logOut();
            listener.logoutComplete();
        }catch (Exception e){
            listener.clearFailed(e.getMessage());
        }

    }


}
