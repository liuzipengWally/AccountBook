package com.accountbook.entity.local;

/**
 * 主页所使用的数据实体类
 */
public class AccountBill {
    private String id;
    private String classify;
    private int money;
    private int type;
    private String account;
    private String color;
    private int iconResId;
    private String create_time = "";
    private String moneyCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getMoneyCount() {
        return moneyCount;
    }

    public void setMoneyCount(String moneyCount) {
        this.moneyCount = moneyCount;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AccountBill{");
        sb.append("id='").append(id).append('\'');
        sb.append(", classify='").append(classify).append('\'');
        sb.append(", money=").append(money);
        sb.append(", type=").append(type);
        sb.append(", account='").append(account).append('\'');
        sb.append(", color='").append(color).append('\'');
        sb.append(", iconResId=").append(iconResId);
        sb.append(", create_time='").append(create_time).append('\'');
        sb.append(", moneyCount='").append(moneyCount).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
