package com.accountbook.biz.api;

import com.accountbook.biz.impl.BudgetBiz;

public interface IBudgetBiz {
    /**
     * 查询预算方法
     *
     * @param queryListener 查询结果监听
     */
    void queryBudget(BudgetBiz.OnQueryBudgetListener queryListener);

    /**
     * 删除分类的方法
     *
     * @param id                   要删除的那个数据的id
     * @param deleteBudgetListener 删除监听
     */
    void delete(String id, BudgetBiz.OnDeleteBudgetListener deleteBudgetListener);

    /**
     * 恢复删除的数据
     *
     * @param id                     要恢复的数据的id
     * @param recoveryBudgetListener 恢复数据的监听
     */
    void recovery(String id, BudgetBiz.OnRecoveryBudgetListener recoveryBudgetListener);
}
