package com.accountbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.impl.UserBiz;
import com.accountbook.view.api.IRegistryView;

/**
 * Created by Grady on 2016.2.24.
 */
public class RegistryPresenter {
    private String username;
    private String password;
    private String passwordConfirm;
    private IRegistryView mRegistryView;
    private IUserBiz userBiz;

    public RegistryPresenter(IRegistryView iRegistryView) {
        this.mRegistryView = iRegistryView;
        userBiz = new UserBiz();
    }

    /**
     * 执行注册
     */
    public void doRegistry(Context context) {
        username = mRegistryView.getRegUsername();
        password = mRegistryView.getRegPassword();
        passwordConfirm = mRegistryView.getRegPasswordConfirm();

        if (validateUsername() && validatePassword()) {
            mRegistryView.showProgress();
            userBiz.registry(context, username, password, new UserBiz.OnRegistryListener() {
                @Override
                public void registrySuccess() {
                    //提交UI变化
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mRegistryView.registerSuccess();
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
                            mRegistryView.registerFailed(message);
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
            mRegistryView.registerFailed("用户名不能为空");
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
            mRegistryView.registerFailed("密码不能为空");
            return false;
        } else if (passwordConfirm.equals("")) {
            mRegistryView.registerFailed("重复密码不能为空");
            return false;
        } else if (!password.equals(passwordConfirm)) {
            mRegistryView.registerFailed("两次密码不一样");
            mRegistryView.registerFailed("两次密码不一样");
            return false;
        } else return true;
    }
}
