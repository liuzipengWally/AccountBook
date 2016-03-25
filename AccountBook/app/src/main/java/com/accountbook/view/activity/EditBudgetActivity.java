package com.accountbook.view.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accountbook.R;
import com.accountbook.entity.Budget;
import com.accountbook.entity.Classify;
import com.accountbook.presenter.EditBudgetPresenter;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.Util;
import com.accountbook.view.api.IEditBudgetView;
import com.accountbook.view.customview.AutoHideFab;

import java.text.ParseException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditBudgetActivity extends AppCompatActivity implements IEditBudgetView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.money_edit)
    EditText mMoneyEdit;
    @Bind(R.id.money_type_text)
    TextView mMoneyTypeText;
    @Bind(R.id.description_edit)
    EditText mDescriptionEdit;
    @Bind(R.id.classify_text)
    TextView mClassifyText;
    @Bind(R.id.classify_item)
    RelativeLayout mClassifyItem;
    @Bind(R.id.start_date_text)
    TextView mStartDateText;
    @Bind(R.id.end_date_item)
    RelativeLayout mEndDateItem;
    @Bind(R.id.end_date_text)
    TextView mEndDateText;
    @Bind(R.id.start_date_item)
    RelativeLayout mStartDateItem;
    @Bind(R.id.edit_btn)
    AutoHideFab mEditBtn;
    @Bind(R.id.RootLayout)
    CoordinatorLayout mRootLayout;

    private long mStartDate;
    private long mEndDate;
    private String mClassifyId;
    private String mId;
    private boolean isAlter;

    private EditBudgetPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_budget_activity);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        mPresenter = new EditBudgetPresenter(this);
        initView();
        eventBind();

        if (intent.getExtras() != null) {
            mId = intent.getExtras().getString("id");
            isAlter = true;
            mPresenter.loadAlterBudgetDate(mId);
        } else {
            intent = new Intent(this, ClassifyActivity.class);
            intent.putExtra("type", ConstantContainer.BUDGET_REQUEST);
            startActivityForResult(intent, 0);
        }
    }

    private void eventBind() {
        mClassifyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditBudgetActivity.this, ClassifyActivity.class);
                intent.putExtra("type", ConstantContainer.BUDGET_REQUEST);
                startActivityForResult(intent, 0);
            }
        });


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mStartDateItem.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(EditBudgetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        //使用日期格式化类，将calendar.getTimeInMillis()获取到的日期毫秒值转换为"yyyy年MM月dd日"形式的字符串
                        mStartDate = Util.formatDateNotCh(calendar.getTimeInMillis());
                        mStartDateText.setText(Util.formatDateUseCh(calendar.getTimeInMillis()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        mEndDateItem.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(EditBudgetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        //使用日期格式化类，将calendar.getTimeInMillis()获取到的日期毫秒值转换为"yyyy年MM月dd日"形式的字符串
                        mEndDate = Util.formatDateNotCh(calendar.getTimeInMillis());
                        mEndDateText.setText(Util.formatDateUseCh(calendar.getTimeInMillis()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAlter) {
                    mPresenter.saveBudget();
                } else {
                    mPresenter.alterBudget();
                }
            }
        });
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mStartDateText.setText(Util.formatDateUseCh(System.currentTimeMillis()));
        mStartDate = Util.formatDateNotCh(System.currentTimeMillis());
        mEndDateText.setText(Util.formatDateUseCh(System.currentTimeMillis()));
        mEndDate = Util.formatDateNotCh(System.currentTimeMillis());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            mClassifyId = bundle.getString("id");
            mClassifyText.setText(bundle.getString("classify"));
        }
    }

    @Override
    public Budget getBudgetInfo() {
        Budget budget = new Budget();
        if (isAlter) {
            budget.setId(mId);
        }
        budget.setClassifyId(mClassifyId);
        budget.setCountMoney(new Integer(mMoneyEdit.getText().toString()));
        budget.setStartTime(mStartDate);
        budget.setEndTime(mEndDate);
        budget.setDescription(mDescriptionEdit.getText().toString());


        return budget;
    }

    @Override
    public void validateFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveFailed() {
        Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveSuccess() {
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        setResult(12);
        finish();
    }

    @Override
    public void loadAlterBudgetDate(Budget budget) {
        mClassifyText.setText(budget.getClassify());
        mMoneyEdit.setText(budget.getCountMoney() + "");
        mDescriptionEdit.setText(budget.getDescription());
        mClassifyId = budget.getClassifyId();
        try {
            mStartDateText.setText(Util.formatDateUseCh(Util.parseMsNotCh(budget.getStartTime() + "")));
            mEndDateText.setText(Util.formatDateUseCh(Util.parseMsNotCh(budget.getEndTime() + "")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterSuccess() {
        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        setResult(12);
        finish();
    }

    @Override
    public void alterFailed() {
        Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
    }
}
