package com.accountbook.view.activity;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accountbook.R;
import com.accountbook.presenter.LoginPresenter;
import com.accountbook.presenter.RegistryPresenter;
import com.accountbook.view.api.ILoginView;
import com.accountbook.view.api.IRegistryView;
import com.accountbook.view.customview.ProgressButton;

import java.util.ArrayList;
import java.util.List;


public class LoginAndRegistryActivity extends BaseActivity implements ILoginView,IRegistryView {



    private Toolbar toolbar;
    private TextView welcome;
    private RelativeLayout upWrapper;
    private View[] views;
    private String[] titles;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private TextInputLayout login_usernameWrapper;
    private TextInputLayout login_passwordWrapper;
    private EditText login_usernameInput;
    private EditText login_passwordInput;
    private ProgressButton login_btn;

    private TextInputLayout reg_usernameWrapper;
    private TextInputLayout reg_passwordWrapper;
    private TextInputLayout reg_passwordConfirmWrapper;
    private EditText reg_usernameInput;
    private EditText reg_passwordInput;
    private EditText reg_passwordConfirmInput;
    private Button reg_btn;

    private LoginPresenter loginPresenter;
    private RegistryPresenter registryPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_registry);

        initView();
        loginPresenter = new LoginPresenter(this);
        registryPresenter = new RegistryPresenter(this);
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


        LayoutInflater inflater = getLayoutInflater();
        views = new View[2];
        titles = new String[2];
        views[0] = inflater.inflate(R.layout.activity_login, null);
        views[1] = inflater.inflate(R.layout.activity_registry,null);
        titles[0] = "登录";
        titles[1] = "注册";



        viewPager = (ViewPager)findViewById(R.id.login_reg_viewpager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = views[position];
                container.addView(view);
                return view;
            }

            @Override
            public int getCount() {
                return views.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views[position]);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        login_usernameWrapper = (TextInputLayout)views[0].findViewById(R.id.login_usernameWrapper);
        login_passwordWrapper = (TextInputLayout)views[0].findViewById(R.id.login_passwordWrapper);
        login_usernameInput= (EditText)views[0].findViewById(R.id.login_usernameInput);
        login_passwordInput= (EditText)views[0].findViewById(R.id.login_passwordInput);
        login_btn = (ProgressButton)views[0].findViewById(R.id.login_btn);
        login_usernameWrapper.setErrorEnabled(true);
        login_passwordWrapper.setErrorEnabled(true);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_usernameWrapper.setError("");
                login_passwordWrapper.setError("");
                loginPresenter.doLogin();
            }
        });

        reg_usernameWrapper = (TextInputLayout)views[1].findViewById(R.id.reg_usernameWrapper);
        reg_passwordWrapper = (TextInputLayout)views[1].findViewById(R.id.reg_passwordWrapper);
        reg_passwordConfirmWrapper = (TextInputLayout)views[1].findViewById(R.id.reg_passwordConfirmWrapper);
        reg_usernameInput= (EditText)views[1].findViewById(R.id.reg_usernameInput);
        reg_passwordInput= (EditText)views[1].findViewById(R.id.reg_passwordInput);
        reg_passwordConfirmInput = (EditText)views[1].findViewById(R.id.reg_passwordConfirmInput);
        reg_btn = (Button)views[1].findViewById(R.id.reg_btn);
        reg_usernameWrapper.setErrorEnabled(true);
        reg_passwordWrapper.setErrorEnabled(true);
        reg_passwordConfirmWrapper.setErrorEnabled(true);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_usernameWrapper.setError("");
                reg_passwordWrapper.setError("");
                reg_passwordConfirmWrapper.setError("");
                registryPresenter.doRegistry();
            }
        });
    }

    /**
     * 提供给presenter获取username的方法
     *
     * @return 用户名
     */
    @Override
    public String getLoginUsername() {
        return login_usernameInput.getText().toString();
    }

    /**
     * 提供给presenter获取password的方法
     *
     * @return 密码
     */
    @Override
    public String getLoginPassword() {
        return login_passwordInput.getText().toString();
    }

    /**
     * 登录成做的事情
     */
    @Override
    public void loginSuccess() {
        Toast.makeText(LoginAndRegistryActivity.this, "loginSuccess", Toast.LENGTH_SHORT).show();
        this.setResult(2);
        finish();
    }

    /**
     * 登录失败做的事情
     *
     * @param message
     */
    @Override
    public void loginFailed(String message) {
        Toast.makeText(LoginAndRegistryActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示用户名错误
     *
     * @param message 错误信息
     */
    @Override
    public void showLoginUsernameError(String message) {
        login_usernameWrapper.setError(message);
    }

    /**
     * 显示密码错误
     *
     * @param message 错误信息
     */
    @Override
    public void showLoginPasswordError(String message) {
        login_passwordWrapper.setError(message);
    }

    /**
     * 提供用户名
     *
     * @return 填入的用户名
     */
    @Override
    public String getRegUsername() {
        return reg_usernameInput.getText().toString();
    }

    /**
     * 提供密码
     *
     * @return 填入的密码
     */
    @Override
    public String getRegPassword() {
        return reg_passwordInput.getText().toString();
    }

    /**
     * 提供重复密码
     *
     * @return 填入的密码确认
     */
    @Override
    public String getRegPasswordConfirm() {
        return reg_passwordConfirmInput.getText().toString();
    }

    /**
     * 显示用户名错误
     *
     * @param message 错误信息
     */
    @Override
    public void showRegUsernameError(String message) {
        reg_usernameWrapper.setError(message);
    }

    /**
     * 显示密码错误
     *
     * @param message 错误信息
     */
    @Override
    public void showRegPasswordError(String message) {
        reg_passwordWrapper.setError(message);
    }

    /**
     * 显示重复密码错误
     *
     * @param message 错误信息
     */
    @Override
    public void showRegPasswordConfirmError(String message) {
        reg_passwordConfirmWrapper.setError(message);
    }

    /**
     * 注册成功
     */
    @Override
    public void registerSuccess() {
        Toast.makeText(LoginAndRegistryActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
        this.setResult(2);
        finish();
    }

    /**
     * 注册失败
     *
     * @param message 错误信息
     */
    @Override
    public void registerFailed(String message) {
        Toast.makeText(LoginAndRegistryActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
