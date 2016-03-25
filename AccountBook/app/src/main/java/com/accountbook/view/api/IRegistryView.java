package com.accountbook.view.api;

/**
 * Created by Grady on 2016.2.24.
 */
public interface IRegistryView {

    /**
     * 显示进度
     */
    void showProgress();


    /**
     * 提供用户名
     *
     * @return 填入的用户名
     */
    String getRegUsername();

    /**
     * 提供密码
     *
     * @return 填入的密码
     */
    String getRegPassword();

    /**
     * 提供重复密码
     *
     * @return 填入的密码确认
     */
    String getRegPasswordConfirm();

    /**
     * 注册成功
     */
    void registerSuccess();

    /**
     * 注册失败
     *
     * @param message 错误信息
     */
    void registerFailed(String message);
}
