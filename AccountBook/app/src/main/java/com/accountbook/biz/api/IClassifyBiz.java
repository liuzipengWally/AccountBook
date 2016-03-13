package com.accountbook.biz.api;

import com.accountbook.biz.impl.ClassifyBiz;
import com.accountbook.entity.Classify;

import java.util.List;

/**
 * Created by liuzipeng on 16/3/12.
 */
public interface IClassifyBiz {
    /**
     * 分类数据的查询方法
     * 类别有四种，分别为expend，income，borrow，lend四个常量在常量库ConstantContainer中
     *
     * @param type             要查询的分类类别
     * @param classifyListener 查询监听
     */
    void query(int type, ClassifyBiz.OnQueryClassifyListener classifyListener);

    interface OnQueryClassifyListener {
        void querySuccess(List<Classify> classifies);

        void queryFailed();
    }
}
