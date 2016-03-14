package com.accountbook.biz.api;

import com.accountbook.biz.impl.AddBiz;
import com.accountbook.entity.RecordAdd;

/**
 * Created by liuzipeng on 16/3/8.
 */
public interface IAddBiz {
    /**
     * 用于初始化添加数据的activity中的role数据
     *
     * @param onQueryRoleListener 查询role数据的监听
     */
    void queryRole(AddBiz.OnQueryRoleListener onQueryRoleListener);

    /**
     * 保存记录
     *
     * @param record               包含一条记录信息的实体
     * @param onRecordSaveListener 保存记录的监听器
     */
    void saveRecord(RecordAdd record, AddBiz.OnRecordSaveListener onRecordSaveListener);
}
