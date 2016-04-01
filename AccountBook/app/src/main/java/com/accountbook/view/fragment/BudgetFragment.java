package com.accountbook.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accountbook.R;
import com.accountbook.entity.local.Budget;
import com.accountbook.presenter.BudgetPresenter;
import com.accountbook.presenter.service.SyncService;
import com.accountbook.tools.DialogManager;
import com.accountbook.view.activity.EditBudgetActivity;
import com.accountbook.view.adapter.BudgetListAdapter;
import com.accountbook.view.api.IBudgetView;
import com.accountbook.view.api.ToolbarMenuOnClickListener;
import com.accountbook.view.customview.AutoHideFab;
import com.avos.avoscloud.AVUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BudgetFragment extends Fragment implements IBudgetView {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.BudgetList)
    RecyclerView mBudgetList;
    @Bind(R.id.Refresh)
    SwipeRefreshLayout mRefresh;
    @Bind(R.id.edit_btn)
    AutoHideFab mEditBtn;
    @Bind(R.id.RootLayout)
    CoordinatorLayout mRootLayout;

    private Context mContext;
    private View mLayoutView;

    private List<Budget> mBudgets;
    private Budget mBudgetCurr;

    private BudgetPresenter mPresenter;
    private BudgetListAdapter mAdapter;
    private int mDeletePosition;

    private ToolbarMenuOnClickListener mToolbarMenuOnClickListener;

    public void setToolbarMenuOnClickListener(ToolbarMenuOnClickListener onClickListener) {
        mToolbarMenuOnClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutView = inflater.inflate(R.layout.budget_fragment, container, false);
        ButterKnife.bind(this, mLayoutView);
        initView();
        bindEvents();

        mPresenter = new BudgetPresenter(this, mContext);
        return mLayoutView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.queryBudget();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void bindEvents() {
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AVUser.getCurrentUser() != null) {
                    mContext.startService(new Intent(mContext, SyncService.class));
                }
                mPresenter.queryBudget();
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToolbarMenuOnClickListener != null) {
                    mToolbarMenuOnClickListener.toolbarMenuOnClick();
                }
            }
        });

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditBudgetActivity.class);
                startActivityForResult(intent, 12);
            }
        });
    }

    private void initView() {
        mToolbar.setTitle(R.string.budget);
        mRefresh.setColorSchemeResources(R.color.colorAccent);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mBudgetList.setLayoutManager(layoutManager);
        mBudgetList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.queryBudget();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showLoadDataFailed() {

    }

    @Override
    public void LoadBudget(List<Budget> budgets) {
        mBudgets = budgets;
        mAdapter = new BudgetListAdapter(budgets, mContext);
        mBudgetList.setAdapter(mAdapter);

        itemEventBind();
    }

    private void itemEventBind() {
        mAdapter.setItemClickListener(new BudgetListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, EditBudgetActivity.class);
                intent.putExtra("id", mBudgets.get(position).getId());
                startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClick(View view, int position) {
                mDeletePosition = position;
                DialogManager manager = new DialogManager(mContext);
                mBudgetCurr = mBudgets.get(position);
                manager.showMenuDialog(new String[]{"删除记录"}, new DialogManager.OnDialogMenuSelectListener() {
                    @Override
                    public void menuSelect(int which) {
                        switch (which) {
                            case 0:
                                mPresenter.deleteBudget(mBudgetCurr.getId());
                                break;
                        }
                    }
                });
            }
        });
    }

    @Override
    public void showLoadingProgress() {
        mRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoadingProgress() {
        mRefresh.setRefreshing(false);
    }

    @Override
    public void deleteSuccess(final String id) {
        mAdapter.removeItem(mDeletePosition);
        Snackbar.make(mRootLayout, "删除成功", Snackbar.LENGTH_LONG).setAction("恢复", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.recoveryBudget(id);
            }
        }).show();
    }

    @Override
    public void deleteFailed(final String id) {
        Snackbar.make(mRootLayout, "删除失败", Snackbar.LENGTH_LONG).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.deleteBudget(id);
            }
        }).show();
    }

    @Override
    public void recoverySuccess() {
        mAdapter.addItem(mDeletePosition, mBudgetCurr);
        Snackbar.make(mRootLayout, "恢复成功", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void recoveryFailed(final String id) {
        Snackbar.make(mRootLayout, "恢复失败", Snackbar.LENGTH_LONG).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.recoveryBudget(id);
            }
        }).show();
    }
}
