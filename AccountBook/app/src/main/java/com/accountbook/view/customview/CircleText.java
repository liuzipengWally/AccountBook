package com.accountbook.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.accountbook.R;
import com.accountbook.tools.Util;

/**
 * 显示圆形文字的自定义View
 */
public class CircleText extends ImageView {
    private String mText = "一";
    private int mColor;
    private int mTextSize;

    public CircleText(Context context) {
        this(context, null);
    }

    public CircleText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getAttributes(context, attrs);
    }


    private void getAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleText);
        mText = typedArray.getString(R.styleable.CircleText_circleText);
        mTextSize = (int) typedArray.getDimension(R.styleable.CircleText_circleTextSize, Util.sp2px(20, getResources().getDisplayMetrics()));
        mColor = typedArray.getColor(R.styleable.CircleText_circleColor, getResources().getColor(R.color.colorAccent));

        typedArray.recycle();
    }

    public void setText(String text) {
        this.mText = text;
        invalidate();
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        invalidate();
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(mColor);

        float radius = getMeasuredWidth() / 2;
        canvas.drawCircle(radius, radius, radius, circlePaint);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(mTextSize);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText(mText, radius, radius + (mTextSize / 4), paint);
    }
}
