package com.accountbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.accountbook.biz.impl.UserBiz;
import com.accountbook.biz.api.IUserBiz;
import com.accountbook.view.api.ILoginView;


public class LoginPresenter {

    private ILoginView mLoginView;
    private IUserBiz userBiz;
    private String username;
    private String password;

    public LoginPresenter(ILoginView view) {
        this.mLoginView = view;
        userBiz = new UserBiz();
    }

    /**
     * 检查输入的用户名
     *
     * @return true表示没问题
     */
    private boolean validateUsername() {
        if (username.equals("")) {
            mLoginView.loginFailed("用户名不能为空");
            return false;
        } else return true;
    }

    /**
     * 检查输入的密码
     *
     * @return true表示没问题
     */
    private boolean validatePassword() {
        if (password.equals("")) {
            mLoginView.loginFailed("密码不能为空");
            return false;
        } else return true;
    }

    /**
     * 登录逻辑
     */
    public void doLogin(Context context) {
        username = mLoginView.getLoginUsername();
        password = mLoginView.getLoginPassword();
        mLoginView.showProgress();

        if (validateUsername() && validatePassword()) {
            userBiz.login(context, username, password, new UserBiz.OnLoginListener() {
                @Override
                public void loginSuccess() {
                    //更新UI
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mLoginView.loginSuccess();
                        }
                    });
                }

                @Override
                public void loginFailed(final String message) {
                    //更新UI
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mLoginView.loginFailed(message);
                        }
                    });
                }
            });
        }

    }

}
