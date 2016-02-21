package com.accountbook.biz.api;

/**
 * Created by Grady on 2016.2.21.
 */
public interface IUserBiz {
    void login(String username,String password,OnLoginListener listener);
}
