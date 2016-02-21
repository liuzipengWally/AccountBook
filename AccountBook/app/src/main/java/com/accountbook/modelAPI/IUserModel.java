package com.accountbook.modelAPI;

/**
 * Created by Grady on 2016.2.21.
 */
public interface IUserModel {
    void login(String username,String password,OnLoginListener listener);
}
