package com.accountbook.biz.impl;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.accountbook.R;
import com.accountbook.tools.ConstantContainer;

import java.util.UUID;

public class SQLite {

    public static final String ROLE_TABLE = "role";
    public static final String CLASSIFY_TABLE = "classify";


    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static SQLite mInstance;

    private SQLite() {

    }

    public static SQLite getInstance() {
        if (mInstance == null) {
            synchronized (SQLite.class) {
                if (mInstance == null) {
                    mInstance = new SQLite();
                }
            }
        }
        return mInstance;
    }

    /**
     * 只允许当前包下的类能获取数据库对象
     *
     * @return 数据库对象
     */
    protected SQLiteDatabase getDatabaseObject() {
        return mDatabase;
    }

    /**
     * 创建本地数据库
     *
     * @param name 数据库名
     */
    public void createDataBase(String name, Context context) throws SQLiteException {
        mContext = context;
        mDatabase = mContext.openOrCreateDatabase(name, Context.MODE_PRIVATE, null);
    }

    /**
     * 创建user表
     */
    public void createUserTable() throws SQLiteException {
        mDatabase.execSQL("create table if not exists user (" +
                "_id varchar(30) primary key, " +
                "username varchar(20) not null," +
                "email varchar(20) unique)");
    }

    /**
     * 创建role表
     */
    public void createRoleTable() throws SQLiteException {
        mDatabase.execSQL("create table if not exists role (" +
                "_id text primary key, " +
                "object_id text," +
                "role text," +
                "isSave int," +
                "available int," +
                "timestamp number)");
    }

    /**
     * 初始化默认家庭角色数据
     */
    public void initDefaultRole() throws SQLiteException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] roles = mContext.getResources().getStringArray(R.array.role_default);
                for (int i = 0; i < roles.length; i++) {
                    ContentValues values = new ContentValues();
                    values.put("_id", UUID.randomUUID().toString());
                    values.put("role", roles[i]);
                    values.put("isSave", ConstantContainer.FALSE);
                    values.put("available", ConstantContainer.TRUE);
                    values.put("timestamp", System.currentTimeMillis());
                    mDatabase.insert(ROLE_TABLE, null, values);

                    values.clear();
                }
            }
        }).start();
    }

    /**
     * 创建分类表
     *
     * @throws SQLiteException
     */
    public void createClassifyTable() throws SQLiteException {
        mDatabase.execSQL("create table if not exists classify (" +
                "_id text primary key, " +
                "object_id text," +
                "classify text," +
                "iconResId number," +
                "color text," +
                "type int," +
                "isSave int," +
                "available int," +
                "timestamp number)");
    }

    /**
     * 添加默认的分类数据
     *
     * @throws SQLiteException
     */
    public void initDefaultClassify() throws SQLiteException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Resources resources = mContext.getResources();

                String[] colors = resources.getStringArray(R.array.color);

                TypedArray expendIcons = resources.obtainTypedArray(R.array.expend_classify_default_icon);
                String[] expendLabels = resources.getStringArray(R.array.expend_classify_default_label);

                TypedArray incomeIcons = resources.obtainTypedArray(R.array.income_classify_default_icon);
                String[] incomeLabels = resources.getStringArray(R.array.income_classify_default_label);

                //添加支出的分类
                for (int i = 0; i < expendLabels.length; i++) {
                    ContentValues values = new ContentValues();
                    values.put("_id", UUID.randomUUID().toString());
                    values.put("classify", expendLabels[i]);
                    values.put("iconResId", expendIcons.getResourceId(i, R.mipmap.ic_launcher));
                    values.put("color", colors[i]);
                    values.put("type", ConstantContainer.EXPEND);
                    values.put("isSave", ConstantContainer.FALSE);
                    values.put("available", ConstantContainer.TRUE);
                    values.put("timestamp", System.currentTimeMillis());

                    mDatabase.insert(CLASSIFY_TABLE, null, values);
                    values.clear();
                }

                //添加收入的分类
                for (int i = 0; i < incomeLabels.length; i++) {
                    ContentValues values = new ContentValues();

                    values.put("_id", UUID.randomUUID().toString());
                    values.put("classify", incomeLabels[i]);
                    values.put("iconResId", incomeIcons.getResourceId(i, R.mipmap.ic_launcher));
                    values.put("color", colors[i]);
                    values.put("type", ConstantContainer.INCOME);
                    values.put("isSave", ConstantContainer.FALSE);
                    values.put("available", ConstantContainer.TRUE);
                    values.put("timestamp", System.currentTimeMillis());

                    mDatabase.insert(CLASSIFY_TABLE, null, values);
                    values.clear();
                }

                //添加借入
                ContentValues borrowValues = new ContentValues();

                borrowValues.put("_id", UUID.randomUUID().toString());
                borrowValues.put("classify", "借入");
                borrowValues.put("iconResId", R.mipmap.ic_thumbs_up_down_white_24dp);
                borrowValues.put("color", "#795548");
                borrowValues.put("type", ConstantContainer.BORROW);
                borrowValues.put("isSave", ConstantContainer.FALSE);
                borrowValues.put("available", ConstantContainer.TRUE);
                borrowValues.put("timestamp", System.currentTimeMillis());

                mDatabase.insert(CLASSIFY_TABLE, null, borrowValues);
                borrowValues.clear();

                //添加借出
                ContentValues lendValues = new ContentValues();

                lendValues.put("_id", UUID.randomUUID().toString());
                lendValues.put("classify", "借出");
                lendValues.put("iconResId", R.mipmap.ic_thumbs_up_down_white_24dp);
                lendValues.put("color", "#ff5722");
                lendValues.put("type", ConstantContainer.LEND);
                lendValues.put("isSave", ConstantContainer.FALSE);
                lendValues.put("available", ConstantContainer.TRUE);
                lendValues.put("timestamp", System.currentTimeMillis());

                mDatabase.insert(CLASSIFY_TABLE, null, lendValues);
                lendValues.clear();

                expendIcons.recycle();
                incomeIcons.recycle();
            }
        }).start();
    }
    /**
     * 清理数据
     * 因为不同情景对于异常有不同处理方式所以，抛异常
     */
    public void clearData() throws Exception{
        throw new Exception("假失败");
    }
}
