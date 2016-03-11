package com.accountbook.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginAndRegistryActivity extends BaseActivity implements ILoginView, IRegistryView {

    View[] views;
    String[] titles;


    @Bind(R.id.login_reg_toolbar)
    Toolbar toolbar;
    @Bind(R.id.welcome_text)
    TextView welcome;
    @Bind(R.id.upWrapper)
    RelativeLayout upWrapper;
    @Bind(R.id.login_reg_viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    //    @Bind(R.id.login_usernameWrapper)
    TextInputLayout login_usernameWrapper;
    //    @Bind(R.id.login_passwordWrapper)
    TextInputLayout login_passwordWrapper;
    //    @Bind(R.id.login_usernameInput)
    EditText login_usernameInput;
    //    @Bind(R.id.login_passwordInput)
    EditText login_passwordInput;
    //    @Bind(R.id.login_btn)
    ProgressButton login_btn;

    //    @Bind(R.id.reg_usernameWrapper)
    TextInputLayout reg_usernameWrapper;
    //    @Bind(R.id.reg_passwordWrapper)
    TextInputLayout reg_passwordWrapper;
    //    @Bind(R.id.reg_passwordConfirmWrapper)
    TextInputLayout reg_passwordConfirmWrapper;
    //    @Bind(R.id.reg_usernameInput)
    EditText reg_usernameInput;
    //    @Bind(R.id.reg_passwordInput)
    EditText reg_passwordInput;
    //    @Bind(R.id.reg_passwordConfirmInput)
    EditText reg_passwordConfirmInput;
    //    @Bind(R.id.reg_btn)
    ProgressButton reg_btn;

    private LoginPresenter loginPresenter;
    private RegistryPresenter registryPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_registry);

        initView();
        bindEvents();
        loginPresenter = new LoginPresenter(this);
        registryPresenter = new RegistryPresenter(this);
    }

    /**
     * 初始化View
     */
    public void initView() {

        /*绑定控件*/
        ButterKnife.bind(this);

        /*设置启用toolbar*/
        setSupportActionBar(toolbar);

        /*加载两个view的布局*/
        views = new View[2];
        LayoutInflater inflater = getLayoutInflater();
        views[0] = inflater.inflate(R.layout.activity_login, null);
        views[1] = inflater.inflate(R.layout.activity_registry, null);

        /*初始化两个标题，用于下面的适配器的getPageTitle()*/
        titles = new String[]{"登录", "注册"};

        /*配置viewPager的适配器*/
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
        /*跟tabLayout绑定*/
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        /*绑定控件*/
        login_usernameWrapper = (TextInputLayout) views[0].findViewById(R.id.login_usernameWrapper);
        login_passwordWrapper = (TextInputLayout) views[0].findViewById(R.id.login_passwordWrapper);
        login_usernameInput = (EditText) views[0].findViewById(R.id.login_usernameInput);
        login_passwordInput = (EditText) views[0].findViewById(R.id.login_passwordInput);
        login_btn = (ProgressButton) views[0].findViewById(R.id.login_btn);

        reg_usernameWrapper = (TextInputLayout) views[1].findViewById(R.id.reg_usernameWrapper);
        reg_passwordWrapper = (TextInputLayout) views[1].findViewById(R.id.reg_passwordWrapper);
        reg_passwordConfirmWrapper = (TextInputLayout) views[1].findViewById(R.id.reg_passwordConfirmWrapper);
        reg_usernameInput = (EditText) views[1].findViewById(R.id.reg_usernameInput);
        reg_passwordInput = (EditText) views[1].findViewById(R.id.reg_passwordInput);
        reg_passwordConfirmInput = (EditText) views[1].findViewById(R.id.reg_passwordConfirmInput);
        reg_btn = (ProgressButton) views[1].findViewById(R.id.reg_btn);

        /*将TextInputLayout设置为显示错误*/
        login_usernameWrapper.setErrorEnabled(true);
        login_passwordWrapper.setErrorEnabled(true);
        reg_usernameWrapper.setErrorEnabled(true);
        reg_passwordWrapper.setErrorEnabled(true);
        reg_passwordConfirmWrapper.setErrorEnabled(true);
    }

    /**
     * 绑定事件
     */
    private void bindEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upWrapper.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                welcome.setHeight(upWrapper.getHeight());
            }
        });

        login_btn.setOnClickListener(new ProgressButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.doLogin();
            }
        });

        reg_btn.setOnClickListener(new ProgressButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                registryPresenter.doRegistry();
            }
        });

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
    }


    /**
     * 开始执行操作的UI变化
     */
    @Override
    public void uiBeginLogin() {
        login_usernameWrapper.setError("");
        login_passwordWrapper.setError("");
        login_btn.showProgress();
    }

    /**
     * 结束后的UI变化
     */
    @Override
    public void uiEndLogin() {

    }

    /**
     * 开始执行操作的UI变化
     */
    @Override
    public void uiBeginReg() {
        reg_usernameWrapper.setError("");
        reg_passwordWrapper.setError("");
        reg_passwordConfirmWrapper.setError("");
        reg_btn.showProgress();
    }

    /**
     * 结束后的UI变化
     */
    @Override
    public void uiEndReg() {

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
        Log.i("登陆成功", "登陆成功");
        login_btn.done();
        login_btn.setDoneListener(new ProgressButton.OnProgressDoneListener() {
            @Override
            public void done() {
                LoginAndRegistryActivity.this.setResult(2);
                finish();
            }
        });
    }

    /**
     * 登录失败做的事情
     *
     * @param message 错误信息
     */
    @Override
    public void loginFailed(String message) {
        login_btn.error(message);
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
        reg_btn.done();
        reg_btn.setDoneListener(new ProgressButton.OnProgressDoneListener() {
            @Override
            public void done() {
                LoginAndRegistryActivity.this.setResult(2);
//                Toast.makeText(LoginAndRegistryActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /**
     * 注册失败
     *
     * @param message 错误信息
     */
    @Override
    public void registerFailed(String message) {
//        Toast.makeText(LoginAndRegistryActivity.this, message, Toast.LENGTH_SHORT).show();
        reg_btn.error(message);
    }


}
