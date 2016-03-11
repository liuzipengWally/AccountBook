package com.accountbook.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.accountbook.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_input);
    }

    /**
     * 当点击非输入处时隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isShouldHideKeyBoard(view, event)) {
                hideKeyBoard(view);
            }
        }

        return super.dispatchTouchEvent(event);
    }


    /**
     * 判断点击坐标是否在当前得到焦点的EditText内
     */
    private boolean isShouldHideKeyBoard(View view, MotionEvent event) {
        if (view != null && view instanceof EditText) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            int left = location[0];
            int top = location[1];
            int right = left + view.getWidth();
            int bottom = top + view.getHeight();
            System.out.println(left + " " + right + " " + top + " " + bottom);
            int x = (int) event.getX();
            int y = (int) event.getY();
            return x < left || x > right || y < top || y > bottom;
        } else
            return false;
    }


    /**
     * 隐藏键盘
     *
     * @param view
     */
    private void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
