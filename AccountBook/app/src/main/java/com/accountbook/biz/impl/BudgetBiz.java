package com.accountbook.biz.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.accountbook.biz.api.IBudgetBiz;
import com.accountbook.entity.AccountBill;
import com.accountbook.entity.Budget;
import com.accountbook.tools.ConstantContainer;

import java.util.ArrayList;
import java.util.List;

public class BudgetBiz implements IBudgetBiz {

    public interface OnQueryBudgetListener {
        void querySuccess(List<Budget> budgets);

        void queryFailed();
    }

    public interface OnDeleteBudgetListener {
        void deleteSuccess();

        void deleteFailed();
    }

    public interface OnRecoveryBudgetListener {
        void recoverySuccess();

        void recoveryFailed();
    }

    private SQLiteDatabase mDatabase;

    public BudgetBiz() {
        mDatabase = SQLite.getInstance().getDatabaseObject();
    }

    @Override
    public void queryBudget(OnQueryBudgetListener queryListener) {
        List<Budget> budgets = new ArrayList<>();
        String sql = "SELECT b._id,b.classify_id,c.classify,b.money,c.color,b.start_date,b.end_date" +
                " FROM budget b inner join classify c\n" +
                "on b.classify_id = c.\"_id\"\n" +
                "and b.available=?";

        Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.TRUE + ""});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Budget budget = new Budget();
                budget.setCountMoney(cursor.getInt(cursor.getColumnIndex("money")));
                budget.setClassify(cursor.getString(cursor.getColumnIndex("classify")));
                budget.setId(cursor.getString(cursor.getColumnIndex("_id")));
                budget.setStartTime(cursor.getLong(cursor.getColumnIndex("start_date")));
                budget.setEndTime(cursor.getLong(cursor.getColumnIndex("end_date")));
                budget.setColor(cursor.getString(cursor.getColumnIndex("color")));
                budget.setCurrMoney(getCurrMoney(cursor.getString(cursor.getColumnIndex("classify_id"))));

                budgets.add(budget);
            }

            cursor.close();

            if (queryListener != null) {
                queryListener.querySuccess(budgets);
            }
        } else {
            if (queryListener != null) {
                queryListener.queryFailed();
            }
        }
    }

    private int getCurrMoney(String classify_id) {
        String sql = "select sum(money) sum from record " +
                "where available = ? and " +
                "classify_id =?";

        Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.TRUE + "", classify_id});

        int money = 0;

        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            money = cursor.getInt(cursor.getColumnIndex("sum"));
        }

        cursor.close();

        return money;
    }

    @Override
    public void delete(String id, OnDeleteBudgetListener deleteBudgetListener) {
        ContentValues values = new ContentValues();
        values.put("available", ConstantContainer.FALSE);

        int successfulNum = mDatabase.update(SQLite.BUDGET_TABLE, values, "_id = ?", new String[]{id});
        values.clear();

        if (successfulNum > 0) {
            if (deleteBudgetListener != null) {
                deleteBudgetListener.deleteSuccess();
            }
        } else {
            if (deleteBudgetListener != null) {
                deleteBudgetListener.deleteFailed();
            }
        }
    }

    @Override
    public void recovery(String id, OnRecoveryBudgetListener recoveryBudgetListener) {
        ContentValues values = new ContentValues();
        values.put("available", ConstantContainer.TRUE);

        int successfulNum = mDatabase.update(SQLite.BUDGET_TABLE, values, "_id = ?", new String[]{id});
        values.clear();

        if (successfulNum > 0) {
            if (recoveryBudgetListener != null) {
                recoveryBudgetListener.recoverySuccess();
            }
        } else {
            if (recoveryBudgetListener != null) {
                recoveryBudgetListener.recoveryFailed();
            }
        }
    }
}
