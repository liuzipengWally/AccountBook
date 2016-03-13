package com.accountbook.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.entity.Classify;
import com.accountbook.view.customview.CircleIcon;

import java.util.List;

/**
 * Created by liuzipeng on 16/3/12.
 */
public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ViewHolder> {
    private List<Classify> mClassifies;
    private LayoutInflater mInflater;

    private ClassifyAdapter.OnItemClickListener mClickListener;

    public interface OnItemClickListener {
        void onItemClick(int type, String classify, String id);

        void onLongClick(Classify classify, int position);
    }

    public void setOnItemClickListener(ClassifyAdapter.OnItemClickListener listener) {
        mClickListener = listener;
    }

    public ClassifyAdapter(List<Classify> mClassifies, Context context) {
        this.mClassifies = mClassifies;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.classify_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.circleIcon.setIconResId(mClassifies.get(position).getIconResId());
        holder.circleIcon.setColor(Color.parseColor(mClassifies.get(position).getColor()));
        holder.textView.setText(mClassifies.get(position).getClassify());

        //绑定点击监听，传递type，classify，id 三个数据，为了选中之后，可把这三个数据传递到addActivity
        //以便保存
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                if (mClickListener != null) {
                    mClickListener.onItemClick(mClassifies.get(position).getType(),
                            mClassifies.get(position).getClassify(), mClassifies.get(position).getId());
                }
            }
        });

        //绑定长按监听，因为长按可能要编辑这个item，所以将整个分类实体传过去，
        //再可能会是删除，所以传一个position，以确定删除的是哪个item
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getLayoutPosition();
                if (mClickListener != null) {
                    mClickListener.onLongClick(mClassifies.get(position), position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mClassifies.size();
    }

    /**
     * 添加数据
     *
     * @param position 添加的位置
     * @param classify 要添加的数据
     */
    public void addItem(int position, Classify classify) {
        mClassifies.add(position, classify);
        notifyItemInserted(position);
    }

    /**
     * 删除数据
     *
     * @param position 删除的位置
     */
    public void removeItem(int position) {
        mClassifies.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleIcon circleIcon;
        TextView textView;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            circleIcon = (CircleIcon) itemView.findViewById(R.id.classify_circleIcon);
            textView = (TextView) itemView.findViewById(R.id.classify_lbl);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.classify_item_rootLayout);
        }
    }
}

