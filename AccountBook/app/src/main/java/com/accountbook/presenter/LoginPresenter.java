package com.accountbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.accountbook.biz.impl.UserBiz;
import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.api.OnLoginListener;
import com.accountbook.view.api.ILoginView;
import com.accountbook.view.customview.ProgressButton;

public class LoginPresenter {

    private ILoginView view;
    private IUserBiz userBiz;
    private String username;
    private String password;

    public LoginPresenter(ILoginView view) {
        this.view = view;
        userBiz = new UserBiz();
    }

    /**
     * 检查输入的用户名
     *
     * @return true表示没问题
     */
    private boolean validateUsername() {
        if (username.equals("")) {
            view.showLoginUsernameError("用户名不能为空");
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
            view.showLoginPasswordError("密码不能为空");
            return false;
        } else return true;
    }

    /**
     * 登录逻辑
     */
    public void doLogin(ProgressButton progress) {
        username = view.getLoginUsername();
        password = view.getLoginPassword();

        if (validateUsername() && validatePassword()) {
            progress.showProgress();
            userBiz.login((Context) view, username, password, new OnLoginListener() {

                @Override
                public void loginSuccess() {
                    //提交UI变化
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.loginSuccess();
                        }
                    });
                }

                @Override
                public void loginFailed(final String message) {
                    //提交UI变化
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.loginFailed(message);
                        }
                    });
                }

            });
        }

    }

}
