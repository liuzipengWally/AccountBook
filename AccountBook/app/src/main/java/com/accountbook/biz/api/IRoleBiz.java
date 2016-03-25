package com.accountbook.biz.api;

import com.accountbook.biz.impl.BudgetBiz;
import com.accountbook.biz.impl.RoleBiz;
import com.accountbook.entity.Role;

import java.util.List;

public interface IRoleBiz {
    /**
     * 查询预算方法
     *
     * @param onQueryRoleListener 查询结果监听
     */
    void queryRole(RoleBiz.OnQueryRoleListener onQueryRoleListener);

    /**
     * 删除role的方法
     *
     * @param id               要删除的那个数据的id
     * @param onDeleteListener 删除监听
     */
    void delete(String id, RoleBiz.OnDeleteListener onDeleteListener);

    /**
     * 恢复删除的数据
     *
     * @param role               要保存的角色实体
     * @param onRoleSaveListener 恢复数据的监听
     */
    void saveRole(Role role, RoleBiz.OnRoleSaveListener onRoleSaveListener);
}
