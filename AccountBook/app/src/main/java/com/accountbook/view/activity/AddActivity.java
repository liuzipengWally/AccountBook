package com.accountbook.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.accountbook.R;

public class AddActivity extends BaseActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.quick_add);
        setSupportActionBar(mToolbar);
    }
}
