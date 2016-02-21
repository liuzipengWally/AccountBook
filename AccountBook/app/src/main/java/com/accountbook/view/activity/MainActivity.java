package com.accountbook.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.accountbook.R;
import com.accountbook.view.api.ToolbarMenuOnClickListener;
import com.accountbook.view.fragment.AccountFragment;
import com.accountbook.view.fragment.ChartFragment;
import com.accountbook.view.fragment.HomeFragment;
import com.accountbook.view.fragment.WaterFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ToolbarMenuOnClickListener {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private HomeFragment mHomeFragment;
    private ChartFragment mChartFragment;
    private AccountFragment mAccountFragment;
    private WaterFragment mWaterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindEvents();
    }

    /**
     * 初始化布局中的View
     */
    private void initView() {
        //绑定抽屉
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        switchFragment(R.id.home_page);
    }

    /**
     * 绑定事件
     */
    private void bindEvents() {
        mNavigationView.setNavigationItemSelectedListener(this);
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
        /*先隐藏所有的Fragment*/
        hideAllFragment(transaction);

        switch (id) {
            case R.id.home_page:
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
            case R.id.account:
                if (mAccountFragment == null) {
                    mAccountFragment = new AccountFragment();
                    mAccountFragment.setToolbarMenuOnClickListener(this);
                    transaction.add(R.id.frag_container, mAccountFragment);
                }

                transaction.show(mAccountFragment);
                break;
            case R.id.account_water:
                if (mWaterFragment == null) {
                    mWaterFragment = new WaterFragment();
                    mWaterFragment.setToolbarMenuOnClickListener(this);
                    transaction.add(R.id.frag_container, mWaterFragment);
                }

                transaction.show(mWaterFragment);
                break;
            case R.id.chart:
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
        if (mWaterFragment != null) {
            transaction.hide(mWaterFragment);
        }
        if (mAccountFragment != null) {
            transaction.hide(mAccountFragment);
        }
    }

    /**
     * 点击头像跳转到LoginView
     */
    public void goLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void toolbarMenuOnClick() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}
