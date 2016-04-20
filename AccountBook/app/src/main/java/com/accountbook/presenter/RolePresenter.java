package com.accountbook.presenter;

import android.content.Context;

import com.accountbook.biz.api.IRoleBiz;
import com.accountbook.biz.impl.RoleBiz;
import com.accountbook.entity.local.Role;
import com.accountbook.tools.QuickSimpleIO;
import com.accountbook.view.api.IRoleView;

import java.util.List;

/**
 * 主页数据查询的Presenter层交互类，负责HomeFragment与HomeLoadDataBiz的交互
 */
public class RolePresenter {
    private IRoleView mRoleView;
    private IRoleBiz mRoleBiz;
    private QuickSimpleIO mSimpleIO;

    public RolePresenter(IRoleView iRoleView) {
        this.mRoleView = iRoleView;
        this.mRoleBiz = new RoleBiz();
        this.mSimpleIO = new QuickSimpleIO((Context) iRoleView, "version_sp");
    }

    /**
     * 查询到结果之后对UI做具体的操作
     */
    public void queryRole() {
        mRoleBiz.queryRole(new RoleBiz.OnQueryRoleListener() {
            @Override
            public void querySuccess(List<Role> roles) {
                mRoleView.loadRole(roles);
            }

            @Override
            public void queryFailed() {
                mRoleView.loadFailed();
            }
        });
    }

    public void deleteRole(final String id) {
        mRoleBiz.delete(id, new RoleBiz.OnDeleteListener() {
            @Override
            public void deleteSuccess() {
                mSimpleIO.setBoolean("isSave", false);
                mSimpleIO.setLong("roleVer", mSimpleIO.getLong("roleVer") + 1);
                mRoleView.deleteSuccess();
            }

            @Override
            public void deleteFailed() {
                mRoleView.deleteFailed();
            }
        });
    }

    public void saveRole(final Role role) {
        mRoleBiz.saveRole(role, new RoleBiz.OnRoleSaveListener() {
            @Override
            public void saveSuccess() {
                mSimpleIO.setBoolean("isSave", false);
                mSimpleIO.setLong("roleVer", mSimpleIO.getLong("roleVer") + 1);
                mRoleView.saveSuccess();
            }

            @Override
            public void saveFailed() {
                mRoleView.saveFailed();
            }
        });
    }
}
