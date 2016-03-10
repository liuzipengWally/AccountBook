package com.accountbook.biz.impl;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.accountbook.R;

public class SQLite {
    public static final String USER_TABLE = "user";
    public static final String USER_COLUMN_ID = "_id";
    public static final String USER_COLUMN_USERNAME = "username";
    public static final String USER_COLUMN_EMAIL = "email";
//    public static final String USER_COLUMN_FID = "fid";
//    public static final String USER_COLUMN_ACTOR = "actor";
//    public static final String USER_COLUMN_MONEY = "money";
//    public static final String USER_COLUMN_MODIFIED = "modified";       //修改标记

    public static final String ROLE_TABLE = "role";

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
    public void createDataBase(String name, Context context) {
        mContext = context;
        mDatabase = mContext.openOrCreateDatabase(name, Context.MODE_PRIVATE, null);
    }

    /**
     * 创建user表
     */
    public void createUserTable() {
        mDatabase.execSQL("create table if not exists user (" +
                "_id varchar(30) primary key, " +
                "username varchar(20) not null," +
                "email varchar(20) unique)");
    }

    /**
     * 创建role表
     */
    public void createRoleTable() {
        mDatabase.execSQL("create table if not exists role (" +
                "_id integer primary key autoincrement," +
                "role varchar(10) not null )");
    }

    /**
     * 初始化默认家庭角色数据
     */
    public void initDefaultRole() {
        String[] roles = mContext.getResources().getStringArray(R.array.role_default);
        for (int i = 0; i < roles.length; i++) {
            ContentValues values = new ContentValues();
            values.put("role", roles[i]);
            mDatabase.insert(ROLE_TABLE, null, values);

            values.clear();
        }
    }
}
