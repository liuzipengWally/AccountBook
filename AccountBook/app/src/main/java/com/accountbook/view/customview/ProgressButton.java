package com.accountbook.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private Paint mPaint, mPaintSecond;

    private RectF mRect, mInnerRect, mArcRect;

    private int mRadius, mFinalRadius;
    private int mSweepAngle = 0;

    public static final int SPEED = 15;

    private boolean mClickAfterIsDone;

    private int mProgressState;

    private int mDistance;
    private int innerBottom;

    private int mState = CLICK_BEFORE;

    public static final int CLICK_BEFORE = 0;
    public static final int CLICK_AFTER = 1;
    public static final int LOADING = 2;
    public static final int LOADING_DONE = 3;
    public static final int LOADING_ERROR = 4;

    private int mWidth;
    private int mHeight;

    private int mCurrWidth;
    private int mCurrLeft = 0;

    private int mFinalLeft;
    private int mFinalRight;

    private OnClickListener mListener;
    private OnProgressDoneListener mDoneListener;

    public interface OnClickListener {
        void onClick(View view);
    }

    public interface OnProgressDoneListener {
        void done();
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public void setDoneListener(OnProgressDoneListener listener) {
        mDoneListener = listener;
    }

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

        mPaintSecond = new Paint();
        mPaintSecond.setAntiAlias(true);

        mDistance = (int) Util.dp2px(5, getResources().getDisplayMetrics());
        mRadius = (int) Util.dp2px(80, getResources().getDisplayMetrics());
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
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mFinalLeft = mWidth / 2 - mHeight / 2;
        mFinalRight = mWidth / 2 + mHeight / 2;

        mCurrWidth = mWidth;
        innerBottom = mHeight - mDistance;
        mFinalRadius = mHeight / 2;

        mRect = new RectF(0, 0, mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                if (mState == CLICK_BEFORE) {
                    if (mListener != null) {
                        mListener.onClick(this);
                    }
                }
                break;
        }
        return true;
    }

    public void showProgress() {
        mState = CLICK_AFTER;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mState) {
            case CLICK_BEFORE:
                mPaint.setColor(mColor);
                canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);

                mPaintSecond.setColor(Color.WHITE);
                mPaintSecond.setTextSize(mTextSize);
                mPaintSecond.setTextAlign(Paint.Align.CENTER);

                canvas.drawText(mText, getMeasuredWidth() / 2, getMeasuredHeight() / 2 + (mTextSize / 4), mPaintSecond);
                break;
            case CLICK_AFTER:
                if (mCurrWidth - mCurrLeft > mHeight) {
                    mCurrWidth = mCurrWidth - SPEED > mFinalRight ? mCurrWidth - SPEED : mFinalRight;
                    mCurrLeft = mCurrLeft + SPEED < mFinalLeft ? mCurrLeft + SPEED : mFinalLeft;

                    mRect = new RectF(mCurrLeft, 0, mCurrWidth, getMeasuredHeight());
                    mInnerRect = new RectF(mDistance + mCurrLeft, mDistance, mCurrWidth - mDistance, innerBottom);

                    mRadius = mRadius + 10 < mFinalRadius ? mRadius + 10 : mFinalRadius;

                    mPaint.setColor(Color.parseColor("#cecece"));
                    canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);

                    mPaint.setColor(Color.WHITE);
                    canvas.drawRoundRect(mInnerRect, mRadius, mRadius, mPaint);
                } else {
                    mClickAfterIsDone = true;

                    switch (mProgressState) {
                        case LOADING_DONE:
                            mState = LOADING_DONE;
                            break;
                        case LOADING_ERROR:
                            mState = LOADING_ERROR;
                            break;
                        default:
                            mState = LOADING;
                            break;
                    }
                }

                invalidate();
                break;
            case LOADING:
                mPaint.setColor(Color.parseColor("#cecece"));
                canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);

                mPaintSecond.setColor(mColor);
                mPaintSecond.setStyle(Paint.Style.FILL);
                canvas.drawArc(mRect, 0, mSweepAngle, true, mPaintSecond);

                mPaint.setColor(Color.WHITE);
                canvas.drawRoundRect(mInnerRect, mRadius, mRadius, mPaint);

                mSweepAngle = mSweepAngle >= 360 ? 0 : mSweepAngle + 5;
                invalidate();
                break;
            case LOADING_DONE:
                mPaint.setColor(Color.parseColor("#cecece"));
                canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawRoundRect(mInnerRect, mRadius, mRadius, mPaint);

                mPaintSecond.setColor(mColor);
                mPaintSecond.setStyle(Paint.Style.FILL);
                canvas.drawArc(mRect, 0, mSweepAngle, true, mPaintSecond);

                mSweepAngle = mSweepAngle >= 360 ? 360 : mSweepAngle + 5;

                if (mSweepAngle == 360) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_done_white_24dp);
                    canvas.drawBitmap(bitmap, mFinalLeft + (mHeight / 2 - bitmap.getWidth() / 2), mHeight / 2 - bitmap.getHeight() / 2, null);

                    if (mDoneListener != null) {
                        mDoneListener.done();
                    }
                }

                invalidate();
                break;
            case LOADING_ERROR:
                if (mCurrLeft != 0 && mCurrWidth != mWidth) {
                    mCurrWidth = mCurrWidth + SPEED < mWidth ? mCurrWidth + SPEED : mWidth;
                    mCurrLeft = mCurrLeft - SPEED > 0 ? mCurrLeft - SPEED : 0;

                    mRect = new RectF(mCurrLeft, 0, mCurrWidth, mHeight);

                    int oldRadius = (int) Util.dp2px(80, getResources().getDisplayMetrics());
                    mRadius = mRadius - 10 > oldRadius ? mRadius - 10 : oldRadius;

                    mPaint.setColor(mColor);
                    canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);

                    invalidate();
                } else {
                    mCurrWidth = mCurrWidth + SPEED < mWidth ? mCurrWidth + SPEED : mWidth;
                    mCurrLeft = mCurrLeft - SPEED > 0 ? mCurrLeft - SPEED : 0;

                    mRect = new RectF(mCurrLeft, 0, mCurrWidth, mHeight);

                    int oldRadius = (int) Util.dp2px(80, getResources().getDisplayMetrics());
                    mRadius = mRadius - 10 > oldRadius ? mRadius - 10 : oldRadius;

                    mPaint.setColor(mColor);
                    canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);

                    mPaintSecond.setColor(Color.WHITE);
                    mPaintSecond.setTextSize(mTextSize);
                    mPaintSecond.setTextAlign(Paint.Align.CENTER);

                    canvas.drawText(mText, getMeasuredWidth() / 2, getMeasuredHeight() / 2 + (mTextSize / 4), mPaintSecond);

                    mState = CLICK_BEFORE;
                    mClickAfterIsDone = false;
                    mProgressState = 0;
                }
                break;
        }
    }

    public void done() {
        if (mClickAfterIsDone) {
            mState = LOADING_DONE;
        } else {
            mState = CLICK_AFTER;
            mProgressState = LOADING_DONE;
        }

        invalidate();
    }

    public void error(String msg) {
        if (mClickAfterIsDone) {
            mState = LOADING_ERROR;
        } else {
            mState = CLICK_AFTER;
            mProgressState = LOADING_ERROR;
        }

        mText = msg;
        invalidate();
    }
}
