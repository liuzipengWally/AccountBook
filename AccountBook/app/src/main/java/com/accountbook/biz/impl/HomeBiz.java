package com.accountbook.biz.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.accountbook.biz.api.IHomeBiz;
import com.accountbook.entity.local.AccountBill;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.Util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责查询主页所要用到的数据，属于业务逻辑层(biz)
 */
public class HomeBiz implements IHomeBiz {
    private SQLiteDatabase mDatabase;

    public HomeBiz() {
        mDatabase = SQLite.getInstance().getDatabaseObject();
    }

    public interface OnQueryAccountBillsListener {
        void querySuccess(List<AccountBill> accountBills);

        void queryFailed();
    }

    public interface OnQueryInfoCardDataListener {
        void querySuccess(String expend, String income, String balance);

        void queryFailed();
    }

    public interface OnDeleteAccountBillsListener {
        void deleteSuccess();

        void deleteFailed();
    }

    public interface OnRecoveryAccountBillsListener {
        void recoverySuccess();

        void recoveryFailed();
    }

    @Override
    public void queryAccountBills(long startTime, long endTime, OnQueryAccountBillsListener queryListener) {
        List<AccountBill> accountBills = new ArrayList<>();
        String create_time = "";

        String sql = "SELECT r._id,c.classify,r.money,c.type,r.account,c.color,c.iconResId,r.record_ms FROM record r inner join classify c\n" +
                "on r.classify_id = c.\"_id\"\n" +
                "where r.record_ms>=? and r.record_ms<=?\n" +
                "and r.available=?\n" +
                "ORDER BY r.record_ms DESC";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{startTime + "", endTime + "", ConstantContainer.TRUE + ""});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                long ms = 0;
                try {
                    ms = Util.parseMsNotCh(cursor.getLong(cursor.getColumnIndex("record_ms")) + "");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String create_time_curr = Util.formatDateUseCh(ms);
                AccountBill bill = new AccountBill();

                bill.setId(cursor.getString(cursor.getColumnIndex("_id")));
                bill.setClassify(cursor.getString(cursor.getColumnIndex("classify")));
                bill.setMoney(cursor.getInt(cursor.getColumnIndex("money")));
                bill.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                bill.setColor(cursor.getString(cursor.getColumnIndex("color")));
                bill.setIconResId(cursor.getInt(cursor.getColumnIndex("iconResId")));
                bill.setType(cursor.getInt(cursor.getColumnIndex("type")));

                if (!create_time_curr.equals(create_time)) {
                    create_time = create_time_curr;
                    bill.setCreate_time(create_time);
                    bill.setMoneyCount(countMoney(create_time));
                }

                accountBills.add(bill);
            }

            cursor.close();
            if (queryListener != null) {
                queryListener.querySuccess(accountBills);
            }
        } else {
            if (queryListener != null) {
                queryListener.queryFailed();
            }
        }
    }


    /**
     * 获取每一天所使用的钱的统计
     *
     * @param recordDate 记录的日期
     * @return
     */
    private String countMoney(String recordDate) {
        String sql = "select money,record_ms from record " +
                "where available = ?"; //先把钱和时间全部查询出来
        int money = 0;

        Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.TRUE + ""});

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                long ms = 0;
                try {
                    ms = Util.parseMsNotCh(cursor.getLong(cursor.getColumnIndex("record_ms")) + "");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String time = Util.formatDateUseCh(ms);
                if (time.equals(recordDate)) { //如果格式化后的日期相同，就累加money
                    money += cursor.getInt(cursor.getColumnIndex("money"));
                }
            }
        }

        //金额大于0加+号
        if (money > 0) {
            return "+" + money;
        } else if (money < 0) {
            return money + "";
        } else {
            return 0 + "";
        }
    }

    @Override
    public void queryInfoCardData(OnQueryInfoCardDataListener listener) {

    }

    @Override
    public void delete(String id, OnDeleteAccountBillsListener deleteAccountBillsListener) {
        ContentValues values = new ContentValues();
        values.put("available", ConstantContainer.FALSE);
        values.put("isSave", ConstantContainer.FALSE);
        values.put("update_ms", System.currentTimeMillis());

        int successfulNum = mDatabase.update(SQLite.RECORD_TABLE, values, "_id = ?", new String[]{id});
        values.clear();

        if (successfulNum > 0) {
            if (deleteAccountBillsListener != null) {
                deleteAccountBillsListener.deleteSuccess();
            }
        } else {
            if (deleteAccountBillsListener != null) {
                deleteAccountBillsListener.deleteFailed();
            }
        }
    }

    @Override
    public void recovery(String id, OnRecoveryAccountBillsListener recoveryAccountBillsListener) {
        ContentValues values = new ContentValues();
        values.put("available", ConstantContainer.TRUE);

        int successfulNum = mDatabase.update(SQLite.RECORD_TABLE, values, "_id = ?", new String[]{id});
        values.clear();

        if (successfulNum > 0) {
            if (recoveryAccountBillsListener != null) {
                recoveryAccountBillsListener.recoverySuccess();
            }
        } else {
            if (recoveryAccountBillsListener != null) {
                recoveryAccountBillsListener.recoveryFailed();
            }
        }
    }
}
