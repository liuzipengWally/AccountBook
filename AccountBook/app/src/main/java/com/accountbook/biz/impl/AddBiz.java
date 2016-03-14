package com.accountbook.biz.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.accountbook.biz.api.IAddBiz;
import com.accountbook.entity.RecordAdd;
import com.accountbook.entity.Role;
import com.accountbook.tools.ConstantContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by liuzipeng on 16/3/8.
 */
public class AddBiz implements IAddBiz {
    private SQLiteDatabase mDatabase;

    //查询role数据的监听器
    public interface OnQueryRoleListener {
        void querySuccess(List<Role> roles);

        void queryFailed();
    }

    //保存记录的监听器
    public interface OnRecordSaveListener {
        void saveSuccess();

        void saveFailed();
    }

    //构造方法中获取db对象
    public AddBiz() {
        this.mDatabase = SQLite.getInstance().getDatabaseObject();
    }

    @Override
    public void queryRole(OnQueryRoleListener mQueryRoleListener) {
        //查询出所有的role数据  available = ConstantContainer.TRUE 即为可用的
        Cursor cursor = mDatabase.query(SQLite.ROLE_TABLE, new String[]{"_id", "role"}, "available=?", new String[]{ConstantContainer.TRUE + ""}, null, null, null);
        List<Role> mRoleList = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Role role = new Role();
                role.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                role.setRole(cursor.getString(cursor.getColumnIndex("role")));
                mRoleList.add(role);
            }

            //数据查询到之后通过回调监听把 数据回调出去给presenter
            if (mQueryRoleListener != null) {
                mQueryRoleListener.querySuccess(mRoleList);
            }
        } else {
            //数据查询失败也要通过回调告诉presenter
            if (mQueryRoleListener != null) {
                mQueryRoleListener.queryFailed();
            }
        }
    }

    @Override
    public void saveRecord(RecordAdd record, OnRecordSaveListener onRecordSaveListener) {
        ContentValues values = new ContentValues();

        values.put("_id", UUID.randomUUID().toString());
        values.put("money", record.getMoney());
        values.put("description", record.getDescription());
        values.put("borrow_name", record.getBorrowName());
        values.put("classify_id", record.getClassifyId());
        values.put("account", record.getAccount());
        values.put("role_id", record.getRoleId());
        values.put("create_time", record.getCreateTime());
        values.put("isSave", ConstantContainer.FALSE);
        values.put("available", ConstantContainer.TRUE);
        values.put("timestamp", System.currentTimeMillis());

        long successfulNum = mDatabase.insert(SQLite.RECORD_TABLE, null, values);
        values.clear();

        if (successfulNum > 0) {
            if (onRecordSaveListener != null) {
                onRecordSaveListener.saveSuccess();
            }
        } else {
            if (onRecordSaveListener != null) {
                onRecordSaveListener.saveFailed();
            }
        }
    }
}
