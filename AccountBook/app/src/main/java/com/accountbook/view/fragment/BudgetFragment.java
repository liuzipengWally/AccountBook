package com.accountbook.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accountbook.R;
import com.accountbook.view.api.ToolbarMenuOnClickListener;

public class BudgetFragment extends Fragment {
    private View mLayoutView;
    private Toolbar mToolbar;

    private ToolbarMenuOnClickListener mToolbarMenuOnClickListener;

    public void setToolbarMenuOnClickListener(ToolbarMenuOnClickListener onClickListener) {
        mToolbarMenuOnClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutView = inflater.inflate(R.layout.account_fragment, container, false);

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
    }

    private void initView() {
        mToolbar = (Toolbar) mLayoutView.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.home_fragment_menu);
        mToolbar.setTitle(R.string.account);
    }
}
