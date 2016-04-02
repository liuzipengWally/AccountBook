package com.accountbook.view.api;

import com.accountbook.entity.local.AccountBill;

import java.util.List;

public interface IHomeView {
    /**
     * 显示查询错误信息
     */
    void showLoadDataFailed();

    /**
     * 在页面中载入数据
     *
     * @param accountBills 包含多个homeItem实体的集合
     */
    void LoadAccountBills(List<AccountBill> accountBills);

    /**
     * 加载主页卡片数据
     *
     * @param expend  支出
     * @param income  收入
     * @param balance 预算余额
     */
    void loadInfoCard(String expend, String income, String balance);

    /**
     * 加载主页卡片数据失败
     */
    void loadInfoCardFailed();

    /**
     * 显示载入数据的进度
     */
    void showLoadingProgress();

    /**
     * 隐藏载入数据的进度
     */
    void hideLoadingProgress();

    /**
     * 删除成功
     *
     * @param id 被删除的数据的ID
     */
    void deleteSuccess(String id);

    /**
     * 删除失败
     *
     * @param id 被删除的数据的ID
     */
    void deleteFailed(String id);

    /**
     * 恢复成功
     */
    void recoverySuccess();

    /**
     * 恢复失败
     *
     * @param id 被删除的数据的ID
     */
    void recoveryFailed(String id);
}
