package com.accountbook.view.api;

public interface ILoginView {

    /**
     * 开始执行操作的UI变化
     */
    void uiBeginLogin();

    /**
     * 结束后的UI变化
     */
    void uiEndLogin();

    /**
     * 提供给presenter获取username的方法
     *
     * @return 用户名
     */
    String getLoginUsername();

    /**
     * 提供给presenter获取password的方法
     *
     * @return 密码
     */
    String getLoginPassword();

    /**
     * 登录成做的事情
     */
    void loginSuccess();

    /**
     * 登录失败做的事情
     */
    void loginFailed(String message);

    /**
     * 显示用户名错误
     *
     * @param message 错误信息
     */
    void showLoginUsernameError(String message);

    /**
     * 显示密码错误
     *
     * @param message 错误信息
     */
    void showLoginPasswordError(String message);
}
