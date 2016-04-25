package com.accountbook.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.accountbook.R;
import com.accountbook.entity.local.ChartData;
import com.accountbook.presenter.ChartPresenter;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.DialogManager;
import com.accountbook.tools.Util;
import com.accountbook.view.adapter.ToolbarSpinnerAdapter;
import com.accountbook.view.api.IChartView;
import com.accountbook.view.api.ToolbarMenuOnClickListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChartFragment extends Fragment implements IChartView {
    @Bind(R.id.chart_spinner)
    Spinner mChartSpinner;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ViewPager)
    ViewPager mViewPager;

    private PieChart mIncomePieChart;
    private PieChart mExpendPieChart;
    private CardView mCardView;

    private List<View> mViews;

    private View mLayoutView;

    private ChartPresenter mPresenter;

    private Context mContext;
    private long mStartTime;
    private long mEndTime;

    private ToolbarMenuOnClickListener mToolbarMenuOnClickListener;

    public void setToolbarMenuOnClickListener(ToolbarMenuOnClickListener onClickListener) {
        mToolbarMenuOnClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutView = inflater.inflate(R.layout.chart_fragment, container, false);
        ButterKnife.bind(this, mLayoutView);
        initView();
        registerBroadCastReceiver();
        bindEvents();
        mPresenter = new ChartPresenter(this);
        return mLayoutView;
    }

    private void registerBroadCastReceiver() {
        LocalBroadcastManager syncBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter syncIntentFilter = new IntentFilter();
        syncIntentFilter.addAction(ConstantContainer.SYNC_URI);//建议把它写一个公共的变量，这里方便阅读就不写了。
        BroadcastReceiver syncBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.loadClassifyPercent(ConstantContainer.EXPEND, mStartTime, mEndTime);
                mPresenter.loadClassifyPercent(ConstantContainer.INCOME, mStartTime, mEndTime);
            }
        };
        syncBroadcastManager.registerReceiver(syncBroadCastReceiver, syncIntentFilter);


        LocalBroadcastManager logoutBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter logoutFilter = new IntentFilter();
        logoutFilter.addAction(ConstantContainer.LOGOUT_DONE_URI);
        BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.loadClassifyPercent(ConstantContainer.EXPEND, mStartTime, mEndTime);
                mPresenter.loadClassifyPercent(ConstantContainer.INCOME, mStartTime, mEndTime);
            }
        };

        logoutBroadcastManager.registerReceiver(logoutReceiver, logoutFilter);
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


        //反射改变spinner的选择事件，让其可重选
        mChartSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Class<?> clazz = AdapterView.class;
                try {
                    Field field = clazz.getDeclaredField("mOldSelectedPosition");
                    field.setAccessible(true);
                    field.setInt(mChartSpinner, -1);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        mChartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar calendar = Calendar.getInstance();

                mEndTime = Util.formatDateNotCh(calendar.getTimeInMillis());
                switch (position) {
                    case 0:
                        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                        mStartTime = Util.formatDateNotCh(calendar.getTimeInMillis());
                        mPresenter.loadClassifyPercent(ConstantContainer.EXPEND, mStartTime, mEndTime);
                        mPresenter.loadClassifyPercent(ConstantContainer.INCOME, mStartTime, mEndTime);
                        break;
                    case 1:
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        mStartTime = Util.formatDateNotCh(calendar.getTimeInMillis());
                        mPresenter.loadClassifyPercent(ConstantContainer.EXPEND, mStartTime, mEndTime);
                        mPresenter.loadClassifyPercent(ConstantContainer.INCOME, mStartTime, mEndTime);
                        break;
                    case 2:
                        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 2);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        mStartTime = Util.formatDateNotCh(calendar.getTimeInMillis());
                        mPresenter.loadClassifyPercent(ConstantContainer.EXPEND, mStartTime, mEndTime);
                        mPresenter.loadClassifyPercent(ConstantContainer.INCOME, mStartTime, mEndTime);
                        break;
                    case 3:
                        DialogManager dialogManager = new DialogManager(mContext);
                        dialogManager.showDateRangePickerDialog(new DialogManager.OnDateRangeSetListener() {
                            @Override
                            public void dateRangeSet(long startMillisecond, long endMillisecond) {
                                mStartTime = Util.formatDateNotCh(startMillisecond);
                                mEndTime = Util.formatDateNotCh(endMillisecond);
                                mPresenter.loadClassifyPercent(ConstantContainer.EXPEND, mStartTime, mEndTime);
                                mPresenter.loadClassifyPercent(ConstantContainer.INCOME, mStartTime, mEndTime);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        mChartSpinner.setAdapter(new ToolbarSpinnerAdapter(mContext, getResources().getStringArray(R.array.home_page_spinner)));

        mViews = new ArrayList<>();

        mViews.add(LayoutInflater.from(mContext).inflate(R.layout.chart_fragment_page, null));
        mViews.add(LayoutInflater.from(mContext).inflate(R.layout.chart_fragment_page, null));

        mExpendPieChart = (PieChart) mViews.get(0).findViewById(R.id.PieChart);
        mIncomePieChart = (PieChart) mViews.get(1).findViewById(R.id.PieChart);
        mCardView = (CardView) mViews.get(0).findViewById(R.id.CardView);

        mViewPager.setAdapter(new pagerAdapter());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        mChartSpinner.setSelection(0);
        Calendar calendar = Calendar.getInstance();
        mEndTime = Util.formatDateNotCh(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        mStartTime = Util.formatDateNotCh(calendar.getTimeInMillis());
        mPresenter.loadClassifyPercent(ConstantContainer.EXPEND, mStartTime, mEndTime);
        mPresenter.loadClassifyPercent(ConstantContainer.INCOME, mStartTime, mEndTime);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void loadClassifyPercent(List<ChartData> chartDatas, int type) {
        PieData pieData = constructData(chartDatas);

        if (type == ConstantContainer.EXPEND) {
            initChart(mExpendPieChart);
            mExpendPieChart.setData(pieData);
        } else {
            initChart(mIncomePieChart);
            mIncomePieChart.setData(pieData);
        }
    }

    private PieData constructData(List<ChartData> chartDatas) {
        ArrayList<String> xValues = new ArrayList<>();  //xVals用来表示每个饼块上的内容
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Entry> yValues = new ArrayList<>();  //yVals用来表示封装每个饼块的实际数据
        for (int i = 0; i < chartDatas.size(); i++) {
            xValues.add(chartDatas.get(i).getClassify());
            yValues.add(new Entry(chartDatas.get(i).getPercent(), i));
            colors.add(Color.parseColor(chartDatas.get(i).getColor()));
        }

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "分类示意"/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }

    @Override
    public void loadClassifyPercentFailed() {
//        mIncomePieChart.clearValues();
//        mExpendPieChart.clearValues();
    }

    class pagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            ((ViewPager) container).removeView(view);
            view = null;
        }
    }

    private void initChart(PieChart chart) {
        chart.setDrawSliceText(false);
        chart.setUsePercentValues(true);
        chart.setDrawSlicesUnderHole(true);
        chart.setHoleRadius(60f);  //半径
        chart.setTransparentCircleRadius(64f); // 半透明圈
        chart.setCenterText("支出");  //饼状图中间的文字
        chart.setDrawCenterText(true);  //饼状图中间可以添加文字
        chart.setDrawHoleEnabled(true);
        chart.setRotationEnabled(false);
        chart.setRotationAngle(90); // 初始旋转角度
        Legend legend = chart.getLegend();  //设置比例图
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);  //下方居中显示
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(5f);
        chart.animateXY(1000, 1000);  //设置动画
        chart.setDescription("收支占比饼图");
    }
}
