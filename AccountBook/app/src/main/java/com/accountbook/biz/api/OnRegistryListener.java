package com.accountbook.biz.api;

/**
 * Created by Grady on 2016.2.26.
 */
public interface OnRegistryListener {

    /**
     * 注册成功
     */
    void registrySuccess();

    /**
     * 注册失败
     * @param message 错误信息
     */
    void registryFailed(String message);

}
