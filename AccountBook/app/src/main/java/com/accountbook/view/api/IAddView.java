package com.accountbook.view.api;

import com.accountbook.entity.Role;

import java.util.Date;
import java.util.List;

/**
 * Created by liuzipeng on 16/3/8.
 */
public interface IAddView {
    /**
     * 载入角色数据
     *
     * @param roles 角色
     */
    void loadRole(String[] roles);

    /**
     * 获取金额
     *
     * @return 金额
     */
    int getMoney();

    /**
     * 获取描述信息
     *
     * @return 描述信息
     */
    String getDescription();

    /**
     * 获取债权人或债务人信息
     *
     * @return 债权人或债务人姓名
     */
    String getBorrowing();

    /**
     * 获取记录时间
     *
     * @return 时间
     */
    String getDate();

    /**
     * 获取账户源
     *
     * @return 账户
     */
    String getAccount();

    /**
     * 获取选中的角色下标
     *
     * @return 角色下标
     */
    int getRolePosition();

    /**
     * 获取选中的分类ID
     *
     * @return 分类ID
     */
    int getClassifyId();

    /**
     * 获取角色数据失败
     */
    void loadRoleFailed();

    /**
     * 添加数据失败
     */
    void addFailed();

    /**
     * 添加数据成功
     */
    void addSuccess();
}
