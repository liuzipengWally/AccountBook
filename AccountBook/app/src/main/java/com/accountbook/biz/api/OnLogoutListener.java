package com.accountbook.biz.api;

/**
 * Created by Grady on 2016.3.12.
 */
public interface OnLogoutListener {

    /**
     * 登出完成
     */
    void logoutComplete();

    /**
     * 登出失败
     *
     * @param message 错误信息
     */
    void logoutFailed(String message);

    /**
     * 清除数据完成
     */
    void clearComplete();

    /**
     * 清除数据失败
     *
     * @param message 错误信息
     */
    void clearFailed(String message);
}
