package com.accountbook.presenter;

import android.os.Handler;

import com.accountbook.biz.api.IEditBudgetBiz;
import com.accountbook.biz.impl.EditBudgetBiz;
import com.accountbook.entity.Budget;
import com.accountbook.view.api.IEditBudgetView;

public class EditBudgetPresenter {
    private IEditBudgetView mBudgetView;
    private IEditBudgetBiz mBudgetBiz;

    public EditBudgetPresenter(IEditBudgetView BudgetView) {
        this.mBudgetView = BudgetView;
        this.mBudgetBiz = new EditBudgetBiz();
    }

    public void saveBudget() {
        Budget budget = mBudgetView.getBudgetInfo();

        if (validateComplete(budget)) {
            mBudgetBiz.saveBudget(budget, new EditBudgetBiz.OnBudgetSaveListener() {
                @Override
                public void saveSuccess() {
                    mBudgetView.saveSuccess();
                }

                @Override
                public void saveFailed() {
                    mBudgetView.saveFailed();
                }
            });
        }
    }


    private boolean validateComplete(Budget budget) {
        if (budget.getCountMoney() == 0) {
            mBudgetView.validateFailed("请填写金额");

            return false;
        } else if (budget.getClassifyId() == null || budget.getClassifyId().equals("")) {
            mBudgetView.validateFailed("必须选择分类");

            return false;
        }
        return true;
    }

    public void loadAlterBudgetDate(final String id) {
        mBudgetBiz.queryBudget(id, new EditBudgetBiz.OnQueryBudgetListener() {
            @Override
            public void querySuccess(Budget budgetAdds) {
                mBudgetView.loadAlterBudgetDate(budgetAdds);
            }

            @Override
            public void queryFailed() {
                loadAlterBudgetDate(id);
            }
        });
    }

    public void alterBudget() {
        Budget budget = mBudgetView.getBudgetInfo();
        if (validateComplete(budget)) {
            mBudgetBiz.alterBudget(budget, new EditBudgetBiz.OnAlterBudgetListener() {
                @Override
                public void alterSuccess() {
                    mBudgetView.alterSuccess();
                }

                @Override
                public void alterFailed() {
                    mBudgetView.alterFailed();
                }
            });
        }
    }
}
