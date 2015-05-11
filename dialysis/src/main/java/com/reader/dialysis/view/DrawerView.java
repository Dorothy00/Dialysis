package com.reader.dialysis.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import test.dorothy.graduation.activity.R;

/**
 * Created by dorothy on 15/5/9.
 */
public class DrawerView extends LinearLayout {
    private TextView mTvUsername;
    private LinearLayout mStartContainer;

    public DrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_drawer, this);
        mTvUsername = (TextView) findViewById(R.id.username);
        mStartContainer = (LinearLayout) findViewById(R.id.item1);
    }

    public DrawerView(Context context) {
        this(context, null);
    }

    public DrawerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setUserName(String username){
        mTvUsername.setText(username);
    }
}
