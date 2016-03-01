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
import com.accountbook.view.customview.CircleText;

import java.util.List;

/**
 * 主页近期记录的适配器
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private List<HomeItem> mHomeItems;
    private LayoutInflater mInflater;

    private OnItemClickListener mItemClickListener;

    public HomeListAdapter(List<HomeItem> mHomeItems, Context mContext) {
        this.mHomeItems = mHomeItems;
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
        View view = mInflater.inflate(R.layout.home_fragment_recent_item, parent, false);

        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        settingsItemViews(holder, position);

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
            holder.moneyText.setTextColor(Color.parseColor("#217c4a"));
        } else if (item.getMoneyType() == ConstantContainer.EXPEND) {
            holder.moneyText.setTextColor(Color.parseColor("#b10909"));
        }
    }

    /**
     * 为卡片和item的点击和长按时间绑定上前面写的listener监听，将是事件作为回调抛出去
     *
     * @param holder
     */
    private void bindEvents(final ViewHolder holder) {
        if (mItemClickListener != null) {
            if (holder.homeItemLayout != null) {
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
    }

    @Override
    public int getItemCount() {
        return mHomeItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //主页近期记录item中的控件
        RelativeLayout homeItemLayout;
        CircleText weekText;
        TextView titleText;
        TextView accountTypeText;
        TextView moneyText;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            homeItemLayout = (RelativeLayout) itemView.findViewById(R.id.home_item);
            weekText = (CircleText) itemView.findViewById(R.id.week_lbl);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            accountTypeText = (TextView) itemView.findViewById(R.id.account_type_text);
            moneyText = (TextView) itemView.findViewById(R.id.money_text);
        }
    }
}
