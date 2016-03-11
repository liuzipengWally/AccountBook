package com.accountbook.biz.api;

/**
 * Created by liuzipeng on 16/2/23.
 */
public interface IHomeLoadDataBiz {
    /**
     * 查询方法
     *
     * @param queryListener 查询结果监听
     */
    void query(OnHomeQueryDataListener queryListener);
}
