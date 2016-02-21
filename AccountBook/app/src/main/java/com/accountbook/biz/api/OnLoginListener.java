package com.accountbook.biz.api;


public interface OnLoginListener {
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
