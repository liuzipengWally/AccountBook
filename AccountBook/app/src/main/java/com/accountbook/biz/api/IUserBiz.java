package com.accountbook.biz.api;

import android.content.Context;

import com.accountbook.biz.impl.UserBiz;

public interface IUserBiz {
    /**
     * 处理登录
     *
     * @param context  上下文对象
     * @param username 用户名
     * @param password 密码
     * @param listener 登录结果回调
     */
    void login(Context context, String username, String password, UserBiz.OnLoginListener listener);

    /**
     * 处理注册
     *
     * @param context  上下文
     * @param username 用户名
     * @param password 密码
     * @param listener 注册结果回调
     */
    void registry(Context context, String username, String password, UserBiz.OnRegistryListener listener);

    /**
     * 处理登出
     *
     * @param context          上下文
     * @param isClearLocalData 是否清除现有数据
     */
    void logOut(Context context, boolean isClearLocalData, UserBiz.OnLogoutListener listener);
}

