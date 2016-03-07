package com.accountbook.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liuzipeng on 16/3/7.
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
}
