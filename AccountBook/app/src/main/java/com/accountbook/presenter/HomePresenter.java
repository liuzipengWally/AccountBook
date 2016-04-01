package com.accountbook.presenter;

import android.content.Context;

import com.accountbook.biz.api.IHomeBiz;
import com.accountbook.biz.impl.HomeBiz;
import com.accountbook.entity.local.AccountBill;
import com.accountbook.tools.QuickSimpleIO;
import com.accountbook.view.api.IHomeView;

import java.util.List;

/**
 * 主页数据查询的Presenter层交互类，负责HomeFragment与HomeLoadDataBiz的交互
 */
public class HomePresenter {
    private IHomeView mHomeView;
    private IHomeBiz mHomeBiz;

    private QuickSimpleIO mSimpleIO;

    public HomePresenter(IHomeView mIHomeView,Context context) {
        this.mHomeView = mIHomeView;
        this.mHomeBiz = new HomeBiz();
        this.mSimpleIO = new QuickSimpleIO(context, "version_sp");
    }

    /**
     * 查询到结果之后对UI做具体的操作
     */
    public void queryAccountBills(long startTime, long endTime) {
        mHomeView.showLoadingProgress();
        mHomeBiz.queryAccountBills(startTime, endTime, new HomeBiz.OnQueryAccountBillsListener() {
            @Override
            public void querySuccess(List<AccountBill> accountBills) {
                mHomeView.LoadAccountBills(accountBills);
                mHomeView.hideLoadingProgress();
            }

            @Override
            public void queryFailed() {
                mHomeView.showLoadDataFailed();
                mHomeView.hideLoadingProgress();
            }
        });
    }

    public void queryInfoCardData() {

    }

    public void deleteAccountBill(final String id) {
        mHomeBiz.delete(id, new HomeBiz.OnDeleteAccountBillsListener() {
            @Override
            public void deleteSuccess() {
                mSimpleIO.setBoolean("needSync", true);
                mSimpleIO.setInt("recordVer", mSimpleIO.getInt("recordVer") + 1);
                mHomeView.deleteSuccess(id);
            }

            @Override
            public void deleteFailed() {
                mHomeView.deleteFailed(id);
            }
        });
    }

    public void recoveryAccountBill(final String id) {
        mHomeBiz.recovery(id, new HomeBiz.OnRecoveryAccountBillsListener() {
            @Override
            public void recoverySuccess() {
                mHomeView.recoverySuccess();
            }

            @Override
            public void recoveryFailed() {
                mHomeView.recoveryFailed(id);
            }
        });
    }
}
