package com.accountbook.biz.api;

import com.accountbook.biz.impl.ChartBiz;


public interface IChartBiz {
    /**
     * 查询分类占比数据
     *
     * @param startTime              开始时间
     * @param endTime                结束时间
     * @param onQueryPercentListener 查询监听
     */
    void queryClassifyPercent(int type, long startTime, long endTime, ChartBiz.OnQueryPercentListener onQueryPercentListener);

    /**
     * 查询家庭角色收支占比
     *
     * @param type
     * @param startTime              开始时间
     * @param endTime                结束时间
     * @param onQueryPercentListener 查询监听
     */
    void queryRolePercent(int type, long startTime, long endTime, ChartBiz.OnQueryPercentListener onQueryPercentListener);
}
