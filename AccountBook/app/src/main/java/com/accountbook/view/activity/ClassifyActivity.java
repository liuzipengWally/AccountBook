package com.accountbook.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.accountbook.R;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.view.fragment.ClassifyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClassifyActivity extends AppCompatActivity {
    @Bind(R.id.Toolbar)
    Toolbar mToolbar;
    @Bind(R.id.TabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.ViewPager)
    ViewPager mViewPager;

    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classify_activity);
        ButterKnife.bind(this);

        init();
        eventBind();
    }

    private void init() {
        mToolbar.setTitle(R.string.classify);
        setSupportActionBar(mToolbar);

        mFragments = new ArrayList<>();
        mFragments.add(ClassifyFragment.newInstance(ConstantContainer.EXPEND));
        mFragments.add(ClassifyFragment.newInstance(ConstantContainer.INCOME));
        mFragments.add(ClassifyFragment.newInstance(ConstantContainer.BORROW));
        mFragments.add(ClassifyFragment.newInstance(ConstantContainer.LEND));

        ClassifyAdapter adapter = new ClassifyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);
        //初始化顶部tab
        mTabLayout.getTabAt(0).setText(R.string.expend);
        mTabLayout.getTabAt(1).setText(R.string.income);
        mTabLayout.getTabAt(2).setText(R.string.borrow);
        mTabLayout.getTabAt(3).setText(R.string.lend);
    }

    private void eventBind() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.classify_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //点击添加分类菜单，跳转到分类编辑activity
            case R.id.add_classify:
                Intent intent = new Intent(ClassifyActivity.this, EditClassifyActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class ClassifyAdapter extends FragmentPagerAdapter {
        public ClassifyAdapter(FragmentManager fm) {
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
