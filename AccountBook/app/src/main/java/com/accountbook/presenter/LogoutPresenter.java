package com.accountbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.impl.UserBiz;
import com.accountbook.view.api.ILogoutView;

public class LogoutPresenter {
    private ILogoutView mLogoutView;
    private IUserBiz mUserBiz;

    public LogoutPresenter(ILogoutView iLogoutView) {
        this.mLogoutView = iLogoutView;
        mUserBiz = new UserBiz();
    }

    public void doLogout(Context context) {
        mUserBiz.logOut(context, mLogoutView.isClearData(), new UserBiz.OnLogoutListener() {
            @Override
            public void logoutComplete() {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLogoutView.logoutComplete();
                    }
                });
            }

            @Override
            public void logoutFailed(final String message) {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLogoutView.logoutFailed(message);
                    }
                });
            }

            @Override
            public void clearComplete() {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLogoutView.clearComplete();
                    }
                });
            }

            @Override
            public void clearFailed(final String message) {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLogoutView.clearFailed(message);
                    }
                });
            }
        });
    }
}
