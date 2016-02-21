package com.accountbook.presenter;

import android.os.Handler;

import com.accountbook.model.UserModel;
import com.accountbook.modelAPI.IUserModel;
import com.accountbook.modelAPI.OnLoginListener;
import com.accountbook.viewAPI.ILoginView;

/**
 * Created by Grady on 2016.2.21.
 */
public class LoginPresenter {
    private ILoginView view;
    private IUserModel userModel;
    private String username;
    private String password;

    public LoginPresenter(ILoginView view) {
        this.view = view;
        userModel = new UserModel();
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
            userModel.login(username, password, new OnLoginListener() {

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
