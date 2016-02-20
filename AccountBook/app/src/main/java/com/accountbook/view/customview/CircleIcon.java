package com.accountbook.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.accountbook.R;

/**
 * Created by liuzipeng on 16/2/19.
 */
public class CircleIcon extends View {
    private int mImageId = R.mipmap.head;

    public CircleIcon(Context context) {
        this(context, null);
    }

    public CircleIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void drawCircleIcon(int id) {
        mImageId = id;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mImageId);

        Matrix matrix = new Matrix();
        matrix.postScale((float) getMeasuredWidth() / bitmap.getWidth(), (float) getMeasuredHeight() / bitmap.getHeight());
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        Paint shaderPaint = new Paint();
        shaderPaint.setAntiAlias(true);
        BitmapShader bitmapShader = new BitmapShader(newBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shaderPaint.setShader(bitmapShader);

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2, shaderPaint);
    }
}
