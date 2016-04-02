package com.accountbook.biz.api;

import com.accountbook.biz.impl.ClassifyBiz;
import com.accountbook.biz.impl.HomeBiz;

/**
 * Created by liuzipeng on 16/2/23.
 */
public interface IHomeBiz {
    /**
     * 查询账单方法
     *
     * @param queryListener 查询结果监听
     */
    void queryAccountBills(long startTime, long endTime, HomeBiz.OnQueryAccountBillsListener queryListener);

    /**
     * 查询主页卡片中的数据
     */
    void queryInfoCardData(HomeBiz.OnQueryInfoCardDataListener listener);

    /**
     * 删除分类的方法
     *
     * @param id                         要删除的那个数据的id
     * @param deleteAccountBillsListener 删除监听
     */
    void delete(String id, HomeBiz.OnDeleteAccountBillsListener deleteAccountBillsListener);

    /**
     * 恢复删除的数据
     *
     * @param id                           要恢复的数据的id
     * @param recoveryAccountBillsListener 恢复数据的监听
     */
    void recovery(String id, HomeBiz.OnRecoveryAccountBillsListener recoveryAccountBillsListener);
}
