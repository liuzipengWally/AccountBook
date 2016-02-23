package com.accountbook.biz.api;

import com.accountbook.entity.HomeItem;

import java.util.List;

/**
 * Created by liuzipeng on 16/2/24.
 */
public interface OnHomeQueryDataListener {
    void querySuccess(List<HomeItem> homeItems, String income, String expend, String balance);

    void queryFailed();
}
