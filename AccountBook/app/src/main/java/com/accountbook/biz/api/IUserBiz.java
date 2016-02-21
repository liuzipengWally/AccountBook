package com.accountbook.biz.api;

public interface IUserBiz {
    /**
     * 处理登录
     * @param username 用户名
     * @param password 密码
     * @param listener 登录结果回调
     */
    void login(String username,String password,OnLoginListener listener);


}

