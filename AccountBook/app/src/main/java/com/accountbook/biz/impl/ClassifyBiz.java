package com.accountbook.biz.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.accountbook.biz.api.IClassifyBiz;
import com.accountbook.entity.local.Classify;
import com.accountbook.tools.ConstantContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzipeng on 16/3/12.
 */
public class ClassifyBiz implements IClassifyBiz {
    private SQLiteDatabase mDatabase;

    public interface OnQueryClassifyListener {
        void querySuccess(List<Classify> classifies);

        void queryFailed();
    }

    public interface OnDeleteClassifyListener {
        void deleteSuccess();

        void deleteFailed();
    }

    public interface OnRecoveryClassifyListener {
        void recoverySuccess();

        void recoveryFailed();
    }

    public ClassifyBiz() {
        mDatabase = SQLite.getInstance().getDatabaseObject();
    }

    @Override
    public void query(int type, OnQueryClassifyListener classifyListener) {
        List<Classify> classifies = new ArrayList<>();

        //查询 "_id", "classify", "color", "iconResId", "type" 四个字段的数据，必须满足available为可用，type为我们传进来的type
        Cursor cursor = mDatabase.query(SQLite.CLASSIFY_TABLE, new String[]{"_id", "classify", "color", "iconResId", "type"}
                , "available = ? and type = ?", new String[]{ConstantContainer.TRUE + "", type + ""}, null, null, null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Classify classify = new Classify();
                classify.setId(cursor.getString(cursor.getColumnIndex("_id")));
                classify.setClassify(cursor.getString(cursor.getColumnIndex("classify")));
                classify.setColor(cursor.getString(cursor.getColumnIndex("color")));
                classify.setIconResId(cursor.getInt(cursor.getColumnIndex("iconResId")));
                classify.setType(cursor.getInt(cursor.getColumnIndex("type")));
                classifies.add(classify);
            }

            //查询到的数据通过回调传递出去给presenter
            if (classifyListener != null) {
                classifyListener.querySuccess(classifies);
            }
        } else {
            //查询失败，通知presenter
            if (classifyListener != null) {
                classifyListener.queryFailed();
            }
        }
    }

    @Override
    public void delete(String id, OnDeleteClassifyListener deleteClassifyListener) {
        ContentValues values = new ContentValues();
        values.put("available", ConstantContainer.FALSE);
        values.put("isSave", ConstantContainer.FALSE);

        int successfulNum = mDatabase.update(SQLite.CLASSIFY_TABLE, values, "_id = ?", new String[]{id});
        values.clear();

        if (successfulNum > 0) {
            if (deleteClassifyListener != null) {
                deleteClassifyListener.deleteSuccess();
            }
        } else {
            if (deleteClassifyListener != null) {
                deleteClassifyListener.deleteFailed();
            }
        }
    }

    @Override
    public void recovery(String id, OnRecoveryClassifyListener recoveryClassifyListener) {
        ContentValues values = new ContentValues();
        values.put("available", ConstantContainer.TRUE);

        int successfulNum = mDatabase.update(SQLite.CLASSIFY_TABLE, values, "_id = ?", new String[]{id});
        values.clear();

        if (successfulNum > 0) {
            if (recoveryClassifyListener != null) {
                recoveryClassifyListener.recoverySuccess();
            }
        } else {
            if (recoveryClassifyListener != null) {
                recoveryClassifyListener.recoveryFailed();
            }
        }
    }
}
