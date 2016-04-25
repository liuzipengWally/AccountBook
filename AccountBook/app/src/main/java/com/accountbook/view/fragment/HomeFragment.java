package com.accountbook.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.entity.local.AccountBill;
import com.accountbook.presenter.HomePresenter;
import com.accountbook.presenter.service.SyncService;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.DialogManager;
import com.accountbook.tools.Util;
import com.accountbook.view.activity.EditActivity;
import com.accountbook.view.adapter.HomeListAdapter;
import com.accountbook.view.api.IHomeView;
import com.accountbook.view.api.ToolbarMenuOnClickListener;
import com.accountbook.view.customview.AutoHideFab;
import com.accountbook.view.customview.DoubleClickDomain;
import com.accountbook.view.customview.FoldAppBar;
import com.accountbook.view.customview.HomeListDivider;
import com.avos.avoscloud.AVUser;

import java.lang.reflect.Field;
import java.util.Calendar;
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
    @Bind(R.id.RootLayout)
    CoordinatorLayout mRootLayout;
    private View mLayoutView;

    private Context mContext;

    private int downY;
    private int offsetY;
    private long mStartTime;
    private long mEndTime;
    private boolean mDecorationFlag;

    private HomeListAdapter mAdapter;
    private HomePresenter mPresenter;

    private List<AccountBill> mAccountBills;
    private AccountBill mAccountBillCurr;
    private int mDeletePosition;

    private ToolbarMenuOnClickListener mToolbarMenuOnClickListener;

    public void setToolbarMenuOnClickListener(ToolbarMenuOnClickListener onClickListener) {
        mToolbarMenuOnClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, mLayoutView);
        init();
        registerBroadCastReceiver();

        return mLayoutView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //加载初始数据，该方法会去查询数据，并最终走到下面的LoadData去适配数据到列表中
        bindEvents();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void init() {
        //初始化presenter
        mPresenter = new HomePresenter(this, mContext);

        //初始化下拉刷新控件
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent); //设置控件进度条颜色

        //列表布局设置为垂直的
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        //列表动画使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void registerBroadCastReceiver() {
        LocalBroadcastManager syncBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter syncIntentFilter = new IntentFilter();
        syncIntentFilter.addAction(ConstantContainer.SYNC_URI);
        BroadcastReceiver syncBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.queryAccountBills(mStartTime, mEndTime);
            }
        };
        syncBroadcastManager.registerReceiver(syncBroadCastReceiver, syncIntentFilter);

        LocalBroadcastManager logoutBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter logoutFilter = new IntentFilter();
        logoutFilter.addAction(ConstantContainer.LOGOUT_DONE_URI);
        BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.queryAccountBills(mStartTime, mEndTime);
            }
        };
        logoutBroadcastManager.registerReceiver(logoutReceiver, logoutFilter);
    }


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

        // 反射改变spinner的选择事件，让其可重复选择
        mSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Class<?> clazz = AdapterView.class;
                try {
                    Field field = clazz.getDeclaredField("mOldSelectedPosition");
                    field.setAccessible(true);
                    field.setInt(mSpinner, -1);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar calendar = Calendar.getInstance();

                mEndTime = Util.formatDateNotCh(calendar.getTimeInMillis());
                switch (position) {
                    case 0:
                        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                        mStartTime = Util.formatDateNotCh(calendar.getTimeInMillis());
                        mPresenter.queryAccountBills(mStartTime, mEndTime);
                        break;
                    case 1:
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        mStartTime = Util.formatDateNotCh(calendar.getTimeInMillis());
                        mPresenter.queryAccountBills(mStartTime, mEndTime);
                        break;
                    case 2:
                        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 2);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        mStartTime = Util.formatDateNotCh(calendar.getTimeInMillis());
                        mPresenter.queryAccountBills(mStartTime, mEndTime);
                        break;
                    case 3:
                        DialogManager dialogManager = new DialogManager(mContext);
                        dialogManager.showDateRangePickerDialog(new DialogManager.OnDateRangeSetListener() {
                            @Override
                            public void dateRangeSet(long startMillisecond, long endMillisecond) {
                                mStartTime = Util.formatDateNotCh(startMillisecond);
                                mEndTime = Util.formatDateNotCh(endMillisecond);
                                mPresenter.queryAccountBills(mStartTime, mEndTime);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        //主页列表RecyclerView的滑动事件
//        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                scrollEvent(e);
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
//
//        //appbar的状态事件，可监听折叠，展开的改变
//        mAppBarLayout.setStateChangeListener(new FoldAppBar.OnAppbarStateChangeListener() {
//            @Override
//            public void onStateChange(boolean isFold) {
//                if (!isFold) {
//                    mEditBtn.show();
//                }
//            }
//        });

        //下拉刷新控件的刷新事件
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AVUser.getCurrentUser() != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    mContext.startService(new Intent(mContext, SyncService.class));
                } else {
                    mSpinner.setSelection(0);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                    mStartTime = Util.formatDateNotCh(calendar.getTimeInMillis());
                    mEndTime = Util.formatDateNotCh(System.currentTimeMillis());
                    mPresenter.queryAccountBills(mStartTime, mEndTime);
                }
            }
        });

        //主页floatingactionbutton点击事件
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

//    //RecyclerView滚动的具体处理
//    private void scrollEvent(MotionEvent e) {
//        int y = (int) e.getY();   //每次手指触发onTouchEvent的时候记录手指当前所处屏幕的位置
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downY = y;  //记录下点击时手指的位置
//                break;
//            case MotionEvent.ACTION_MOVE:
//                offsetY = y - downY; //每次移动，用当前手指所在位置去减去 点击下去的时候的位置，得到的就是我们手指移动的距离
//                int height = mAppBarLayout.getHeight();
//                int toolbarHeight = mAppBarLayout.minHeight;
//
//                //appbar的高度>toolbar的高度 并且 <= appbar高度限定的最大值时，我们要让appbar可以根据手指拖动多少距离，放大缩小多少
//                if (height > toolbarHeight && height <= mAppBarLayout.maxHeight) {
//                    ViewGroup.LayoutParams params = mAppBarLayout.getLayoutParams();
//                    //每次滑动，我们的appbar的高度要变为原高度加上我们的滑动距离。
//                    int newHeight = height + offsetY;
//                    params.height = newHeight;
//
//                    //如果得到的新高度会小于toolbar的高度，那么我们就把高度设置为toolbar的高度
//                    if (newHeight < toolbarHeight) {
//                        params.height = toolbarHeight;
//                    } else if (newHeight > mAppBarLayout.maxHeight) {
//                        //如果大于最大高度，那么我们就设置为最大高度
//                        params.height = mAppBarLayout.maxHeight;
//                    }
//                    mAppBarLayout.setLayoutParams(params);
//
//                    //如果滑动距离小于0，代表我们在往上滑动，appbar的折叠状态应该设置为false，否则为往下互动，既设置为true
//                    if (offsetY < 0) {
//                        mAppBarLayout.isFold = false;
//                    } else {
//                        mAppBarLayout.isFold = true;
//                    }
//                }
//
//                //根据手指滑动距离，判断出滑动方向，决定FloatingActionButton 是显示还是隐藏
//                if (offsetY > 10) {
//                    mEditBtn.show();
//                } else if (offsetY < -10) {
//                    mEditBtn.hide();
//                }
//
//                break;
//            case MotionEvent.ACTION_UP:
//                //当手指离开屏幕是，我们需要有个界限值，这个值即是下面的limit，差不多是主页图片高度的一半
//                //也就是说，手指离开屏幕时要半段，如果当前appbar的高度>limit，我需要让appbar完全展开
//                //否则也就需要把他折叠回去
//                int limit = mAppBarLayout.maxHeight - (mAppBarLayout.maxHeight - mAppBarLayout.minHeight) / 2;
//                if (mAppBarLayout.getHeight() > limit && mAppBarLayout.getHeight() < mAppBarLayout.maxHeight) {
//                    mAppBarLayout.unfold();
//                } else if (mAppBarLayout.getHeight() < limit) {
//                    mAppBarLayout.fold();
//                }
//                break;
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (AVUser.getCurrentUser() != null && resultCode == 2) {
            mSwipeRefreshLayout.setRefreshing(true);
            mContext.startService(new Intent(mContext, SyncService.class));
        } else {
            mPresenter.queryAccountBills(mStartTime, mEndTime);
        }
    }

    @Override
    public void showLoadDataFailed() {
        if (mAccountBills != null) {
            mAccountBills.removeAll(mAccountBills);
            mAdapter.notifyDataSetChanged();
        }

        Snackbar.make(mRootLayout, "数据获取失败或无数据", Snackbar.LENGTH_LONG).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.queryAccountBills(mStartTime, mEndTime);
            }
        }).show();
    }

    @Override
    public void LoadAccountBills(List<AccountBill> accountBills) {
        mAccountBills = accountBills;
        mAdapter = new HomeListAdapter(accountBills, mContext); //上面几个参数为查询数据库得到的数据，用来构造适配器
        mRecyclerView.setAdapter(mAdapter); //设置适配器，加载数据到列表

        if (!mDecorationFlag) {
            mRecyclerView.addItemDecoration(new HomeListDivider(mContext));
            mDecorationFlag = true;
        }
        mSwipeRefreshLayout.setRefreshing(false);  //数据加载完，进度条也要停止刷新动画

        bindItemEvent();//item的点击事件必须在这绑定，否则因为数据没有适配，会出错
    }

    private void bindItemEvent() {
        //主页列表RecyclerView每一item的点击事件
        mAdapter.setItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDeletePosition = position;
                mAccountBillCurr = mAccountBills.get(position);
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("id", mAccountBillCurr.getId());
                startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClick(View view, final int position) {
                mDeletePosition = position;
                DialogManager manager = new DialogManager(mContext);
                mAccountBillCurr = mAccountBills.get(position);
                manager.showMenuDialog(new String[]{"删除记录"}, new DialogManager.OnDialogMenuSelectListener() {
                    @Override
                    public void menuSelect(int which) {
                        switch (which) {
                            case 0:
                                mPresenter.deleteAccountBill(mAccountBillCurr.getId());
                                break;
                        }
                    }
                });
            }
        });
    }

    @Override
    public void loadInfoCard(String expend, String income, String balance) {
        mIncomeText.setText(income);
        mExpendText.setText(expend);
        mBalanceText.setText(balance);
        mStatusHintText.setText(getStatus(new Integer(balance)));
    }

    @Override
    public void loadInfoCardFailed() {

    }

    @Override
    public void showLoadingProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void deleteSuccess(final String id) {
        mAdapter.removeItem(mDeletePosition);
        Snackbar.make(mRootLayout, "删除成功", Snackbar.LENGTH_LONG).setAction("恢复", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.recoveryAccountBill(id);
            }
        }).show();
    }

    @Override
    public void deleteFailed(final String id) {
        Snackbar.make(mRootLayout, "删除失败", Snackbar.LENGTH_LONG).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.deleteAccountBill(id);
            }
        }).show();
    }

    @Override
    public void recoverySuccess() {
        mAdapter.addItem(mDeletePosition, mAccountBillCurr);
        Snackbar.make(mRootLayout, "恢复成功", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void recoveryFailed(final String id) {
        Snackbar.make(mRootLayout, "恢复失败", Snackbar.LENGTH_LONG).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.recoveryAccountBill(id);
            }
        }).show();
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
        mLayoutView = null;
    }
}
