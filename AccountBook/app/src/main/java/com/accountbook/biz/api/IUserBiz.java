package com.accountbook.biz.api;

public interface IUserBiz {
    void login(String username,String password,OnLoginListener listener);
}
