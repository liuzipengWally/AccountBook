package com.accountbook.view.api;

import com.accountbook.entity.AccountBill;

import java.util.List;

/**
 * Created by liuzipeng on 16/2/23.
 */
public interface IHomeView {
    /**
     * 显示查询错误信息
     */
    void showLoadDataError();

    /**
     * 刷新数据
     */
    void refresh();

    /**
     * 在页面中载入数据
     *
     * @param accountBills 包含多个homeItem实体的集合
     * @param income    当月总收入
     * @param expend    当月总支出
     * @param balance   单月预算余额
     */
    void LoadData(List<AccountBill> accountBills, String income, String expend, String balance);

    /**
     * 跳转到详情页
     *
     * @param id        所选数据的id
     * @param moneyType 所选数据的金钱类别，有支出、收入、借出、借入
     */
    void toDetailedActivity(int id, int moneyType);

    /**
     * 刷新完成
     */
    void refreshDone();
}
