package com.accountbook.view.api;

import com.accountbook.entity.Record;
import com.accountbook.entity.Role;

import java.util.List;

/**
 * Created by liuzipeng on 16/3/8.
 */
public interface IEditView {
    /**
     * 载入角色数据
     *
     * @param roles 角色
     */
    void loadRole(List<Role> roles);

    /**
     * 获取要保存的RecordAdd对象
     *
     * @return
     */
    Record getRecordInfo();

    /**
     * 获取角色数据失败
     */
    void loadRoleFailed();

    /**
     * 验证数据失败
     */
    void validateFailed(String msg);

    /**
     * 添加数据失败
     */
    void saveFailed();

    /**
     * 添加数据成功
     */
    void saveSuccess();

    /**
     * 载入要修改的数据
     */
    void loadAlterRecordDate(Record record);

    /**
     * 修改成功
     */
    void alterSuccess();

    /**
     * 修改失败
     */
    void alterFailed();
}
