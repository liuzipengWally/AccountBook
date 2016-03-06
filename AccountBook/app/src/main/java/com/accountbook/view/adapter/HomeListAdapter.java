package com.accountbook.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.entity.AccountBill;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.view.customview.CircleIcon;

import java.util.List;

/**
 * 主页近期记录的适配器
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private List<AccountBill> mAccountBills;
    private LayoutInflater mInflater;

    private OnItemClickListener mItemClickListener;

    public HomeListAdapter(List<AccountBill> mAccountBills, Context mContext) {
        this.mAccountBills = mAccountBills;
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
        AccountBill accountBill = mAccountBills.get(position);

        holder.titleText.setText(accountBill.getCategory());
        holder.accountTypeText.setText(accountBill.getAccountType());
        holder.moneyText.setText(accountBill.getMoney());
        holder.circleIcon.setColor(accountBill.getColor());
        holder.circleIcon.setIconResId(accountBill.getIconResId());


        if (accountBill.getMoneyType() == ConstantContainer.INCOME) {
            holder.moneyText.setTextColor(Color.parseColor("#217c4a"));
        } else if (accountBill.getMoneyType() == ConstantContainer.EXPEND) {
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
        return mAccountBills.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //主页近期记录item中的控件
        RelativeLayout homeItemLayout;
        CircleIcon circleIcon;
        TextView titleText;
        TextView accountTypeText;
        TextView moneyText;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            homeItemLayout = (RelativeLayout) itemView.findViewById(R.id.home_item);
            circleIcon = (CircleIcon) itemView.findViewById(R.id.circle_icon);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            accountTypeText = (TextView) itemView.findViewById(R.id.account_type_text);
            moneyText = (TextView) itemView.findViewById(R.id.money_text);
        }
    }
}
