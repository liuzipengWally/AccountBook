package com.accountbook.biz.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.accountbook.biz.api.IEditBudgetBiz;
import com.accountbook.entity.local.Budget;
import com.accountbook.tools.ConstantContainer;

import java.util.UUID;

public class EditBudgetBiz implements IEditBudgetBiz {
    private SQLiteDatabase mDatabase;

    public EditBudgetBiz() {
        SQLite.getInstance().createDataBase("accountBook.db");
        this.mDatabase = SQLite.getInstance().getDatabaseObject();
    }

    //保存记录的监听器
    public interface OnBudgetSaveListener {
        void saveSuccess();

        void saveFailed();
    }

    //载入要修改的记录的监听
    public interface OnQueryBudgetListener {
        void querySuccess(Budget budget);

        void queryFailed();
    }

    public interface OnAlterBudgetListener {
        void alterSuccess();

        void alterFailed();
    }

    @Override
    public void saveBudget(Budget budget, OnBudgetSaveListener onBudgetSaveListener) {
        ContentValues values = new ContentValues();

        values.put("_id", UUID.randomUUID().toString());
        values.put("money", budget.getCountMoney());
        values.put("description", budget.getDescription());
        values.put("classify_id", budget.getClassifyId());
        values.put("isSave", ConstantContainer.FALSE);
        values.put("available", ConstantContainer.TRUE);
        values.put("start_date", budget.getStartTime());
        values.put("end_date", budget.getEndTime());
        values.put("update_ms", System.currentTimeMillis());

        long successfulNum = mDatabase.insert(SQLite.BUDGET_TABLE, null, values);
        values.clear();

        if (successfulNum != 0) {
            if (onBudgetSaveListener != null) {
                onBudgetSaveListener.saveSuccess();
            }
        } else {
            if (onBudgetSaveListener != null) {
                onBudgetSaveListener.saveFailed();
            }
        }
    }

    @Override
    public void queryBudget(String id, OnQueryBudgetListener onQueryBudgetListener) {
        String sql = "SELECT b.money,b.description,b.classify_id,b.start_date,b.end_date,c.classify " +
                "FROM budget b inner join classify c\n" +
                "on b.classify_id=c._id\n" +
                "where b.available=?\n" +
                "and b._id =?";

        Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.TRUE + "", id});
        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            Budget budget = new Budget();
            budget.setClassifyId(cursor.getString(cursor.getColumnIndex("classify_id")));
            budget.setClassify(cursor.getString(cursor.getColumnIndex("classify")));
            budget.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            budget.setCountMoney(cursor.getInt(cursor.getColumnIndex("money")));
            budget.setStartTime(cursor.getLong(cursor.getColumnIndex("start_date")));
            budget.setEndTime(cursor.getLong(cursor.getColumnIndex("end_date")));

            if (onQueryBudgetListener != null) {
                onQueryBudgetListener.querySuccess(budget);
            }
        } else {
            if (onQueryBudgetListener != null) {
                onQueryBudgetListener.queryFailed();
            }
        }
    }

    @Override
    public void alterBudget(Budget budget, OnAlterBudgetListener onAlterBudgetListener) {
        ContentValues values = new ContentValues();

        values.put("money", budget.getCountMoney());
        values.put("description", budget.getDescription());
        values.put("classify_id", budget.getClassifyId());
        values.put("isSave", ConstantContainer.FALSE);
        values.put("start_date", budget.getStartTime());
        values.put("end_date", budget.getEndTime());
        values.put("update_ms", System.currentTimeMillis());

        int successfulNum = mDatabase.update(SQLite.BUDGET_TABLE, values, "_id= ?", new String[]{budget.getId()});

        values.clear();

        if (successfulNum != 0) {
            if (onAlterBudgetListener != null) {
                onAlterBudgetListener.alterSuccess();
            }
        } else {
            if (onAlterBudgetListener != null) {
                onAlterBudgetListener.alterFailed();
            }
        }
    }
}
