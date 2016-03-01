package com.accountbook.biz.impl;

import com.accountbook.biz.api.IHomeLoadDataBiz;
import com.accountbook.biz.api.OnHomeQueryDataListener;
import com.accountbook.entity.HomeItem;
import com.accountbook.tools.ConstantContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责查询主页所要用到的数据，属于业务逻辑层(biz)
 */
public class HomeLoadDataBiz implements IHomeLoadDataBiz {
    @Override
    public void query(OnHomeQueryDataListener queryListener) {
        String income = getIncomeTotal();
        String expend = getExpendTotal();
        String balance = getBalanceTotal();
        List<HomeItem> homeItems = getHomeItemData();

        if (income != null && expend != null && balance != null && homeItems != null) {
            queryListener.querySuccess(homeItems, income, expend, balance);
        } else {
            queryListener.queryFailed();
        }
    }

    /**
     * 查询本周记录
     *
     * @return
     */
    private List<HomeItem> getHomeItemData() {
        List<HomeItem> homeItems = new ArrayList<>();

        HomeItem item1 = new HomeItem();
        item1.setCategory("衣服鞋子");
        item1.setAccountType("银行卡");
        item1.setMoney("-300");
        item1.setTime(1);
        item1.setMoneyType(ConstantContainer.EXPEND);
        homeItems.add(item1);

        HomeItem item2 = new HomeItem();
        item2.setCategory("早餐晚餐");
        item2.setAccountType("现金");
        item2.setMoney("-20");
        item2.setTime(1);
        item2.setMoneyType(ConstantContainer.EXPEND);
        homeItems.add(item2);

        HomeItem item3 = new HomeItem();
        item3.setCategory("工资");
        item3.setAccountType("银行卡");
        item3.setMoney("+5000");
        item3.setTime(2);
        item3.setMoneyType(ConstantContainer.INCOME);
        homeItems.add(item3);

        HomeItem item4 = new HomeItem();
        item4.setCategory("借出");
        item4.setAccountType("银行卡");
        item4.setMoney("2000");
        item4.setTime(3);
        item4.setMoneyType(ConstantContainer.LEND);
        homeItems.add(item4);

        HomeItem item5 = new HomeItem();
        item5.setCategory("借入");
        item5.setAccountType("银行卡");
        item5.setMoney("2000");
        item5.setTime(4);
        item5.setMoneyType(ConstantContainer.BORROW);
        homeItems.add(item5);

        HomeItem item6 = new HomeItem();
        item6.setCategory("借入");
        item6.setAccountType("银行卡");
        item6.setMoney("2000");
        item6.setTime(5);
        item6.setMoneyType(ConstantContainer.BORROW);
        homeItems.add(item6);

        HomeItem item7 = new HomeItem();
        item7.setCategory("借入");
        item7.setAccountType("银行卡");
        item7.setMoney("2000");
        item7.setTime(3);
        item7.setMoneyType(ConstantContainer.BORROW);
        homeItems.add(item7);

        HomeItem item8 = new HomeItem();
        item8.setCategory("借入");
        item8.setAccountType("银行卡");
        item8.setMoney("2000");
        item8.setTime(3);
        item8.setMoneyType(ConstantContainer.BORROW);
        homeItems.add(item8);

        return homeItems;
    }

    /**
     * 查询本月总收入
     *
     * @return
     */
    private String getIncomeTotal() {
        return 4500 + "";
    }

    /**
     * 查询本月总支出
     *
     * @return
     */
    private String getExpendTotal() {
        return 2000 + "";
    }

    /**
     * 查询本月预算余额
     *
     * @return
     */
    private String getBalanceTotal() {
        return 500 + "";
    }

}
