package com.accountbook.view.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.accountbook.entity.Role;
import com.accountbook.presenter.AddRecordPresenter;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.view.api.IAddView;
import com.accountbook.view.customview.AutoHideFab;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddActivity extends BaseActivity implements IAddView {
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


    private AddRecordPresenter mPresenter;

    private List<Role> mRoles;
    private String[] mAccounts;
    private String mRoleId;
    private String mAccount;
    private String mDate;
    private String mClassifyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(AddActivity.this, ClassifyActivity.class);
        Bundle bundle = new Bundle();
        startActivityForResult(intent, 1);

        setContentView(R.layout.add_activity);
        ButterKnife.bind(this);
        mPresenter = new AddRecordPresenter(this);
        mPresenter.queryRole();

        initView();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        mDateText.setText(mDate = dateFormat.format(System.currentTimeMillis()));
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
                mRoleId = mRoles.get(position).getRole();
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
                Intent intent = new Intent(AddActivity.this, ClassifyActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //日期item点击事件
        mDateItem.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        //使用日期格式化类，将calendar.getTimeInMillis()获取到的日期毫秒值转换为"yyyy年MM月dd日"形式的字符串
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                        mDate = dateFormat.format(calendar.getTimeInMillis());
                        mDateText.setText(mDate);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        //保存按钮的点击事件
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveRecord();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            mClassifyId = bundle.getString("id");
            int type = bundle.getInt("type");
            handleType(type);//根据传回来的数据中的type判断是否需要用户填写债权人或债务人
            mClassifyText.setText(bundle.getString("classify"));
        } else {
            if (requestCode == 1) {  //如果没有数据，并且请求码为1，那么证明这个跳转是一开始点击记录按钮跳转到的那个分类界面
                finish(); //此时，我们应该直接把这个activity关闭
            }
        }
    }

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

    @Override
    public void loadRole(List<Role> roles) {
        mRoles = roles; //把获取到的 role集合先置于全局，用于之后获得ID

        //循环获取到所有的role明
        String[] roleArr = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            roleArr[i] = roles.get(i).getRole();
        }
        mRoleSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, roleArr));
    }

    @Override
    public int getMoney() {
        return new Integer(mMoneyEdit.getText().toString());
    }

    @Override
    public String getDescription() {
        return mDescriptionEdit.getText().toString();
    }

    @Override
    public String getBorrowing() {
        return mBorrowingEdit.getText().toString();
    }

    @Override
    public String getCreateTime() {
        return mDate;
    }

    @Override
    public String getAccount() {
        return mAccount;
    }

    @Override
    public String getRoleId() {
        return mRoleId;
    }

    @Override
    public String getClassifyId() {
        return mClassifyId;
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
    }
}
