package com.accountbook.biz.impl;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLite {

    public static SQLiteDatabase db;

    public static String USERTABLE = "_user";

    public static void initLocalDatabase(Context context) {

        //创建数据库
        db = context.openOrCreateDatabase("accountBook.db", context.MODE_PRIVATE, null);

        //创建_user表
        db.execSQL("create table if not exists " + USERTABLE + "(" +
                "id varchar(30) primary key, " +
                "username varchar(20) not null," +
                "email varchar(20) unique," +
                "fid varchar(30) null," +
                "actor varchar(20) null," +
                "money real not null)");
    }
}
