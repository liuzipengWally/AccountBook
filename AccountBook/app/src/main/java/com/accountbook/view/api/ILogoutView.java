package com.accountbook.view.api;

/**
 * Created by Grady on 2016.3.12.
 */
public interface ILogoutView {


    /**
     * 是否清除数据
     * @return true == 是
     */
    boolean isClearData();

    /**
     *登出完成
     */
    void logoutComplete();

    /**
     * 登出失败
     * @param message 错误信息
     */
    void logoutFailed(String message);

    /**
     * 清除完成
     */
    void clearComplete();

    /**
     * 清除失败
     * @param message 错误信息
     */
    void clearFailed(String message);
}
