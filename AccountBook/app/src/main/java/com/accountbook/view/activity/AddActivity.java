package com.accountbook.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.view.customview.AutoHideFab;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.money_edit)
    EditText mMoneyEdit;
    @Bind(R.id.money_type_text)
    TextView mMoneyTypeText;
    @Bind(R.id.description_edit)
    EditText mDescriptionEdit;
    @Bind(R.id.borrowing_edit)
    EditText mBorrowingEdit;
    @Bind(R.id.classify_text)
    TextView mClassifyText;
    @Bind(R.id.classify_item)
    RelativeLayout mClassifyItem;
    @Bind(R.id.account_spinner)
    Spinner mAccountSpinner;
    @Bind(R.id.role_spinner)
    Spinner mRoleSpinner;
    @Bind(R.id.date_text)
    TextView mDateText;
    @Bind(R.id.date_item)
    RelativeLayout mDateItem;
    @Bind(R.id.edit_btn)
    AutoHideFab mDoneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mToolbar.setTitle(R.string.quick_add);
        setSupportActionBar(mToolbar);
    }
}
