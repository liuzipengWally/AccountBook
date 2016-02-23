package com.accountbook.presenter;

import com.accountbook.biz.api.IHomeLoadDataBiz;
import com.accountbook.biz.api.OnHomeQueryDataListener;
import com.accountbook.biz.impl.HomeLoadDataBiz;
import com.accountbook.entity.HomeItem;
import com.accountbook.view.api.IHomeView;

import java.util.List;

/**
 * 主页数据查询的Presenter层交互类，负责HomeFragment与HomeLoadDataBiz的交互
 */
public class HomeLoadDataPresenter {
    private IHomeView mHomeView;
    private IHomeLoadDataBiz mHomeLoadDataBiz;

    public HomeLoadDataPresenter(IHomeView mIHomeView) {
        this.mHomeView = mIHomeView;
        this.mHomeLoadDataBiz = new HomeLoadDataBiz();
    }

    /**
     * 查询到结果之后对UI做具体的操作
     */
    public void query() {
        mHomeLoadDataBiz.query(new OnHomeQueryDataListener() {
            @Override
            public void querySuccess(List<HomeItem> homeItems, String income, String expend, String balance) {
                mHomeView.LoadData(homeItems, income, expend, balance);
            }

            @Override
            public void queryFailed() {
                mHomeView.showLoadDataError();
            }
        });
    }
}
