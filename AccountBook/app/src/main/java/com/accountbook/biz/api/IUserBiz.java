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
}

