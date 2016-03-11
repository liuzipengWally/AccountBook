package com.accountbook.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liuzipeng on 16/3/5.
 */
public class DoubleClickDomain extends View {
    private int mClickCount;

    private long mFirstClick, mSecondClick;

    private OnDoubleClickListener mListener;

    public interface OnDoubleClickListener {
        void doubleClick(View view);
    }

    public void setOnDoubleClickListener(OnDoubleClickListener listener) {
        mListener = listener;
    }

    public DoubleClickDomain(Context context) {
        this(context, null);
    }

    public DoubleClickDomain(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleClickDomain(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mClickCount++;
                switch (mClickCount) {
                    case 1:
                        mFirstClick = System.currentTimeMillis();
                        break;
                    case 2:
                        mSecondClick = System.currentTimeMillis();

                        if (mSecondClick - mFirstClick < 350) {
                            if (mListener != null) {
                                mListener.doubleClick(this);
                            }

                            mFirstClick = 0;
                            mSecondClick = 0;
                            mClickCount = 0;
                        } else {
                            mFirstClick = System.currentTimeMillis();
                            mClickCount = 1;
                        }

                        break;
                }
                break;
        }

        return true;
    }
}
