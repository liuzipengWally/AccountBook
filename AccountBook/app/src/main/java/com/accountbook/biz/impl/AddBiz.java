package com.accountbook.biz.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.accountbook.biz.api.IAddBiz;
import com.accountbook.entity.Role;
import com.accountbook.tools.ConstantContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzipeng on 16/3/8.
 */
public class AddBiz implements IAddBiz {
    private SQLiteDatabase mDatabase;
    private List<Role> mRoleList;

    //该查询的监听器
    public interface OnQueryRoleListener {
        void querySuccess(String[] roles);

        void queryFailed();
    }

    //构造方法中获取db对象
    public AddBiz() {
        this.mDatabase = SQLite.getInstance().getDatabaseObject();
    }

    @Override
    public void queryRole(OnQueryRoleListener mQueryRoleListener) {
        //查询出所有的role数据  available = ConstantContainer.TRUE 即为可用的
        Cursor cursor = mDatabase.query(SQLite.ROLE_TABLE, new String[]{"_id", "role"}, "available=?", new String[]{ConstantContainer.TRUE + ""}, null, null, null);
        mRoleList = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Role role = new Role();
                role.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                role.setRole(cursor.getString(cursor.getColumnIndex("role")));
                mRoleList.add(role);
            }

            String[] roles = new String[mRoleList.size()];

            for (int i = 0; i < mRoleList.size(); i++) {
                roles[i] = mRoleList.get(i).getRole();
            }

            //数据查询到之后通过回调监听把 数据回调出去给presenter
            if (mQueryRoleListener != null) {
                mQueryRoleListener.querySuccess(roles);
            }
        } else {
            //数据查询失败也要通过回调告诉presenter
            if (mQueryRoleListener != null) {
                mQueryRoleListener.queryFailed();
            }
        }
    }
}
