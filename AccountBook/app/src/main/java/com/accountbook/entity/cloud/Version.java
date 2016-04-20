package com.accountbook.entity.cloud;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("Version")
public class Version extends AVObject {
    public long getRecordVer() {
        return getLong("recordVer");
    }

    public void setRecordVer(long ver) {
        put("recordVer", ver);
    }

    public long getBudgetVer() {
        return getLong("budgetVer");
    }

    public void setBudgetVer(long ver) {
        put("budgetVer", ver);
    }

    public long getRoleVer() {
        return getLong("roleVer");
    }

    public void setRoleVer(long ver) {
        put("roleVer", ver);
    }

    public long getClassifyVer() {
        return getLong("classifyVer");
    }

    public void setClassifyVer(long ver) {
        put("classifyVer", ver);
    }
}
