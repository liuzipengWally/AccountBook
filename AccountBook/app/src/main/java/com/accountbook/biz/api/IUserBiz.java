package com.accountbook.biz.api;

import android.content.Context;

public interface IUserBiz {
    /**
     * 处理登录
     * @param context 上下文对象
     * @param username 用户名
     * @param password 密码
     * @param listener 登录结果回调
     */
    void login(Context context,String username,String password,OnLoginListener listener);

    /**
     * 处理注册
     * @param context 上下文
     * @param username 用户名
     * @param password 密码
     * @param listener 注册结果回调
     */
    void registry(Context context,String username,String password,OnRegistryListener listener);

    /**
     * 处理登出
     * @param context 上下文
     * @param isClearLocalData 是否清除现有数据
     */
    void logOut(Context context,boolean isClearLocalData,OnLogoutListener listener);



    interface OnLoginListener {
        /**
         * 登录成功回调
         */
        void loginSuccess();

        /**
         * 登录失败回调
         * @param message 失败原因
         */
        void loginFailed(String message);
    }


    interface OnLogoutListener {

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


    interface OnRegistryListener {

        /**
         * 注册成功
         */
        void registrySuccess();

        /**
         * 注册失败
         * @param message 错误信息
         */
        void registryFailed(String message);

    }
}

