package com.accountbook.presenter;

import android.content.Context;
import android.os.Handler;
import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.impl.UserBiz;
import com.accountbook.view.api.ILogoutView;
/**
 * Created by Grady on 2016.3.12.
 */
public class LogoutPresenter {
    private ILogoutView view;
    private IUserBiz userBiz;

    public LogoutPresenter(ILogoutView view) {
        this.view = view;
        userBiz = new UserBiz();
    }

    public void doLogout() {
        userBiz.logOut((Context) view, view.isClearData(), new IUserBiz.OnLogoutListener() {
            @Override
            public void logoutComplete() {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.logoutComplete();
                    }
                });
            }

            @Override
            public void logoutFailed(final String message) {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.logoutFailed(message);
                    }
                });
            }

            @Override
            public void clearComplete() {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.clearComplete();
                    }
                });
            }

            @Override
            public void clearFailed(final String message) {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.clearFailed(message);
                    }
                });
            }
        });
    }
}
