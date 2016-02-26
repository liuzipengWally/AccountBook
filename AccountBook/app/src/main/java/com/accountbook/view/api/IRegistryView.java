package com.accountbook.view.api;

/**
 * Created by Grady on 2016.2.24.
 */
public interface IRegistryView {
    /**
     * 提供用户名
     * @return 填入的用户名
     */
    String getUsername();

    /**
     * 提供密码
     * @return 填入的密码
     */
    String getPassword();

    /**
     * 提供重复密码
     * @return 填入的密码确认
     */
    String getPasswordConfirm();

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

    /**
     * 显示重复密码错误
     * @param message 错误信息
     */
    void showPasswordConfirmError(String message);

    /**
     * 注册成功
     */
    void registerSuccess();

    /**
     * 注册失败
     * @param message 错误信息
     */
    void registerFailed(String message);
}
