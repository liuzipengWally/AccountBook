package com.accountbook.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.entity.Budget;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.Util;
import com.accountbook.view.customview.CircleIcon;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.text.ParseException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主页近期记录的适配器
 */
public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.ViewHolder> {
    private List<Budget> mBudgets;
    private LayoutInflater mInflater;
    private Context mContext;

    private OnItemClickListener mItemClickListener;

    public BudgetListAdapter(List<Budget> mBudgets, Context context) {
        this.mBudgets = mBudgets;
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
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
        View view = mInflater.inflate(R.layout.budget_fragment_item, parent, false);

        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        settingsItemViews(holder, position);
        bindEvents(holder);
    }

    private void settingsItemViews(ViewHolder holder, int position) {
        Budget budget = mBudgets.get(position);
        holder.classifyText.setText(budget.getClassify());
        holder.countMoneyText.setText(budget.getCountMoney() + "CNY");
        if (Math.abs(budget.getCurrMoney()) < budget.getCountMoney()) {
            holder.currMoneyText.setText("当前用量：" + Math.abs(budget.getCurrMoney()));
        } else {
            holder.currMoneyText.setText("当前用量：超出预算" + (Math.abs(budget.getCurrMoney()) - budget.getCountMoney()));
        }

        try {
            holder.startDateText.setText(Util.formatDateUseCh(Util.parseMsNotCh(budget.getStartTime() + "")));
            holder.endDateText.setText(Util.formatDateUseCh(Util.parseMsNotCh(budget.getEndTime() + "")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.progressBar.setProgressColor(Color.parseColor(budget.getColor()));
        holder.progressBar.setRadius(2);
        holder.progressBar.setMax(100);
        holder.progressBar.setProgress(((float) Math.abs(budget.getCurrMoney()) / budget.getCountMoney()) * 100);
    }

    /**
     * 为卡片和item的点击和长按时间绑定上前面写的listener监听，将是事件作为回调抛出去
     *
     * @param holder
     */
    private void bindEvents(final ViewHolder holder) {
        if (mItemClickListener != null) {
            if (holder.itemLayout != null) {
                holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        mItemClickListener.onItemClick(v, position);
                    }
                });

                holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        mItemClickListener.onLongClick(v, position);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBudgets.size();
    }

    /**
     * 添加数据
     *
     * @param position    添加的位置
     * @param accountBill 要添加的数据
     */
    public void addItem(int position, Budget accountBill) {
        mBudgets.add(position, accountBill);
        notifyItemInserted(position);
    }

    /**
     * 删除数据
     *
     * @param position 删除的位置
     */
    public void removeItem(int position) {
        mBudgets.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemLayout;
        TextView classifyText;
        TextView countMoneyText;
        TextView currMoneyText;
        TextView startDateText;
        TextView endDateText;
        RoundCornerProgressBar progressBar;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.itemLayout);
            classifyText = (TextView) itemView.findViewById(R.id.ClassifyText);
            countMoneyText = (TextView) itemView.findViewById(R.id.CountMoneyText);
            currMoneyText = (TextView) itemView.findViewById(R.id.CurrMoneyText);
            startDateText = (TextView) itemView.findViewById(R.id.StartDateText);
            endDateText = (TextView) itemView.findViewById(R.id.EndDateText);
            progressBar = (RoundCornerProgressBar) itemView.findViewById(R.id.Progress);
        }
    }
}
