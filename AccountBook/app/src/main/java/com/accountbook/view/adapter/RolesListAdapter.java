package com.accountbook.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.accountbook.R;
import com.accountbook.entity.local.Role;

import java.util.List;

/**
 * 主页近期记录的适配器
 */
public class RolesListAdapter extends RecyclerView.Adapter<RolesListAdapter.ViewHolder> {
    private List<Role> mRoles;
    private LayoutInflater mInflater;
    private Context mContext;

    private OnItemClickListener mItemClickListener;

    public RolesListAdapter(List<Role> roles, Context context) {
        this.mRoles = roles;
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
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.roles_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        settingsItemViews(holder, position);
        bindEvents(holder);
    }

    private void settingsItemViews(ViewHolder holder, int position) {
        Role role = mRoles.get(position);
        holder.roleText.setText(role.getRole());
    }

    /**
     * 为卡片和item的点击和长按时间绑定上前面写的listener监听，将是事件作为回调抛出去
     *
     * @param holder
     */
    private void bindEvents(final ViewHolder holder) {
        if (mItemClickListener != null) {
            if (holder.clearBtn != null) {
                holder.clearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        mItemClickListener.onItemClick(v, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mRoles.size();
    }

    /**
     * 添加数据
     *
     * @param position 添加的位置
     * @param role     要添加的数据
     */
    public void addItem(int position, Role role) {
        mRoles.add(position, role);
        notifyItemInserted(position);
    }

    /**
     * 删除数据
     *
     * @param position 删除的位置
     */
    public void removeItem(int position) {
        mRoles.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton clearBtn;
        TextView roleText;

        public ViewHolder(View itemView) {
            super(itemView);
            clearBtn = (ImageButton) itemView.findViewById(R.id.clearBtn);
            roleText = (TextView) itemView.findViewById(R.id.roleText);
        }
    }
}
