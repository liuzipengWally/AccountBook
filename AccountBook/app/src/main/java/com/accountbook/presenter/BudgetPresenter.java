package com.accountbook.presenter;

import android.content.Context;

import com.accountbook.biz.api.IBudgetBiz;
import com.accountbook.biz.impl.BudgetBiz;
import com.accountbook.entity.local.Budget;
import com.accountbook.tools.QuickSimpleIO;
import com.accountbook.view.api.IBudgetView;

import java.util.List;

/**
 * 预算数据查询的Presenter层交互类
 */
public class BudgetPresenter {
    private IBudgetView mBudgetView;
    private IBudgetBiz mBudgetBiz;

    private QuickSimpleIO mSimpleIO;

    public BudgetPresenter(IBudgetView mIBudgetView, Context context) {
        this.mBudgetView = mIBudgetView;
        this.mBudgetBiz = new BudgetBiz();
        this.mSimpleIO = new QuickSimpleIO(context, "version_sp");
    }

    /**
     * 查询到结果之后对UI做具体的操作
     */
    public void queryBudget() {
        mBudgetView.showLoadingProgress();
        mBudgetBiz.queryBudget(new BudgetBiz.OnQueryBudgetListener() {
            @Override
            public void querySuccess(List<Budget> budgets) {
                mBudgetView.LoadBudget(budgets);
                mBudgetView.hideLoadingProgress();
            }

            @Override
            public void queryFailed() {
                mBudgetView.showLoadDataFailed();
                mBudgetView.hideLoadingProgress();
            }
        });
    }

    public void deleteBudget(final String id) {
        mBudgetBiz.delete(id, new BudgetBiz.OnDeleteBudgetListener() {
            @Override
            public void deleteSuccess() {
                mBudgetView.deleteSuccess(id);
                mSimpleIO.setBoolean("needSync", true);
            }

            @Override
            public void deleteFailed() {
                mBudgetView.deleteFailed(id);
            }
        });
    }

    public void recoveryBudget(final String id) {
        mBudgetBiz.recovery(id, new BudgetBiz.OnRecoveryBudgetListener() {
            @Override
            public void recoverySuccess() {
                mBudgetView.recoverySuccess();
            }

            @Override
            public void recoveryFailed() {
                mBudgetView.recoveryFailed(id);
            }
        });
    }
}
