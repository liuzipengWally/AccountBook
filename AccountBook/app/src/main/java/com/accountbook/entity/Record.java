package com.accountbook.entity;

/**
 * Created by liuzipeng on 16/3/14.
 */
public class Record {
    private String id;
    private int money;
    private String description;
    private String borrowName;
    private String classifyId;
    private String account;
    private String roleId;
    private long recordMs;
    private String classify;
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public long getRecordMs() {
        return recordMs;
    }

    public void setRecordMs(long recordMs) {
        this.recordMs = recordMs;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Record{");
        sb.append("id='").append(id).append('\'');
        sb.append(", money=").append(money);
        sb.append(", description='").append(description).append('\'');
        sb.append(", borrowName='").append(borrowName).append('\'');
        sb.append(", classifyId='").append(classifyId).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", roleId='").append(roleId).append('\'');
        sb.append(", recordMs=").append(recordMs);
        sb.append(", classify='").append(classify).append('\'');
        sb.append(", role='").append(role).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
