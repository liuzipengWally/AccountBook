package com.accountbook.biz.api;

import com.accountbook.entity.AccountBill;

import java.util.List;

/**
 * Created by liuzipeng on 16/2/24.
 */
public interface OnHomeQueryDataListener {
    void querySuccess(List<AccountBill> accountBills, String income, String expend, String balance);

    void queryFailed();
}
