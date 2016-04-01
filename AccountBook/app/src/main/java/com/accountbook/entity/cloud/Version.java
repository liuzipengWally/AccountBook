package com.accountbook.entity.cloud;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("Version")
public class Version extends AVObject {
    public int getRecordVer() {
        return getInt("recordVer");
    }

    public void setRecordVer(int ver) {
        put("recordVer", ver);
    }

    public int getBudgetVer() {
        return getInt("budgetVer");
    }

    public void setBudgetVer(int ver) {
        put("budgetVer", ver);
    }

    public int getRoleVer() {
        return getInt("roleVer");
    }

    public void setRoleVer(int ver) {
        put("roleVer", ver);
    }

    public int getClassifyVer() {
        return getInt("classifyVer");
    }

    public void setClassifyVer(int ver) {
        put("classifyVer", ver);
    }
}
