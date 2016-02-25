package com.accountbook.entity;

/**
 * 主页所使用的数据实体类
 */
public class HomeItem {
    private int time;
    private String category;
    private String money;
    private int moneyType;
    private String accountType;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(int moneyType) {
        this.moneyType = moneyType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("HomeItem{");
        sb.append("time='").append(time).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", money='").append(money).append('\'');
        sb.append(", moneyType=").append(moneyType);
        sb.append(", accountType='").append(accountType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
