package com.accountbook.viewAPI;

/**
 * Created by Grady on 2016.2.21.
 */
public interface ILoginView {
    /**
     * 提供给presenter获取username的方法
     * @return 用户名
     */
    String getUsername();

    /**
     * 提供给presenter获取password的方法
     * @return 密码
     */
    String getPassword();

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
     * @param message 错误信息
     */
    void showUsernameError(String message);

    /**
     * 显示密码错误
     * @param message 错误信息
     */
    void showPasswordError(String message);
}
