package com.accountbook.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.accountbook.R;
import com.accountbook.tools.Util;

/**
 * 主页RecyclerView的分隔线
 */
public class HomeListDivider extends RecyclerView.ItemDecoration {
    private static final int[] attrs = {android.R.attr.listDivider};//系统自带分割线文件，获取后先保存为数组

    private Drawable mDivider;

    public HomeListDivider(Context context) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs); //将数组转化为TypedArray

        mDivider = typedArray.getDrawable(0);  //取出这个Drawable文件

        typedArray.recycle();   //回收TypedArray
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        CircleIcon weekText = (CircleIcon) parent.findViewById(R.id.circle_icon);
        DisplayMetrics metrics = parent.getContext().getResources().getDisplayMetrics();

        int left = (int) (weekText.getWidth() + Util.dp2px(32, metrics));
        int right = (int) (parent.getWidth() - Util.dp2px(16, metrics));

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
