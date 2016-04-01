package com.accountbook.biz.api;

import com.accountbook.biz.impl.EditBudgetBiz;
import com.accountbook.entity.local.Budget;

public interface IEditBudgetBiz {
    /**
     * 保存记录
     *
     * @param budget               包含一条记录信息的实体
     * @param onBudgetSaveListener 保存记录的监听器
     */
    void saveBudget(Budget budget, EditBudgetBiz.OnBudgetSaveListener onBudgetSaveListener);

    /**
     * 查询要修改的那条记录
     *
     * @param id                    要修改的记录的id
     * @param onQueryBudgetListener
     */
    void queryBudget(String id, EditBudgetBiz.OnQueryBudgetListener onQueryBudgetListener);

    /**
     * 修改记录
     *
     * @param budget                要修改的记录实体
     * @param onAlterBudgetListener 修改的监听
     */
    void alterBudget(Budget budget, EditBudgetBiz.OnAlterBudgetListener onAlterBudgetListener);
}
