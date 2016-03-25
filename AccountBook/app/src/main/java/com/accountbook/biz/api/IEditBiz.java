package com.accountbook.biz.api;

import com.accountbook.biz.impl.EditBiz;
import com.accountbook.entity.Record;

public interface IEditBiz {
    /**
     * 用于初始化添加数据的activity中的role数据
     *
     * @param onQueryRoleListener 查询role数据的监听
     */
    void queryRole(EditBiz.OnQueryRoleListener onQueryRoleListener);

    /**
     * 保存记录
     *
     * @param record               包含一条记录信息的实体
     * @param onRecordSaveListener 保存记录的监听器
     */
    void saveRecord(Record record, EditBiz.OnRecordSaveListener onRecordSaveListener);

    /**
     * 查询要修改的那条记录
     *
     * @param id                    要修改的记录的id
     * @param onQueryRecordListener
     */
    void queryRecord(String id, EditBiz.OnQueryRecordListener onQueryRecordListener);

    /**
     * 修改记录
     *
     * @param record                小修改的记录实体
     * @param onAlterRecordListener 修改的监听
     */
    void alterRecord(Record record, EditBiz.OnAlterRecordListener onAlterRecordListener);
}
