package com.accountbook.biz.api;

import com.accountbook.biz.impl.AddBiz;

/**
 * Created by liuzipeng on 16/3/8.
 */
public interface IAddBiz {
    void queryRole(AddBiz.OnQueryRoleListener onQueryRoleListener);

    //该查询的监听器
    interface OnQueryRoleListener {
        void querySuccess(String[] roles);

        void queryFailed();
    }
}
