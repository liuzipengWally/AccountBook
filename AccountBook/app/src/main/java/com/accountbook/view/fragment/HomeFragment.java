package com.accountbook.view.fragment;

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
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.entity.HomeItem;
import com.accountbook.presenter.HomeLoadDataPresenter;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.Util;
import com.accountbook.view.adapter.HomeListAdapter;
import com.accountbook.view.api.IHomeView;
import com.accountbook.view.api.ToolbarMenuOnClickListener;
import com.accountbook.view.customview.AutoHideFab;
import com.accountbook.view.customview.FoldAppBar;
import com.accountbook.view.customview.HomeListDivider;

import org.w3c.dom.Text;

import java.util.List;

public class HomeFragment extends Fragment implements IHomeView {
    private View mLayoutView;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AutoHideFab mEditBtn;
    private CardView mCardView;
    private TextView mIncomeText;
    private TextView mExpendText;
    private TextView mBalanceText;
    private TextView mStatusHintText;

    private int downY;
    private int offsetY;

    private FoldAppBar mAppBarLayout;

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

    private void initView() {
        //初始化toolbar
        mToolbar = (Toolbar) mLayoutView.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.home_fragment_menu);
        mToolbar.setTitle(R.string.home_page);
        mAppBarLayout = (FoldAppBar) mLayoutView.findViewById(R.id.appbar);

        mCardView = (CardView) mLayoutView.findViewById(R.id.home_card);

        mEditBtn = (AutoHideFab) mLayoutView.findViewById(R.id.edit_btn);

        mIncomeText = (TextView) mLayoutView.findViewById(R.id.income_text);
        mExpendText = (TextView) mLayoutView.findViewById(R.id.expend_text);
        mBalanceText = (TextView) mLayoutView.findViewById(R.id.balance_text);
        mStatusHintText = (TextView) mLayoutView.findViewById(R.id.status_hint_text);

        //初始化presenter
        mLoadDataPresenter = new HomeLoadDataPresenter(this);

        //初始化下拉刷新控件
        mSwipeRefreshLayout = (SwipeRefreshLayout) mLayoutView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent); //设置控件进度条颜色

        //初始化主页列表的RecyclerView
        mRecyclerView = (RecyclerView) mLayoutView.findViewById(R.id.home_list);
        //列表布局设置为垂直的
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        //列表动画使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new HomeListDivider(getActivity()));

        //加载初始数据，该方法会去查询数据，并最终走到下面的LoadData去适配数据到列表中
        mLoadDataPresenter.query();
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
            public void onStateChange(boolean state) {
                if (state) {
                    mToolbar.setTitle(R.string.recent_lbl);
                } else {
                    mToolbar.setTitle(R.string.home_page);
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
    }

    //RecyclerView滚动的具体处理
    private void scrollEvent(MotionEvent e) {
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetY = y - downY;
                int height = mAppBarLayout.getHeight();
                int toolbarHeight = mAppBarLayout.minHeight;

                if (height > toolbarHeight && height <= mAppBarLayout.maxHeight) {
                    ViewGroup.LayoutParams params = mAppBarLayout.getLayoutParams();
                    params.height = height + offsetY;
                    if (height + offsetY < toolbarHeight) {
                        params.height = toolbarHeight;
                    } else if (height + offsetY > mAppBarLayout.maxHeight) {
                        params.height = mAppBarLayout.maxHeight;
                    }
                    mAppBarLayout.setLayoutParams(params);

                    if (offsetY < 0) {
                        mAppBarLayout.isFold = false;
                    } else {
                        mAppBarLayout.isFold = true;
                    }
                }

                if (offsetY > 10) {
                    mEditBtn.show();
                } else if (offsetY < -10) {
                    mEditBtn.hide();
                }

                break;
            case MotionEvent.ACTION_UP:
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
    public void LoadData(List<HomeItem> homeItems, String income, String expend, String balance) {
        mIncomeText.setText(income);
        mExpendText.setText(expend);
        mBalanceText.setText(balance);
        mStatusHintText.setText(getStatus(new Integer(balance)));

        mAdapter = new HomeListAdapter(homeItems, getActivity()); //上面几个参数为查询数据库得到的数据，用来构造适配器
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
}
