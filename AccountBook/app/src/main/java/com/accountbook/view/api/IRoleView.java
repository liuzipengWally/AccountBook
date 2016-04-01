package com.accountbook.view.api;

import com.accountbook.entity.local.Role;

import java.util.List;

public interface IRoleView {
    /**
     * 保存成功
     */
    void saveSuccess();

    /**
     * 保存失败
     */
    void saveFailed();

    /**
     * 载入role数据
     *
     * @param roles
     */
    void loadRole(List<Role> roles);

    /**
     * 删除成功
     */
    void deleteSuccess();

    /**
     * 载入失败
     */
    void loadFailed();

    /**
     * 删除失败
     */
    void deleteFailed();

}
