package com.accountbook.biz.api;

import com.accountbook.entity.AccountBill;

import java.util.List;

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


    interface OnHomeQueryDataListener {
        void querySuccess(List<AccountBill> accountBills, String income, String expend, String balance);

        void queryFailed();
    }

}
