package com.accountbook.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.accountbook.R;
import com.accountbook.presenter.LogoutPresenter;

import java.util.Calendar;

/**
 * Created by liuzipeng on 16/3/16.
 */
public class DialogManager {
    private Context mContext;

    public interface OnDateRangeSetListener {
        //时间区间选择完后，将时间毫秒值返回
        void dateRangeSet(long startMillisecond, long endMillisecond);
    }

    public interface OnDialogMenuSelectListener {
        //菜单型对话框，将选择的item下标返回
        void menuSelect(int which);
    }

    public DialogManager(Context mContext) {
        this.mContext = mContext;
    }

    public void showDateRangePickerDialog(final OnDateRangeSetListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);//实例化自定义dialog
        //渲染出要放进这个dialog的布局文件为View对象
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_date_dialog, null);
        builder.setView(view); //设置布局文件

        //获取到选择开始时间与结束时间的两个DatePicker
        final DatePicker startDatePicker = (DatePicker) view.findViewById(R.id.startDatePicker);
        final DatePicker endDatePicker = (DatePicker) view.findViewById(R.id.endDatePicker);

        //设置dialog的取消按钮，并绑定该按钮点击事件，当被点击时关闭dialog
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //设置dialog的确定按钮，并绑定该按钮点击事件，当被点击时获取到选择的时间，并通过前面写的回调监听传递出去
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, startDatePicker.getYear());
                calendar.set(Calendar.MONTH, startDatePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, startDatePicker.getDayOfMonth());
                long startMillisecond = calendar.getTimeInMillis();

                calendar.set(Calendar.YEAR, endDatePicker.getYear());
                calendar.set(Calendar.MONTH, endDatePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, endDatePicker.getDayOfMonth());
                long endMillisecond = calendar.getTimeInMillis();

                if (listener != null) {
                    listener.dateRangeSet(startMillisecond, endMillisecond);
                }
            }
        });

        builder.show(); //显示出dialog
    }

    /**
     * 显示一个带菜单的dialog
     *
     * @param items    菜单的item
     * @param listener 选择的监听
     */
    public void showMenuDialog(String[] items, final OnDialogMenuSelectListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);//实例化自定义dialog
        builder.setTitle(R.string.options);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.menuSelect(which);
            }
        });

        builder.show();
    }

}
