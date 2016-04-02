package com.accountbook.view.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.accountbook.R;
import com.accountbook.presenter.LogoutPresenter;
import com.accountbook.presenter.service.SyncService;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.QuickSimpleIO;
import com.accountbook.view.api.ILogoutView;
import com.accountbook.view.api.ToolbarMenuOnClickListener;
import com.accountbook.view.fragment.BudgetFragment;
import com.accountbook.view.fragment.ChartFragment;
import com.accountbook.view.fragment.HomeFragment;
import com.avos.avoscloud.AVUser;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ToolbarMenuOnClickListener, ILogoutView {
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Bind(R.id.Drawer)
    DrawerLayout mDrawerLayout;

    private TextView userName;
    private LogoutPresenter mLogoutPresenter;
    private boolean isClearData = false;

    private HomeFragment mHomeFragment;
    private ChartFragment mChartFragment;
    private BudgetFragment mBudgetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        initView();
        recoveryFragment(savedInstanceState);
    }

    /**
     * 该方法用于在内存重载时恢复fragment的状态，防止重叠bug
     *
     * @param savedInstanceState savedInstanceState 不等于null时，代表应用的内存被重载了
     */
    private void recoveryFragment(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (savedInstanceState != null) {
            mHomeFragment = (HomeFragment) fm.findFragmentByTag("HomeFragment");
            mChartFragment = (ChartFragment) fm.findFragmentByTag("ChartFragment");
            mBudgetFragment = (BudgetFragment) fm.findFragmentByTag("BudgetFragment");

            transaction.hide(mChartFragment).hide(mBudgetFragment).show(mHomeFragment);
            transaction.commit(); //提交事物
        } else {
            //内存未被重载的时候，初始化所有的fragment，并添加进transaction
            mHomeFragment = new HomeFragment();
            mHomeFragment.setToolbarMenuOnClickListener(this);
            transaction.add(R.id.frag_container, mHomeFragment, "HomeFragment");

            mChartFragment = new ChartFragment();
            mChartFragment.setToolbarMenuOnClickListener(this);
            transaction.add(R.id.frag_container, mChartFragment, "ChartFragment");

            mBudgetFragment = new BudgetFragment();
            mBudgetFragment.setToolbarMenuOnClickListener(this);
            transaction.add(R.id.frag_container, mBudgetFragment, "BudgetFragment");

            //将主页显示出来，其他隐藏
            transaction.hide(mChartFragment).hide(mBudgetFragment).show(mHomeFragment);
            transaction.commit();//提交事物
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindEvents();
    }

    /**
     * 初始化布局中的View
     */
    private void initView() {
        //绑定抽屉
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }

    /**
     * 绑定事件
     */
    private void bindEvents() {
        mNavigationView.setNavigationItemSelectedListener(this);
        //监听抽屉的事件
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            //当抽屉打开时，读取用户数据。（其实是因为没打开的时候TextView是null的，其他地方设置不了）
            @Override
            public void onDrawerOpened(View drawerView) {
                userName = (TextView) findViewById(R.id.userName);
                loadUserInfo();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * 抽屉中navigationView的各个item的点击事件回调
     *
     * @param item 被点击的item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.logout:
                if (AVUser.getCurrentUser() == null) {
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else
                    showLogoutDialog();
                break;
            case R.id.family:
                startActivity(new Intent(MainActivity.this, RolesActivity.class));
                break;
            default:
                switchFragment(id);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 切换fragment的方法
     *
     * @param id 抽屉中每个item的ID
     */
    private void switchFragment(int id) {
        /*获取FragmentManager对象,必须选择v4包下的*/
        FragmentManager fm = getSupportFragmentManager();
        /*获取一个Fragment事物，通过FragmentManager对象调用beginTransaction方法，开启一个事物*/
        FragmentTransaction transaction = fm.beginTransaction();

        switch (id) {
            case R.id.home_page:
                /*先隐藏所有的Fragment*/
                hideAllFragment(transaction);
                //显示出要显示的fragment
                transaction.show(mHomeFragment);
                break;
            case R.id.budget:
                /*先隐藏所有的Fragment*/
                hideAllFragment(transaction);
                //显示出要显示的fragment
                transaction.show(mBudgetFragment);
                break;
            case R.id.chart:
                /*先隐藏所有的Fragment*/
                hideAllFragment(transaction);
                //显示出要显示的fragment
                transaction.show(mChartFragment);
                break;
        }

        transaction.commit();
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mChartFragment != null) {
            transaction.hide(mChartFragment);
        }
        if (mBudgetFragment != null) {
            transaction.hide(mBudgetFragment);
        }
    }

    /**
     * 点击头像跳转到LoginView
     */
    public void goLogin(View view) {
        if (AVUser.getCurrentUser() == null) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            Intent intent = new Intent(MainActivity.this, LoginAndRegistryActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void toolbarMenuOnClick() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }


    /**
     * 从登录界面跳转回来后把用户名显示
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            loadUserInfo();
            firstSync();
            FragmentManager fm = getSupportFragmentManager();
            mHomeFragment = (HomeFragment) fm.findFragmentByTag("HomeFragment");
            mHomeFragment.onActivityResult(requestCode, resultCode, data);
        } else if (resultCode == 11) {
            FragmentManager fm = getSupportFragmentManager();
            mHomeFragment = (HomeFragment) fm.findFragmentByTag("HomeFragment");
            mHomeFragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == 12) {
            FragmentManager fm = getSupportFragmentManager();
            mBudgetFragment = (BudgetFragment) fm.findFragmentByTag("BudgetFragment");
            mBudgetFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firstSync() {
        QuickSimpleIO io = new QuickSimpleIO(this, "version_sp");
        io.setBoolean("isFirstSync", true);
    }

    /**
     * 读取用户数据
     */
    private void loadUserInfo() {
        if (AVUser.getCurrentUser() != null) {
            userName.setText(AVUser.getCurrentUser().getUsername());
        } else userName.setText(R.string.loginHint);
    }

    /**
     * 开始注销
     */
    public void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定注销吗？");
        builder.setMultiChoiceItems(new String[]{"删除全部数据"}, new boolean[]{false}, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                isClearData = isChecked;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                if (mLogoutPresenter == null) {
                    mLogoutPresenter = new LogoutPresenter(MainActivity.this);
                }

                mLogoutPresenter.doLogout(MainActivity.this);
            }
        });
        builder.setNegativeButton("取消", null);

        builder.show();
    }


    /**
     * 是否清除数据
     *
     * @return true == 是
     */
    @Override
    public boolean isClearData() {
        Log.i("data", isClearData + "");
        return isClearData;
    }

    /**
     * 登出完成
     */
    @Override
    public void logoutComplete() {
        loadUserInfo();
    }

    /**
     * 登出失败
     *
     * @param message 错误信息
     */
    @Override
    public void logoutFailed(String message) {
        Toast.makeText(this, "注销失败" + message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 清除完成
     */
    @Override
    public void clearComplete() {
        Intent intent = new Intent(ConstantContainer.LOGOUT_DONE_URI);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        Toast.makeText(MainActivity.this, "清除完成", Toast.LENGTH_SHORT).show();
    }

    /**
     * 清除失败
     *
     * @param message 错误信息
     */
    @Override
    public void clearFailed(String message) {
        Toast.makeText(this, "清空数据失败" + message, Toast.LENGTH_SHORT).show();
    }
}
