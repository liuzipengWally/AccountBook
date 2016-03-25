package com.accountbook.presenter;

import com.accountbook.biz.api.IChartBiz;
import com.accountbook.biz.impl.ChartBiz;
import com.accountbook.entity.ChartData;
import com.accountbook.view.api.IChartView;

import java.util.List;

public class ChartPresenter {
    private IChartBiz mChartBiz;
    private IChartView mChartView;

    public ChartPresenter(IChartView mChartView) {
        this.mChartView = mChartView;
        this.mChartBiz = new ChartBiz();
    }

    public void loadClassifyPercent(final int type, long startTime, long endTime) {
        mChartBiz.queryClassifyPercent(type, startTime, endTime, new ChartBiz.OnQueryClassifyPercentListener() {
            @Override
            public void querySuccess(List<ChartData> chartDatas) {
                mChartView.loadClassifyPercent(chartDatas, type);
            }

            @Override
            public void queryFailed() {
                mChartView.loadClassifyPercentFailed();
            }
        });
    }
}
