package com.accountbook.biz.api;

import com.accountbook.biz.impl.SyncBiz;

public interface ISyncBiz {
    /**
     * 上传记录
     */
    void syncRecord();

    /**
     * 上传家庭角色
     */
    void syncRole();

    /**
     * 上传分类
     */
    void syncClassify();

    /**
     * 上传预算
     */
    void syncBudget();

    /**
     * 同步version表
     */
    void syncVersion();

    void setOnPostErrorListener(SyncBiz.OnSyncErrorListener listener);

    void setOnSyncVersionListener(SyncBiz.OnSyncVersionListener listener);
}
