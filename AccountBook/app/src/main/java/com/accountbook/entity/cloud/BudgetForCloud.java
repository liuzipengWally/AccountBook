package com.accountbook.entity.cloud;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

@AVClassName("Budget")
public class BudgetForCloud extends AVObject {
    public String getId() {
        return getString("budgetId");
    }

    public void setId(String id) {
        put("budgetId", id);
    }

    public String getClassifyId() {
        return getString("classify_id");
    }

    public void setClassifyId(String classifyId) {
        put("classify_id", classifyId);
    }

    public int getMoney() {
        return getInt("money");
    }

    public void setMoney(int money) {
        put("money", money);
    }

    public long getStartTime() {
        return getLong("startDate");
    }

    public void setStartTime(long startTime) {
        put("startDate", startTime);
    }

    public long getEndTime() {
        return getLong("endDate");
    }

    public void setEndTime(long endTime) {
        put("endDate", endTime);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
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
