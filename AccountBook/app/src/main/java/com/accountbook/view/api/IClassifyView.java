package com.accountbook.view.api;

import com.accountbook.entity.Classify;

import java.util.List;

/**
 * Created by liuzipeng on 16/3/12.
 */
public interface IClassifyView {
    /**
     * 载入分类数据
     *
     * @param classifies 分类数据实体集合
     */
    void loadData(List<Classify> classifies);

    /**
     * 载入数据失败
     */
    void loadFailed();
}
