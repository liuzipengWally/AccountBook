package com.accountbook.biz.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.accountbook.biz.api.IAddBiz;
import com.accountbook.entity.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzipeng on 16/3/8.
 */
public class AddBiz implements IAddBiz {
    private SQLiteDatabase mDatabase;
    private List<Role> mRoleList;

    public interface OnQueryRoleListener {
        void querySuccess(String[] roles);

        void queryFailed();
    }

    public AddBiz() {
        this.mDatabase = SQLite.getInstance().getDatabaseObject();
    }

    @Override
    public void queryRole(OnQueryRoleListener mQueryRoleListener) {
        Cursor cursor = mDatabase.query(SQLite.ROLE_TABLE, new String[]{"_id", "role"}, "_id<>?", new String[]{"0"}, null, null, null);
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

            if (mQueryRoleListener != null) {
                mQueryRoleListener.querySuccess(roles);
            }
        } else {
            if (mQueryRoleListener != null) {
                mQueryRoleListener.queryFailed();
            }
        }
    }
}
