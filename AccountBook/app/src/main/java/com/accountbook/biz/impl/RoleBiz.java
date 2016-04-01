package com.accountbook.biz.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.accountbook.biz.api.IRoleBiz;
import com.accountbook.entity.local.Role;
import com.accountbook.tools.ConstantContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoleBiz implements IRoleBiz {
    private SQLiteDatabase mDatabase;

    //查询role数据的监听器
    public interface OnQueryRoleListener {
        void querySuccess(List<Role> roles);

        void queryFailed();
    }

    //保存role的监听器
    public interface OnRoleSaveListener {
        void saveSuccess();

        void saveFailed();
    }

    //载入要修改的记录的监听
    public interface OnDeleteListener {
        void deleteSuccess();

        void deleteFailed();
    }

    //构造方法中获取db对象
    public RoleBiz() {
        SQLite.getInstance().createDataBase("accountBook.db");
        this.mDatabase = SQLite.getInstance().getDatabaseObject();
    }


    @Override
    public void queryRole(OnQueryRoleListener onQueryRoleListener) {
        //查询出所有的role数据  available = ConstantContainer.TRUE 即为可用的
        Cursor cursor = mDatabase.query(SQLite.ROLE_TABLE, new String[]{"_id", "role"}, "available=?", new String[]{ConstantContainer.TRUE + ""}, null, null, null);
        List<Role> mRoleList = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Role role = new Role();
                role.setId(cursor.getString(cursor.getColumnIndex("_id")));
                role.setRole(cursor.getString(cursor.getColumnIndex("role")));
                mRoleList.add(role);
            }

            //数据查询到之后通过回调监听把 数据回调出去给presenter
            if (onQueryRoleListener != null) {
                onQueryRoleListener.querySuccess(mRoleList);
            }
        } else {
            //数据查询失败也要通过回调告诉presenter
            if (onQueryRoleListener != null) {
                onQueryRoleListener.queryFailed();
            }
        }
    }

    @Override
    public void delete(String id, OnDeleteListener onDeleteListener) {
        deleteRecord(id);

        ContentValues values = new ContentValues();
        values.put("available", ConstantContainer.FALSE);

        int successfulNum = mDatabase.update(SQLite.ROLE_TABLE, values, "_id = ?", new String[]{id});
        values.clear();

        if (successfulNum > 0) {
            if (onDeleteListener != null) {
                onDeleteListener.deleteSuccess();
            }
        } else {
            if (onDeleteListener != null) {
                onDeleteListener.deleteFailed();
            }
        }
    }

    private void deleteRecord(String id) {
        Cursor cursor = mDatabase.rawQuery("select role_id from record where role_id = ?", new String[]{id});
        if (cursor.getCount() != 0) {
            ContentValues values = new ContentValues();
            values.put("available", ConstantContainer.FALSE);
            int successNum = mDatabase.update(SQLite.RECORD_TABLE, values, "role_id = ?", new String[]{id});
            values.clear();
        }
    }

    @Override
    public void saveRole(Role role, OnRoleSaveListener onRoleSaveListener) {
        ContentValues values = new ContentValues();
        values.put("_id", UUID.randomUUID().toString());
        values.put("role", role.getRole());
        values.put("isSave", ConstantContainer.FALSE);
        values.put("available", ConstantContainer.TRUE);
        values.put("update_ms", System.currentTimeMillis());

        long successfulNum = mDatabase.insert(SQLite.ROLE_TABLE, null, values);
        values.clear();

        if (successfulNum != 0) {
            if (onRoleSaveListener != null) {
                onRoleSaveListener.saveSuccess();
            }
        } else {
            if (onRoleSaveListener != null) {
                onRoleSaveListener.saveFailed();
            }
        }
    }

}
