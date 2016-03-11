package com.accountbook.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.entity.UserForLeanCloud;
import com.accountbook.view.api.ToolbarMenuOnClickListener;
import com.accountbook.view.fragment.BudgetFragment;
import com.accountbook.view.fragment.ChartFragment;
import com.accountbook.view.fragment.HomeFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ToolbarMenuOnClickListener {
    @Bind(R.id.drawer)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    private TextView userName;


    private HomeFragment mHomeFragment;
    private ChartFragment mChartFragment;
    private BudgetFragment mBudgetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
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

        switchFragment(R.id.home_page);
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
        switchFragment(id);
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

                /*如果该对象为空，就实例化这个Fragment，并且将它添加进事物中。
                * transaction.add(R.id.contents,mOrderFragment);
                * 该方法第一个参数为要存放这些fragment的viewGroup，第二个参数就是该Fragment
                * */
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    mHomeFragment.setToolbarMenuOnClickListener(this);
                    transaction.add(R.id.frag_container, mHomeFragment);
                }

                //如果不为空则显示出来
                transaction.show(mHomeFragment);
                break;
            case R.id.budget:
                /*先隐藏所有的Fragment*/
                hideAllFragment(transaction);

                if (mBudgetFragment == null) {
                    mBudgetFragment = new BudgetFragment();
                    mBudgetFragment.setToolbarMenuOnClickListener(this);
                    transaction.add(R.id.frag_container, mBudgetFragment);
                }

                transaction.show(mBudgetFragment);
                break;
            case R.id.chart:
                /*先隐藏所有的Fragment*/
                hideAllFragment(transaction);

                if (mChartFragment == null) {
                    mChartFragment = new ChartFragment();
                    mChartFragment.setToolbarMenuOnClickListener(this);
                    transaction.add(R.id.frag_container, mChartFragment);
                }

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
        Intent intent = new Intent(MainActivity.this, LoginAndRegistryActivity.class);
        startActivityForResult(intent, 1);
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
        }
    }


    /**
     * 读取用户数据
     */
    private void loadUserInfo() {
        UserForLeanCloud user = UserForLeanCloud.getCurrentUser(UserForLeanCloud.class);
//            User user = ((MyApplication)getApplicationContext()).getUser();
        if (user != null) {
            userName.setText(user.getUsername());

            // TODO: 2016.3.4 刷新各种数据
        }
    }


}
