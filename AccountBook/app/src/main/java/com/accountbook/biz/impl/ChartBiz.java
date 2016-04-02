package com.accountbook.biz.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.accountbook.biz.api.IChartBiz;
import com.accountbook.entity.local.ChartData;
import com.accountbook.tools.ConstantContainer;

import java.util.ArrayList;
import java.util.List;

public class ChartBiz implements IChartBiz {
    private SQLiteDatabase mDatabase;

    public ChartBiz() {
        SQLite.getInstance().createDataBase("accountBook.db");
        this.mDatabase = SQLite.getInstance().getDatabaseObject();
    }

    public interface OnQueryClassifyPercentListener {
        void querySuccess(List<ChartData> chartDatas);

        void queryFailed();
    }

    @Override
    public void queryClassifyPercent(int type, long startTime, long endTime, OnQueryClassifyPercentListener onQueryClassifyPercentListener) {
        String sql = "select c.classify,sum(r.money) sum,c.color  from record r inner join classify c " +
                "on r.classify_id = c._id " +
                "where r.available = ? and " +
                "r.record_ms>=? and r.record_ms<=? and " +
                "c.type =? " +
                "group by c.classify";

        float moneyCount = queryMoneyCount(type, startTime, endTime);

        List<ChartData> chartDataList = new ArrayList<>();

        Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.TRUE + "", startTime + "", endTime + "", type + ""});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ChartData chartData = new ChartData();
                chartData.setClassify(cursor.getString(cursor.getColumnIndex("classify")));
                chartData.setColor(cursor.getString(cursor.getColumnIndex("color")));
                float percent = (Math.abs(cursor.getFloat(cursor.getColumnIndex("sum"))) / moneyCount) * 100;
                chartData.setPercent(percent);

                chartDataList.add(chartData);
            }

            if (onQueryClassifyPercentListener != null) {
                onQueryClassifyPercentListener.querySuccess(chartDataList);
            }
        } else {
            if (onQueryClassifyPercentListener != null) {
                onQueryClassifyPercentListener.queryFailed();
            }
        }
    }

    private float queryMoneyCount(int type, long startTime, long endTime) {
        String sql = "select sum(r.money) sum from record r inner join classify c " +
                "on r.classify_id = c._id " +
                "where r.available = ? and " +
                "r.record_ms>=? and r.record_ms<=? and " +
                "c.type =?";

        Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.TRUE + "", startTime + "", endTime + "", type + ""});
        Log.i("cursor", cursor.getCount() + "");
        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            float money = Math.abs(cursor.getFloat(cursor.getColumnIndex("sum")));

            cursor.close();

            return money;
        }

        return 0;
    }
}
