package com.accountbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.api.OnRegistryListener;
import com.accountbook.biz.impl.UserBiz;
import com.accountbook.view.api.IRegistryView;
import com.accountbook.view.customview.ProgressButton;

/**
 * Created by Grady on 2016.2.24.
 */
public class RegistryPresenter {

    private String username;
    private String password;
    private String passwordConfirm;
    private IRegistryView view;
    private IUserBiz userBiz;

    public RegistryPresenter(IRegistryView view) {
        this.view = view;
        userBiz = new UserBiz();
    }

    /**
     * 执行注册
     */
    public void doRegistry() {
        username = view.getRegUsername();
        password = view.getRegPassword();
        passwordConfirm = view.getRegPasswordConfirm();

        if (validateUsername() && validatePassword()) {
            view.uiBeginReg();

            userBiz.registry((Context) view, username, password, new OnRegistryListener() {
                @Override
                public void registrySuccess() {

                    //提交UI变化
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.registerSuccess();
                        }
                    });
                }

                @Override
                public void registryFailed(final String message) {
                    //提交UI变化
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.registerFailed(message);
                        }
                    });
                }
            });


        }//if
    }

    /**
     * 检查用户名
     *
     * @return true表示没问题
     */
    private boolean validateUsername() {
        if (username.equals("")) {
            view.showRegUsernameError("用户名不能为空");
            return false;
        } else return true;
    }

    /**
     * 检查密码
     *
     * @return true表示没问题
     */
    private boolean validatePassword() {
        if (password.equals("")) {
            view.showRegPasswordError("密码不能为空");
            return false;
        } else if (passwordConfirm.equals("")) {
            view.showRegPasswordConfirmError("重复密码不能为空");
            return false;
        } else if (!password.equals(passwordConfirm)) {
            view.showRegPasswordError("两次密码不一样");
            view.showRegPasswordConfirmError("两次密码不一样");
            return false;
        } else return true;
    }
}
