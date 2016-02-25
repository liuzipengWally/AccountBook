package com.accountbook.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.accountbook.R;

import java.util.Calendar;

/**
 * 用于显示日期的自定义View，可显示年，或月
 */
public class DateTextView extends TextView {
    private int mDateType;
    public static final int YEAR = 0;
    public static final int MONTH = 1;

    public DateTextView(Context context) {
        this(context, null);
    }

    public DateTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(context, attrs);
        settingsMonth();
    }

    private void settingsMonth() {
        Calendar calendar = Calendar.getInstance();
        switch (mDateType) {
            case YEAR:
                setText(" / " + calendar.get(Calendar.YEAR));
                break;
            case MONTH:
                setText((calendar.get(Calendar.MONTH) + 1) + "");
                break;
        }
    }

    private void getAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DateTextView);
        mDateType = typedArray.getInt(R.styleable.DateTextView_date_type, 0);
        typedArray.recycle();
    }
}
