package com.accountbook.view.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.accountbook.R;
import com.accountbook.entity.local.Record;
import com.accountbook.entity.local.Role;
import com.accountbook.presenter.EditRecordPresenter;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.Util;
import com.accountbook.view.api.IEditView;
import com.accountbook.view.customview.AutoHideFab;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditActivity extends AppCompatActivity implements IEditView {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.money_edit)
    EditText mMoneyEdit;
    @Bind(R.id.money_type_text)
    TextView mMoneyTypeText;
    @Bind(R.id.description_edit)
    EditText mDescriptionEdit;
    @Bind(R.id.borrowing_item)
    RelativeLayout mBorrowingItem;
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
    @Bind(R.id.RootLayout)
    CoordinatorLayout mRootLayout;

    private boolean isAlter;
    private EditRecordPresenter mPresenter;

    private List<Role> mRoles;
    private String[] mAccounts;
    private String mRoleId;
    private String mAccount;
    private long mDate;
    private String mClassifyId;
    private int mType;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            mId = intent.getExtras().getString("id");
            isAlter = true;
        } else {
            //从主页跳转过来后，在这边布局加载前，我们要先跳转到分类选择页面，要求用户选择记录的分类。
            //这么做是为了防止用户未选择分类这一重要数据造成不好的体验。
            intent = new Intent(EditActivity.this, ClassifyActivity.class);
            startActivityForResult(intent, 1);
        }

        setContentView(R.layout.edit_activity);
        ButterKnife.bind(this);
        mPresenter = new EditRecordPresenter(this);
        initView();
        mPresenter.queryRole();
        bindEvent();
    }

    private void initView() {
        //初始化toolbar
        mToolbar.setTitle(R.string.record);
        setSupportActionBar(mToolbar);

        //初始化账户源数据
        mAccounts = getResources().getStringArray(R.array.money_source);
        mAccountSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mAccounts));

        //初始化日期
        Calendar calendar = Calendar.getInstance();
        mDate = Util.formatDateNotCh(calendar.getTimeInMillis());
        mDateText.setText(Util.formatDateUseCh(calendar.getTimeInMillis()));
    }

    private void bindEvent() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //角色下拉列表的选择事件
        mRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mRoleId = mRoles.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //账户源下拉列表选择事件
        mAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAccount = mAccounts[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //分类Item 点击事件
        mClassifyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, ClassifyActivity.class);
                intent.putExtra("type", ConstantContainer.EDIT_RECROD_REQUEST);
                startActivityForResult(intent, 0);
            }
        });

        //日期item点击事件
        mDateItem.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);

                        //使用日期格式化类，将calendar.getTimeInMillis()获取到的日期毫秒值转换为"yyyy年MM月dd日"形式的字符串
                        mDate = Util.formatDateNotCh(calendar.getTimeInMillis());
                        mDateText.setText(Util.formatDateUseCh(calendar.getTimeInMillis()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        //保存按钮的点击事件
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAlter) {
                    mPresenter.saveRecord();
                } else {
                    mPresenter.alterRecord();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            mClassifyId = bundle.getString("id");
            mType = bundle.getInt("type");
            handleType(mType);//根据传回来的数据中的type判断是否需要用户填写债权人或债务人
            mClassifyText.setText(bundle.getString("classify"));
        } else {
            if (requestCode == 1) {  //如果没有数据，并且请求码为1，那么证明这个跳转是一开始点击记录按钮跳转到的那个分类界面
                finish(); //此时，我们应该直接把这个activity关闭
            }
        }
    }

    /**
     * 根据type，对UI进行一些处理，比如如果是借出或者借入，那么我们需要用户可以填写债权人或债务人
     *
     * @param type 金额分类的分类
     */
    private void handleType(int type) {
        switch (type) {
            case ConstantContainer.BORROW:
                mBorrowingItem.setVisibility(View.VISIBLE);
                mBorrowingEdit.setHint(R.string.add_creditor);
                break;
            case ConstantContainer.LEND:
                mBorrowingItem.setVisibility(View.VISIBLE);
                mBorrowingEdit.setHint(R.string.add_debtor);
                break;
            default:
                mBorrowingItem.setVisibility(View.GONE);
                break;
        }
    }

    private void handleType(String borrowName) {
        if (!borrowName.equals("")) {
            mBorrowingItem.setVisibility(View.VISIBLE);
        } else {
            mBorrowingItem.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadRole(List<Role> roles) {
        mRoles = roles; //把获取到的 role集合先置于全局，用于之后获得ID

        //循环获取到所有的role明
        String[] roleArr = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            roleArr[i] = roles.get(i).getRole();
        }
        mRoleSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, roleArr));

        if (isAlter) {
            mPresenter.loadAlterRecordDate(mId);
        }
    }

    @Override
    public Record getRecordInfo() {
        Record record = new Record();
        if (isAlter) {
            record.setId(mId);
        }
        record.setMoney(getMoney());
        record.setDescription(mDescriptionEdit.getText().toString());
        record.setBorrowName(mBorrowingEdit.getText().toString());
        record.setClassifyId(mClassifyId);
        record.setAccount(mAccount);
        record.setRoleId(mRoleId);
        record.setRecordMs(mDate);

        return record;
    }

    public int getMoney() {
        if (!mMoneyEdit.getText().toString().equals("")) {
            if (mType == ConstantContainer.EXPEND) {
                return -new Integer(mMoneyEdit.getText().toString());
            } else {
                return new Integer(mMoneyEdit.getText().toString());
            }
        }

        return 0;
    }

    @Override
    public void loadRoleFailed() {
        Snackbar.make(mRootLayout, "数据初始化失败,请重试", Snackbar.LENGTH_LONG).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.queryRole();
            }
        });
    }

    @Override
    public void validateFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveFailed() {
        Snackbar.make(mRootLayout, "保存失败，请重试", Snackbar.LENGTH_LONG).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveRecord();
            }
        });
    }

    @Override
    public void saveSuccess() {
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        setResult(11);
        finish();
    }

    @Override
    public void loadAlterRecordDate(Record record) {
        if (record.getMoney() < 0) {
            mType = ConstantContainer.EXPEND;
        }

        mMoneyEdit.setText(Math.abs(record.getMoney()) + "");
        mDescriptionEdit.setText(record.getDescription());
        handleType(record.getBorrowName());
        mBorrowingEdit.setText(record.getBorrowName());
        mClassifyText.setText(record.getClassify());

        for (int i = 0; i < mAccounts.length; i++) {
            if (mAccounts[i].equals(record.getAccount())) {
                mAccountSpinner.setSelection(i);
                mAccount = mAccounts[i];
            }
        }

        for (int i = 0; i < mRoles.size(); i++) {
            if (mRoles.get(i).getRole().equals(record.getAccount())) {
                mRoleSpinner.setSelection(i);
                mRoleId = mRoles.get(i).getId();
            }
        }

        mDate = record.getRecordMs();
        try {
            long ms = Util.parseMsNotCh(record.getRecordMs() + "");
            mDateText.setText(Util.formatDateUseCh(ms));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mClassifyId = record.getClassifyId();
        mRoleId = record.getRoleId();
    }

    @Override
    public void alterSuccess() {
        Toast.makeText(this, "修改完成", Toast.LENGTH_SHORT).show();
        setResult(11);
        finish();
    }

    @Override
    public void alterFailed() {
        Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
    }
}
