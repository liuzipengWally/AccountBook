package com.accountbook.biz.impl;

import com.accountbook.entity.UserForLeanCloud;

/**
 * Created by Grady on 2016.3.12.
 */
public class Sync {

    private Sync(){}

    private static Sync mInstance;

    /**
     * 单例构造
     *
     * @return 唯一的该类对象
     */
    public static Sync getInstance() {
        if (mInstance == null) {
            synchronized (SQLite.class) {
                if (mInstance == null) {
                    mInstance = new Sync();
                }
            }
        }
        return mInstance;
    }

    public void loadUserData(UserForLeanCloud user){
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        */
    }
}
