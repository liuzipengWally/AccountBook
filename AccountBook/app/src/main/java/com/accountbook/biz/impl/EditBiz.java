package com.accountbook.biz.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.accountbook.biz.api.IEditBiz;
import com.accountbook.entity.local.Record;
import com.accountbook.entity.local.Role;
import com.accountbook.tools.ConstantContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditBiz implements IEditBiz {
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

    //载入要修改的记录的监听
    public interface OnQueryRecordListener {
        void querySuccess(Record recordAdds);

        void queryFailed();
    }

    public interface OnAlterRecordListener {
        void alterSuccess();

        void alterFailed();
    }

    //构造方法中获取db对象
    public EditBiz() {
        SQLite.getInstance().createDataBase("accountBook.db");
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
                role.setId(cursor.getString(cursor.getColumnIndex("_id")));
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
    public void saveRecord(Record record, OnRecordSaveListener onRecordSaveListener) {
        ContentValues values = new ContentValues();

        values.put("_id", UUID.randomUUID().toString());
        values.put("money", record.getMoney());
        values.put("description", record.getDescription());
        values.put("borrow_name", record.getBorrowName());
        values.put("classify_id", record.getClassifyId());
        values.put("account", record.getAccount());
        values.put("role_id", record.getRoleId());
        values.put("isSave", ConstantContainer.FALSE);
        values.put("available", ConstantContainer.TRUE);
        values.put("record_ms", record.getRecordMs());
        values.put("update_ms", System.currentTimeMillis());

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

    @Override
    public void queryRecord(String id, OnQueryRecordListener onQueryRecordListener) {
        String sql = "SELECT r.money,r.description,r.borrow_name,r.classify_id,r.role_id,r.account,r.record_ms,c.type,c.classify,e.role " +
                "FROM role e inner join record r inner join classify c\n" +
                "on r.classify_id=c._id and e._id = r.role_id\n" +
                "where r.available=?\n" +
                "and r._id =?";

        Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.TRUE + "", id});
        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            Record record = new Record();
            record.setClassifyId(cursor.getString(cursor.getColumnIndex("classify_id")));
            record.setRoleId(cursor.getString(cursor.getColumnIndex("role_id")));
            record.setAccount(cursor.getString(cursor.getColumnIndex("account")));
            record.setBorrowName(cursor.getString(cursor.getColumnIndex("borrow_name")));
            record.setClassify(cursor.getString(cursor.getColumnIndex("classify")));
            record.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            record.setMoney(cursor.getInt(cursor.getColumnIndex("money")));
            record.setRecordMs(cursor.getLong(cursor.getColumnIndex("record_ms")));
            record.setRole(cursor.getString(cursor.getColumnIndex("role")));

            if (onQueryRecordListener != null) {
                onQueryRecordListener.querySuccess(record);
            }
        } else {
            if (onQueryRecordListener != null) {
                onQueryRecordListener.queryFailed();
            }
        }
    }

    @Override
    public void alterRecord(final Record record, final OnAlterRecordListener onAlterRecordListener) {
        ContentValues values = new ContentValues();

        values.put("money", record.getMoney());
        values.put("description", record.getDescription());
        values.put("borrow_name", record.getBorrowName());
        values.put("classify_id", record.getClassifyId());
        values.put("account", record.getAccount());
        values.put("role_id", record.getRoleId());
        values.put("isSave", ConstantContainer.FALSE);
        values.put("record_ms", record.getRecordMs());
        values.put("update_ms", System.currentTimeMillis());

        int successfulNum = mDatabase.update(SQLite.RECORD_TABLE, values, "_id= ?", new String[]{record.getId()});

        values.clear();

        if (successfulNum != 0) {
            if (onAlterRecordListener != null) {
                onAlterRecordListener.alterSuccess();
            }
        } else {
            if (onAlterRecordListener != null) {
                onAlterRecordListener.alterFailed();
            }
        }
    }
}
