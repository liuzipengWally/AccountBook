package com.accountbook.biz.impl;

import android.graphics.Color;

import com.accountbook.R;
import com.accountbook.biz.api.IHomeLoadDataBiz;
import com.accountbook.biz.api.OnHomeQueryDataListener;
import com.accountbook.entity.AccountBill;
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
        List<AccountBill> accountBills = getHomeItemData();

        if (income != null && expend != null && balance != null && accountBills != null) {
            queryListener.querySuccess(accountBills, income, expend, balance);
        } else {
            queryListener.queryFailed();
        }
    }

    /**
     * 查询本周记录
     *
     * @return
     */
    private List<AccountBill> getHomeItemData() {
        List<AccountBill> accountBills = new ArrayList<>();

        AccountBill item1 = new AccountBill();
        item1.setCategory("衣服鞋子");
        item1.setAccountType("银行卡");
        item1.setMoney("-300");
        item1.setIconResId(R.mipmap.ic_invert_colors_white_24dp);
        item1.setMoneyType(ConstantContainer.EXPEND);
        item1.setColor(Color.parseColor("#e91e63"));
        accountBills.add(item1);

        AccountBill item2 = new AccountBill();
        item2.setCategory("早餐晚餐");
        item2.setAccountType("现金");
        item2.setMoney("-20");
        item2.setIconResId(R.mipmap.ic_local_restaurant_white_24dp);
        item2.setMoneyType(ConstantContainer.EXPEND);
        item2.setColor(Color.parseColor("#00bcd4"));
        accountBills.add(item2);

        AccountBill item3 = new AccountBill();
        item3.setCategory("工资");
        item3.setAccountType("银行卡");
        item3.setMoney("+5000");
        item3.setIconResId(R.mipmap.ic_attach_money_white_24dp);
        item3.setMoneyType(ConstantContainer.INCOME);
        item3.setColor(Color.parseColor("#8bc34a"));
        accountBills.add(item3);

        AccountBill item4 = new AccountBill();
        item4.setCategory("借出");
        item4.setAccountType("银行卡");
        item4.setMoney("2000");
        item4.setIconResId(R.mipmap.ic_thumbs_up_down_white_24dp);
        item4.setMoneyType(ConstantContainer.LEND);
        item4.setColor(Color.parseColor("#FF9800"));
        accountBills.add(item4);

        AccountBill item5 = new AccountBill();
        item5.setCategory("借入");
        item5.setAccountType("银行卡");
        item5.setMoney("2000");
        item5.setIconResId(R.mipmap.ic_thumbs_up_down_white_24dp);
        item5.setMoneyType(ConstantContainer.BORROW);
        item5.setColor(Color.parseColor("#ff5722"));
        accountBills.add(item5);

        AccountBill item6 = new AccountBill();
        item6.setCategory("借入");
        item6.setAccountType("银行卡");
        item6.setMoney("2000");
        item6.setIconResId(R.mipmap.ic_thumbs_up_down_white_24dp);
        item6.setColor(Color.parseColor("#ff5722"));
        item6.setMoneyType(ConstantContainer.BORROW);
        accountBills.add(item6);

        AccountBill item7 = new AccountBill();
        item7.setCategory("借入");
        item7.setAccountType("银行卡");
        item7.setMoney("2000");
        item7.setIconResId(R.mipmap.ic_thumbs_up_down_white_24dp);
        item7.setColor(Color.parseColor("#ff5722"));
        item7.setMoneyType(ConstantContainer.BORROW);
        accountBills.add(item7);

        AccountBill item8 = new AccountBill();
        item8.setCategory("借入");
        item8.setAccountType("银行卡");
        item8.setMoney("2000");
        item8.setIconResId(R.mipmap.ic_thumbs_up_down_white_24dp);
        item8.setColor(Color.parseColor("#ff5722"));
        item8.setMoneyType(ConstantContainer.BORROW);
        accountBills.add(item8);

        return accountBills;
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
