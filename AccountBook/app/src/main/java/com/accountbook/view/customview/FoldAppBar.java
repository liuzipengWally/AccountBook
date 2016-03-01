package com.accountbook.view.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.accountbook.R;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.Util;
import com.accountbook.view.customview.wrapper.WrapperView;

/**
 * Created by liuzipeng on 16/2/29.
 */
public class FoldAppBar extends AppBarLayout {
    private Toolbar mToolbar;
    private View mSecondChild;

    public int minHeight;
    public int maxHeight;

    private int downY;
    private int offsetY;

    private boolean flag = true;

    public boolean isFold;

    private OnAppbarStateChangeListener mStateChangeListener;

    public interface OnAppbarStateChangeListener {
        void onStateChange(boolean state);
    }

    public void setStateChangeListener(OnAppbarStateChangeListener listener) {
        mStateChangeListener = listener;
    }

    public FoldAppBar(Context context) {
        this(context, null);
    }

    public FoldAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        minHeight = (int) Util.dp2px(ConstantContainer.TOOLBAR_HEIGHT, getResources().getDisplayMetrics());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mToolbar = (Toolbar) getChildAt(0);
        mSecondChild = getChildAt(1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetY = y - downY;
                break;
            case MotionEvent.ACTION_UP:
                if (offsetY < 0) {
                    fold();
                } else if (offsetY > 0) {
                    unfold();
                }
                break;
        }

        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (flag) {
            maxHeight = (int) (mSecondChild.getHeight() + minHeight + Util.dp2px(8, getResources().getDisplayMetrics()));
            flag = false;
        }
    }

    public void unfold() {
        WrapperView wrapperView = new WrapperView(this);

        ObjectAnimator height = ObjectAnimator.ofInt(wrapperView, "height", maxHeight);
        height.setDuration(200);
        height.setInterpolator(new FastOutSlowInInterpolator());
        height.start();

        isFold = false;

        if (mStateChangeListener != null) {
            mStateChangeListener.onStateChange(isFold);
        }
    }

    public void fold() {
        WrapperView wrapperView = new WrapperView(this);

        ObjectAnimator height = ObjectAnimator.ofInt(wrapperView, "height", minHeight);
        height.setDuration(200);
        height.setInterpolator(new OvershootInterpolator());
        height.start();

        isFold = true;

        if (mStateChangeListener != null) {
            mStateChangeListener.onStateChange(isFold);
        }
    }
}
