package com.accountbook.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.accountbook.R;
import com.accountbook.view.fragment.LoginFragment;
import com.accountbook.view.fragment.RegisterFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginAndRegistryActivity extends AppCompatActivity {
    @Bind(R.id.login_reg_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.login_reg_viewpager)
    ViewPager mViewpager;

    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_and_registry_activity);
        ButterKnife.bind(this);

        init();
        bindEvents();
    }

    private void bindEvents() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化View
     */
    public void init() {
        /*设置启用toolbar*/
        setSupportActionBar(mToolbar);

        mFragments = new ArrayList<>();
        mFragments.add(new LoginFragment());
        mFragments.add(new RegisterFragment());

        mViewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        /*跟tabLayout绑定*/
        mTabLayout.setupWithViewPager(mViewpager);
        mTabLayout.getTabAt(0).setText(R.string.login_btn_text);
        mTabLayout.getTabAt(1).setText(R.string.reg_btn_text);
    }

//    /**
//     * 绑定事件
//     */
//    private void bindEvents() {
//
//        upWrapper.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                welcome.setHeight(upWrapper.getHeight());
//            }
//        });
//
//        login_btn.setOnClickListener(new ProgressButton.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loginPresenter.doLogin();
//            }
//        });
//
//        reg_btn.setOnClickListener(new ProgressButton.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                registryPresenter.doRegistry();
//            }
//        });
//
//        login_passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                loginPresenter.doLogin();
//                return false;
//            }
//        });
//
//        reg_passwordConfirmInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                registryPresenter.doRegistry();
//                return false;
//            }
//        });
//    }
//
//
//    /**
//     * 开始执行操作的UI变化
//     */
//    @Override
//    public void uiBeginLogin() {
//        login_usernameWrapper.setError("");
//        login_passwordWrapper.setError("");
//        login_btn.showProgress();
//    }
//
//
//    /**
//     * 开始执行操作的UI变化
//     */
//    @Override
//    public void uiBeginReg() {
//        reg_usernameWrapper.setError("");
//        reg_passwordWrapper.setError("");
//        reg_passwordConfirmWrapper.setError("");
//        reg_btn.showProgress();
//    }
//
//    /**
//     * 提供给presenter获取username的方法
//     *
//     * @return 用户名
//     */
//    @Override
//    public String getLoginUsername() {
//        return login_usernameInput.getText().toString();
//    }
//
//    /**
//     * 提供给presenter获取password的方法
//     *
//     * @return 密码
//     */
//    @Override
//    public String getLoginPassword() {
//        return login_passwordInput.getText().toString();
//    }
//
//    /**
//     * 登录成做的事情
//     */
//    @Override
//    public void loginSuccess() {
//        Log.i("登陆成功", "登陆成功");
//        login_btn.done();
//        login_btn.setDoneListener(new ProgressButton.OnProgressDoneListener() {
//            @Override
//            public void done() {
//                LoginAndRegistryActivity.this.setResult(2);
//                finish();
//            }
//        });
//    }

    class FragmentAdapter extends FragmentPagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
