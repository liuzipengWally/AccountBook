package com.accountbook.biz.api;

public interface OnLoginListener {
    void loginSuccess();
    void loginFailed(String message);
}
