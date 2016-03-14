package com.accountbook.entity;

/**
 * Created by liuzipeng on 16/3/14.
 */
public class RecordAdd {
    private int money;
    private String description;
    private String borrowName;
    private String classifyId;
    private String account;
    private String roleId;
    private String createTime;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RecordAdd{");
        sb.append("money=").append(money);
        sb.append(", description='").append(description).append('\'');
        sb.append(", borrowName='").append(borrowName).append('\'');
        sb.append(", classifyId='").append(classifyId).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", roleId='").append(roleId).append('\'');
        sb.append(", createTime='").append(createTime).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
