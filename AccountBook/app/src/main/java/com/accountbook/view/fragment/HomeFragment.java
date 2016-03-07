package com.accountbook.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.accountbook.R;
import com.accountbook.entity.AccountBill;
import com.accountbook.presenter.HomeLoadDataPresenter;
import com.accountbook.view.activity.AddActivity;
import com.accountbook.view.adapter.HomeListAdapter;
import com.accountbook.view.adapter.SpinnerAdapter;
import com.accountbook.view.api.IHomeView;
import com.accountbook.view.api.ToolbarMenuOnClickListener;
import com.accountbook.view.customview.AutoHideFab;
import com.accountbook.view.customview.DoubleClickDomain;
import com.accountbook.view.customview.FoldAppBar;
import com.accountbook.view.customview.HomeListDivider;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements IHomeView {
    @Bind(R.id.home_spinner)
    Spinner mSpinner;
    @Bind(R.id.touch_domain)
    DoubleClickDomain mDoubleClickDomain;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.income_text)
    TextView mIncomeText;
    @Bind(R.id.expend_text)
    TextView mExpendText;
    @Bind(R.id.balance_text)
    TextView mBalanceText;
    @Bind(R.id.status_hint_text)
    TextView mStatusHintText;
    @Bind(R.id.home_card)
    CardView mCardView;
    @Bind(R.id.appbar)
    FoldAppBar mAppBarLayout;
    @Bind(R.id.home_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.edit_btn)
    AutoHideFab mEditBtn;

    private View mLayoutView;

    private String[] mSpinnerTitle;

    private Context mContext;

    private int downY;
    private int offsetY;

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
        ButterKnife.bind(this, mLayoutView);
        initView();
        return mLayoutView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSwipeRefreshLayout.setRefreshing(true);
        //加载初始数据，该方法会去查询数据，并最终走到下面的LoadData去适配数据到列表中
        mLoadDataPresenter.query();
        bindEvents();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initView() {
        //初始化toolbar
        mToolbar.inflateMenu(R.menu.home_fragment_menu);

        mSpinnerTitle = getResources().getStringArray(R.array.home_page_spinner);
        mSpinner.setAdapter(new SpinnerAdapter(mContext, mSpinnerTitle));

        //初始化presenter
        mLoadDataPresenter = new HomeLoadDataPresenter(this);

        //初始化下拉刷新控件
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent); //设置控件进度条颜色

        //列表布局设置为垂直的
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        //列表动画使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new HomeListDivider(mContext));
    }

    //// TODO: 16/2/29  事件绑定方法，所有事件均集中到这个方法中
    private void bindEvents() {
        //toolbar上 汉堡条菜单按钮的点击事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToolbarMenuOnClickListener != null) {
                    mToolbarMenuOnClickListener.toolbarMenuOnClick();
                }
            }
        });

        mDoubleClickDomain.setOnDoubleClickListener(new DoubleClickDomain.OnDoubleClickListener() {
            @Override
            public void doubleClick(View view) {
                if (mAppBarLayout.isFold) {
                    mAppBarLayout.unfold();
                } else {
                    mAppBarLayout.fold();
                }
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, mSpinnerTitle[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //主页列表RecyclerView的滑动事件
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                scrollEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        //appbar的状态事件，可监听折叠，展开的改变
        mAppBarLayout.setStateChangeListener(new FoldAppBar.OnAppbarStateChangeListener() {
            @Override
            public void onStateChange(boolean isFold) {
                if (!isFold) {
                    mEditBtn.show();
                }
            }
        });

        //主页列表RecyclerView每一item的点击事件
        mAdapter.setItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });

        //下拉刷新控件的刷新事件
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadDataPresenter.query();
            }
        });

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    //RecyclerView滚动的具体处理
    private void scrollEvent(MotionEvent e) {
        int y = (int) e.getY();   //每次手指触发onTouchEvent的时候记录手指当前所处屏幕的位置
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = y;  //记录下点击时手指的位置
                break;
            case MotionEvent.ACTION_MOVE:
                offsetY = y - downY; //每次移动，用当前手指所在位置去减去 点击下去的时候的位置，得到的就是我们手指移动的距离
                int height = mAppBarLayout.getHeight();
                int toolbarHeight = mAppBarLayout.minHeight;

                //appbar的高度>toolbar的高度 并且 <= appbar高度限定的最大值时，我们要让appbar可以根据手指拖动多少距离，放大缩小多少
                if (height > toolbarHeight && height <= mAppBarLayout.maxHeight) {
                    ViewGroup.LayoutParams params = mAppBarLayout.getLayoutParams();
                    //每次滑动，我们的appbar的高度要变为原高度加上我们的滑动距离。
                    int newHeight = height + offsetY;
                    params.height = newHeight;

                    //如果得到的新高度会小于toolbar的高度，那么我们就把高度设置为toolbar的高度
                    if (newHeight < toolbarHeight) {
                        params.height = toolbarHeight;
                    } else if (newHeight > mAppBarLayout.maxHeight) {
                        //如果大于最大高度，那么我们就设置为最大高度
                        params.height = mAppBarLayout.maxHeight;
                    }
                    mAppBarLayout.setLayoutParams(params);

                    //如果滑动距离小于0，代表我们在往上滑动，appbar的折叠状态应该设置为false，否则为往下互动，既设置为true
                    if (offsetY < 0) {
                        mAppBarLayout.isFold = false;
                    } else {
                        mAppBarLayout.isFold = true;
                    }
                }

                //根据手指滑动距离，判断出滑动方向，决定FloatingActionButton 是显示还是隐藏
                if (offsetY > 10) {
                    mEditBtn.show();
                } else if (offsetY < -10) {
                    mEditBtn.hide();
                }

                break;
            case MotionEvent.ACTION_UP:
                //当手指离开屏幕是，我们需要有个界限值，这个值即是下面的limit，差不多是主页图片高度的一半
                //也就是说，手指离开屏幕时要半段，如果当前appbar的高度>limit，我需要让appbar完全展开
                //否则也就需要把他折叠回去
                int limit = mAppBarLayout.maxHeight - (mAppBarLayout.maxHeight - mAppBarLayout.minHeight) / 2;
                if (mAppBarLayout.getHeight() > limit) {
                    mAppBarLayout.unfold();
                } else {
                    mAppBarLayout.fold();
                }
                break;
        }
    }

    @Override
    public void showLoadDataError() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void LoadData(List<AccountBill> accountBills, String income, String expend, String balance) {
        mIncomeText.setText(income);
        mExpendText.setText(expend);
        mBalanceText.setText(balance);
        mStatusHintText.setText(getStatus(new Integer(balance)));

        mAdapter = new HomeListAdapter(accountBills, mContext); //上面几个参数为查询数据库得到的数据，用来构造适配器
        mRecyclerView.setAdapter(mAdapter); //设置适配器，加载数据到列表

        mSwipeRefreshLayout.setRefreshing(false);  //数据加载完，进度条也要停止刷新动画
    }

    @Override
    public void toDetailedActivity(int id, int moneyType) {

    }

    @Override
    public void refreshDone() {

    }

    private String getStatus(Integer balance) {
        if (balance < 500 && balance > 0) {
            return "这个月预算接近枯竭，请合理开销";
        } else if (balance < 0) {
            return "这个月预算已经超支，我希望你还有救(U •́ .̫ •̀ U)";
        }

        return "这个月预算充足，请放心使用＾＾";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
