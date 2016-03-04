package com.accountbook.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.accountbook.R;
import com.accountbook.tools.Util;

/**
 * Created by liuzipeng on 16/3/2.
 */
public class ProgressButton extends View {
    private String mText;
    private int mColor;
    private int mTextSize;

    private Paint mPaint;

    private RectF mRect;
    private RectF mInnerRect;

    private int mRadius = 100;

    public static final int SPEED = 25;

    private int mDistance;

    private int mState = CLICK_BEFORE;

    public static final int CLICK_BEFORE = 0;
    public static final int CLICK_AFTER = 1;
    public static final int LOADING = 2;
    public static final int LOADING_ERROR = 3;


    private int width;
    private int height;

    private int mNewWidth;
    private int mNewLeft = 0;

    private int mLeft;
    private int mRight;

    private Paint paint;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getAttributes(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        paint = new Paint();
        paint.setAntiAlias(true);

        mDistance = (int) Util.dp2px(5, getResources().getDisplayMetrics());
    }

    private void getAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton);

        mText = typedArray.getString(R.styleable.ProgressButton_btn_text);
        mColor = typedArray.getColor(R.styleable.ProgressButton_btn_background, getResources().getColor(R.color.colorAccent));
        mTextSize = (int) typedArray.getDimension(R.styleable.ProgressButton_btn_textSize, Util.sp2px(14, getResources().getDisplayMetrics()));

        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        mLeft = width / 2 - height / 2;
        mRight = width / 2 + height / 2;

        mNewWidth = width;

        mRect = new RectF(0, 0, width, height);
        mInnerRect = new RectF(mDistance, mDistance, height - mDistance, height - mDistance);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                if (mState == CLICK_BEFORE) {
                    mState = CLICK_AFTER;
                    invalidate();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        switch (mState) {
            case CLICK_BEFORE:
                mPaint.setColor(mColor);
                canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);

                mPaint.setColor(mColor);
                canvas.drawRoundRect(mInnerRect, mRadius, mRadius, mPaint);

                paint.setColor(Color.WHITE);
                paint.setTextSize(mTextSize);
                paint.setTextAlign(Paint.Align.CENTER);

                canvas.drawText(mText, getMeasuredWidth() / 2, getMeasuredHeight() / 2 + (mTextSize / 4), paint);
                break;
            case CLICK_AFTER:
                int innerBottom = height - mDistance;

                int newRadius = height / 2;

                if (width > height) {
                    mNewWidth = mNewWidth - SPEED > mRight ? mNewWidth - SPEED : mRight;
                    mNewLeft = mNewLeft + SPEED < mLeft ? mNewLeft + SPEED : mLeft;

                    mRect = new RectF(mNewLeft, 0, mNewWidth, getMeasuredHeight());
                    mInnerRect = new RectF(mDistance + mNewLeft, mDistance, mNewWidth - mDistance, innerBottom);

                    mRadius = mRadius + 10 < newRadius ? mRadius + 10 : newRadius;

                    mPaint.setColor(Color.parseColor("#cecece"));
                    canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);

                    mPaint.setColor(Color.WHITE);
                    canvas.drawRoundRect(mInnerRect, mRadius, mRadius, mPaint);

                    postInvalidateDelayed(1);
                } else {
                    mState = LOADING;
                }
                break;
            case LOADING:

                break;
            case LOADING_ERROR:

                break;
        }
    }
}
