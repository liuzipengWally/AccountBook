package com.accountbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.accountbook.biz.impl.UserBiz;
import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.api.OnLoginListener;
import com.accountbook.view.api.ILoginView;

public class LoginPresenter {
    private ILoginView view;
    private IUserBiz userBiz;
    private String username;
    private String password;

    public LoginPresenter(ILoginView view) {
        this.view = view;
        userBiz = UserBiz.getInstance();
    }

    /**
     * 检查输入的用户名
     * @return true表示有问题
     */
    private boolean validateUsername(){
        return username.equals("");
    }

    /**
     * 检查输入的密码
     * @return true表示有问题
     */
    private boolean validatePassword(){
        return password.equals("");
    }

    /**
     * 登录逻辑
     */
    public void dologin(){
        username = view.getUsername();
        password = view.getPassword();

        if(validateUsername()){
            view.showUsernameError("用户名不能为空!");
        }else if(validatePassword()){
            view.showPasswordError("密码不能为空!");
        }else{
            userBiz.login((Context)view,username, password, new OnLoginListener() {

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
