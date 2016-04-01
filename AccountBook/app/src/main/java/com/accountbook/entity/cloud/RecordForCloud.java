package com.accountbook.entity.cloud;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

@AVClassName("Record")
public class RecordForCloud extends AVObject {
    public String getId() {
        return getString("recordId");
    }

    public void setId(String id) {
        put("recordId", id);
    }

    public int getMoney() {
        return getInt("money");
    }

    public void setMoney(int money) {
        put("money", money);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getBorrowName() {
        return getString("borrow_name");
    }

    public void setBorrowName(String borrowName) {
        put("borrow_name", borrowName);
    }

    public String getClassifyId() {
        return getString("classify_id");
    }

    public void setClassifyId(String classifyId) {
        put("classify_id", classifyId);
    }

    public String getAccount() {
        return getString("account");
    }

    public void setAccount(String account) {
        put("account", account);
    }

    public String getRoleId() {
        return getString("role_id");
    }

    public void setRoleId(String roleId) {
        put("role_id", roleId);
    }

    public long getRecordMs() {
        return getLong("record_ms");
    }

    public void setRecordMs(long recordMs) {
        put("record_ms", recordMs);
    }

    public int getAvailable() {
        return getInt("available");
    }

    public void setAvailable(int available) {
        put("available", available);
    }

    public long getUpdateMs() {
        return getLong("update_ms");
    }

    public void setUpdateMs(long updateMs) {
        put("update_ms", updateMs);
    }

    public void setUser(AVUser user) {
        put("user", user);
    }

    public AVUser getUser() {
        return getAVUser("user");
    }

}
