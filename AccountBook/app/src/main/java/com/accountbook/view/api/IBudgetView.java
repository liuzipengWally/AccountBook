package com.accountbook.view.api;

import com.accountbook.entity.local.Budget;

import java.util.List;

public interface IBudgetView {
    /**
     * 显示查询错误信息
     */
    void showLoadDataFailed();

    /**
     * 在页面中载入数据
     *
     * @param budgets 包含多个budgets实体的集合
     */
    void LoadBudget(List<Budget> budgets);

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
