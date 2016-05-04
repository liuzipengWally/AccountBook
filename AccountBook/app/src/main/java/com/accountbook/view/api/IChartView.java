package com.accountbook.view.api;

import com.accountbook.entity.local.ChartData;

import java.util.List;

public interface IChartView {
    /**
     * 载入分类占比数据
     *
     * @param chartDatas 分类占比的实体集合
     */
    void loadClassifyPercent(List<ChartData> chartDatas, int type);

    /**
     * 载入角色收支占比数据
     *
     * @param chartDatas
     * @param type
     */
    void loadRolePercent(List<ChartData> chartDatas, int type);

    /**
     * 获取失败
     */
    void loadPercentFailed();
}
