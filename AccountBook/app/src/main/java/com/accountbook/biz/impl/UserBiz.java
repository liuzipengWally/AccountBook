package com.accountbook.biz.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.entity.UserForLeanCloud;
import com.accountbook.tools.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;


public class UserBiz implements IUserBiz {
    private SQLiteDatabase mDatabase;
    private Exception exception;

    public interface OnLogoutListener {

        /**
         * 登出完成
         */
        void logoutComplete();

        /**
         * 登出失败
         *
         * @param message 错误信息
         */
        void logoutFailed(String message);

        /**
         * 清除数据完成
         */
        void clearComplete();

        /**
         * 清除数据失败
         *
         * @param message 错误信息
         */
        void clearFailed(String message);
    }

    public interface OnLoginListener {
        /**
         * 登录成功回调
         */
        void loginSuccess();

        /**
         * 登录失败回调
         *
         * @param message 失败原因
         */
        void loginFailed(String message);
    }


    public interface OnRegistryListener {

        /**
         * 注册成功
         */
        void registrySuccess();

        /**
         * 注册失败
         *
         * @param message 错误信息
         */
        void registryFailed(String message);
    }

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
        if (Util.isNetworkAvailable(context)) {
            AVUser.logInInBackground(username, password, new LogInCallback<UserForLeanCloud>() {
                @Override
                public void done(UserForLeanCloud avUser, AVException e) {
                    if (avUser == null) {
                        listener.loginFailed(Util.getLocalizeLeanCloudError(e));
                    } else {
                        listener.loginSuccess();
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
        try {
            if (isClearLocalData) {
                SQLite.getInstance().clearData();
                listener.clearComplete();
            }
            AVUser.logOut();
            listener.logoutComplete();
        } catch (Exception e) {
            listener.clearFailed(e.getMessage());
        }
    }
}
