package com.accountbook.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 该类是处理SharedPreferences的工具类
 */
public class QuickSimpleIO {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public QuickSimpleIO(Context context, String name) {
        mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public String getString(String key) {
        return mPreferences.getString(key, "读取数据失败");
    }

    public void setBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, true);
    }

    public void setInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public int getInt(String key) {
        return mPreferences.getInt(key, 0);
    }

    public void setLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public long getLong(String key) {
        return mPreferences.getLong(key, 0);
    }
}
