package com.accountbook.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.view.api.ToolbarMenuOnClickListener;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    private View mLayoutView;
    private Toolbar mToolbar;
    private Calendar mCalendar;

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
    }

    private void initView() {
        mToolbar = (Toolbar) mLayoutView.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.main_activity_menu);
        mToolbar.setTitle(R.string.home_page);

        mCalendar = Calendar.getInstance();
        TextView monthTextView = (TextView) mLayoutView.findViewById(R.id.monthText);
        monthTextView.setText((mCalendar.get(Calendar.MONTH) + 1) + "");
        TextView yearTextView = (TextView) mLayoutView.findViewById(R.id.yearText);
        yearTextView.setText(" / " + mCalendar.get(Calendar.YEAR) + "");
    }
}
