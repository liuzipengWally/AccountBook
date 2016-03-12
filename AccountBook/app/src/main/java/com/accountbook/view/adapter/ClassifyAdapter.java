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

    private HomeListAdapter.OnItemClickListener mClickListener;

    public interface OnItemClickListener {
        void onClick(View view, int position, int type);

        void onLongClick(View view, int position, int type);
    }

    public void setOnItemClickListener(HomeListAdapter.OnItemClickListener listener) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.circleIcon.setIconResId(mClassifies.get(position).getIconResId());
        holder.circleIcon.setColor(Color.parseColor(mClassifies.get(position).getColor()));
        holder.textView.setText(mClassifies.get(position).getClassify());
    }

    @Override
    public int getItemCount() {
        return mClassifies.size();
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

