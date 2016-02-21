package com.accountbook.modelAPI;

/**
 * Created by Grady on 2016.2.21.
 */
public interface OnLoginListener {
    void loginSuccess();
    void loginFailed(String message);
}
