package com.accountbook.view.activity;

import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.view.api.ILoginView;
import com.accountbook.view.api.IRegistryView;

import java.util.ArrayList;
import java.util.List;


public class LoginAndRegistryActivity extends BaseActivity implements ILoginView,IRegistryView {



    private Toolbar toolbar;
    private TextView welcome;
    private RelativeLayout upWrapper;
    private List<View> views;
    private List<String> titles;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private TextInputLayout login_usernameWrapper;
    private TextInputLayout login_passwordWrapper;
    private EditText login_usernameInput;
    private EditText login_passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_registry);

        initView();
    }

    public void initView(){
        toolbar = (Toolbar)findViewById(R.id.login_reg_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        welcome = (TextView)findViewById(R.id.welcome_text);
        upWrapper = (RelativeLayout)findViewById(R.id.upWrapper);
        upWrapper.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                welcome.setHeight(upWrapper.getHeight());
            }
        });

        views = new ArrayList<>();
        LayoutInflater inflater = getLayoutInflater();
        views.add(inflater.inflate(R.layout.activity_login, null));
        views.add(inflater.inflate(R.layout.activity_registry,null));
        titles = new ArrayList<>();
        titles.add("登录");
        titles.add("注册");

        viewPager = (ViewPager)findViewById(R.id.login_reg_viewpager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = views.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        login_usernameWrapper = (TextInputLayout) findViewById(R.id.login_usernameWrapper);
        login_passwordWrapper = (TextInputLayout)findViewById(R.id.login_passwordWrapper);

        login_usernameInput= (EditText)findViewById(R.id.login_usernameInput);
        login_passwordInput= (EditText)findViewById(R.id.login_passwordInput);


    }

    /**
     * 提供给presenter获取username的方法
     *
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return null;
    }

    /**
     * 提供给presenter获取password的方法
     *
     * @return 密码
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * 登录成做的事情
     */
    @Override
    public void loginSuccess() {

    }

    /**
     * 登录失败做的事情
     *
     * @param message
     */
    @Override
    public void loginFailed(String message) {

    }

    /**
     * 显示用户名错误
     *
     * @param message 错误信息
     */
    @Override
    public void showUsernameError(String message) {

    }

    /**
     * 显示密码错误
     *
     * @param message 错误信息
     */
    @Override
    public void showPasswordError(String message) {

    }

    /**
     * 提供重复密码
     *
     * @return 填入的密码确认
     */
    @Override
    public String getPasswordConfirm() {
        return null;
    }

    /**
     * 显示重复密码错误
     *
     * @param message 错误信息
     */
    @Override
    public void showPasswordConfirmError(String message) {

    }

    /**
     * 注册成功
     */
    @Override
    public void registerSuccess() {

    }

    /**
     * 注册失败
     *
     * @param message 错误信息
     */
    @Override
    public void registerFailed(String message) {

    }
}
