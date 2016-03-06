package com.accountbook.entity;

/**
 * 主页所使用的数据实体类
 */
public class AccountBill {
    private String category;
    private String money;
    private int moneyType;
    private String accountType;
    private int color;
    private int iconResId;

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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AccountBill{");
        sb.append(", category='").append(category).append('\'');
        sb.append(", money='").append(money).append('\'');
        sb.append(", moneyType=").append(moneyType);
        sb.append(", accountType='").append(accountType).append('\'');
        sb.append(", color=").append(color);
        sb.append(", iconResId=").append(iconResId);
        sb.append('}');
        return sb.toString();
    }
}
