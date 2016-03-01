package com.accountbook.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.accountbook.R;
import com.accountbook.entity.HomeItem;
import com.accountbook.presenter.HomeLoadDataPresenter;
import com.accountbook.view.adapter.HomeListAdapter;
import com.accountbook.view.api.IHomeView;
import com.accountbook.view.api.ToolbarMenuOnClickListener;
import com.accountbook.view.customview.HomeListDivider;

import java.util.List;

public class HomeFragment extends Fragment implements IHomeView {
    private View mLayoutView;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private HomeListAdapter mAdapter;

    private HomeLoadDataPresenter mLoadDataPresenter;

    private ToolbarMenuOnClickListener mToolbarMenuOnClickListener;

    public void setToolbarMenuOnClickListener(ToolbarMenuOnClickListener onClickListener) {
        mToolbarMenuOnClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutView = inflater.inflate(R.layout.home_fragment, container, false);
        initView();
        bindEvents();

        return mLayoutView;
    }

    private void bindEvents() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToolbarMenuOnClickListener != null) {
                    mToolbarMenuOnClickListener.toolbarMenuOnClick();
                }
            }
        });

        mAdapter.setItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadDataPresenter.query();
            }
        });
    }

    private void initView() {
        mToolbar = (Toolbar) mLayoutView.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.main_activity_menu);
        mToolbar.setTitle(R.string.home_page);

        mLoadDataPresenter = new HomeLoadDataPresenter(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mLayoutView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mRecyclerView = (RecyclerView) mLayoutView.findViewById(R.id.home_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new HomeListDivider(getActivity()));

        mLoadDataPresenter.query();
    }

    @Override
    public void showLoadDataError() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void LoadData(List<HomeItem> homeItems, String income, String expend, String balance) {
        mAdapter = new HomeListAdapter(homeItems, income, expend, balance, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void toDetailedActivity(int id, int moneyType) {

    }

    @Override
    public void refreshDone() {

    }
}
