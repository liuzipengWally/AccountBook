package com.accountbook.biz.api;

import com.accountbook.biz.impl.ClassifyBiz;

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

    /**
     * 删除分类的方法
     *
     * @param id                     要删除的那个数据的id
     * @param deleteClassifyListener 删除监听
     */
    void delete(String id, ClassifyBiz.OnDeleteClassifyListener deleteClassifyListener);

    /**
     * 恢复删除的数据
     *
     * @param id                       要恢复的数据的id
     * @param recoveryClassifyListener 恢复数据的监听
     */
    void recovery(String id, ClassifyBiz.OnRecoveryClassifyListener recoveryClassifyListener);
}
