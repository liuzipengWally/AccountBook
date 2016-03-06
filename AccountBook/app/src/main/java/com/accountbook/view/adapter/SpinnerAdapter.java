package com.accountbook.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by liuzipeng on 16/3/5.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private String[] mStrings;

    public SpinnerAdapter(Context context, String[] mStrings) {
        super(context, android.R.layout.simple_spinner_dropdown_item, mStrings);
        this.mContext = context;
        this.mStrings = mStrings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStrings[position]);
        tv.setTextColor(Color.WHITE);
        return convertView;
    }
}
