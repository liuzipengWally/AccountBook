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
