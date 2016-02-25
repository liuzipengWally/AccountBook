package com.accountbook.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.entity.HomeItem;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.Util;

import java.util.List;

/**
 * Created by liuzipeng on 16/2/25.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private List<HomeItem> mHomeItems;
    private String mIncome;
    private String mExpend;
    private String mBalance;

    private LayoutInflater mInflater;

    private OnItemClickListener mItemClickListener;

    public HomeListAdapter(List<HomeItem> mHomeItems, String mIncome, String mExpend, String mBalance, Context mContext) {
        this.mHomeItems = mHomeItems;
        this.mIncome = mIncome;
        this.mExpend = mExpend;
        this.mBalance = mBalance;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public interface OnItemClickListener {
        /**
         * item点击事件
         *
         * @param view     被点击的View
         * @param position 所点击的View在Item中的下标
         */
        void onItemClick(View view, int position);

        /**
         * item长按事件
         *
         * @param view     被点击的View
         * @param position 所点击的View在Item中的下标
         */
        void onLongClick(View view, int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType != ConstantContainer.HOME_CARD) {
            view = mInflater.inflate(R.layout.home_fragment_recent_item, parent, false);
        } else {
            view = mInflater.inflate(R.layout.home_fragment_card, parent, false);
        }

        return new ViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position != ConstantContainer.HOME_CARD) {
            return ConstantContainer.HOME_ITEM;
        } else {
            return ConstantContainer.HOME_CARD;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position != ConstantContainer.HOME_CARD) {
            settingsItemViews(holder, position - 1);
        } else {
            settingsCardViews(holder);
        }

        bindEvents(holder);
    }

    private void settingsItemViews(ViewHolder holder, int position) {
        HomeItem item = mHomeItems.get(position);

        holder.titleText.setText(item.getCategory());
        holder.accountTypeText.setText(item.getAccountType());
        holder.moneyText.setText(item.getMoney());

        /*
        * 如果position为0，代表是第一个item，那么是一定要显示出 日期的，如果不是就要判断
        * 如果日期和前一个item不同，那么表示这个item是新的一天中的第一条数据，那么就要将这个item的
        * 日期显示出来
        * */
        if (position == 0) {
            holder.weekText.setText(Util.numWeek2strWeek(item.getTime()));
            holder.weekText.setVisibility(View.VISIBLE);
        } else {
            if (item.getTime() != mHomeItems.get(position - 1).getTime()) {
                holder.weekText.setText(Util.numWeek2strWeek(item.getTime()));
                holder.weekText.setVisibility(View.VISIBLE);
            }
        }

        if (item.getMoneyType() == ConstantContainer.INCOME) {
            holder.moneyText.setTextColor(Color.parseColor("#b10909"));
        } else if (item.getMoneyType() == ConstantContainer.EXPEND) {
            holder.moneyText.setTextColor(Color.parseColor("#217c4a"));
        }
    }

    private void settingsCardViews(ViewHolder holder) {
        holder.incomeText.setText(mIncome);
        holder.expendText.setText(mExpend);
        holder.balanceText.setText(mBalance);
        holder.statusHintText.setText(getStatus(new Integer(mBalance)));
    }

    /**
     * 为卡片和item的点击和长按时间绑定上前面写的listener监听，将是事件作为回调抛出去
     *
     * @param holder
     */
    private void bindEvents(final ViewHolder holder) {
        if (mItemClickListener != null) {
            holder.homeCardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mItemClickListener.onItemClick(v, position);
                }
            });

            holder.homeItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mItemClickListener.onItemClick(v, position);
                }
            });

            holder.homeItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mItemClickListener.onItemClick(v, position);
                    return true;
                }
            });
        }
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
    public int getItemCount() {
        return mHomeItems.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //主页卡片中的控件
        LinearLayout homeCardLayout;
        TextView incomeText;
        TextView expendText;
        TextView balanceText;
        TextView statusHintText;

        //主页近期记录item中的控件
        RelativeLayout homeItemLayout;
        TextView weekText;
        TextView titleText;
        TextView accountTypeText;
        TextView moneyText;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case ConstantContainer.HOME_CARD:
                    initHomeCardViews(itemView);
                    break;
                case ConstantContainer.HOME_ITEM:
                    initHomeItemViews(itemView);
                    break;
            }
        }

        private void initHomeItemViews(View itemView) {
            homeItemLayout = (RelativeLayout) itemView.findViewById(R.id.home_item);
            weekText = (TextView) itemView.findViewById(R.id.week_lbl);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            accountTypeText = (TextView) itemView.findViewById(R.id.account_type_text);
            moneyText = (TextView) itemView.findViewById(R.id.money_text);
        }

        private void initHomeCardViews(View itemView) {
            homeCardLayout = (LinearLayout) itemView.findViewById(R.id.home_card);
            incomeText = (TextView) itemView.findViewById(R.id.income_text);
            expendText = (TextView) itemView.findViewById(R.id.expend_text);
            balanceText = (TextView) itemView.findViewById(R.id.balance_text);
            statusHintText = (TextView) itemView.findViewById(R.id.status_hint_text);
        }
    }
}
