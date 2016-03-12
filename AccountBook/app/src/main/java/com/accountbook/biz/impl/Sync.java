package com.accountbook.biz.impl;

import com.accountbook.entity.UserForLeanCloud;

/**
 * Created by Grady on 2016.3.12.
 */
public class Sync {

    private static final Sync mInstance = new Sync();

    private Sync(){

    }

    public static Sync getInstance(){
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
