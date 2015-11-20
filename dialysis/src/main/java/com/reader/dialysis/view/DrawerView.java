package com.reader.dialysis.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reader.dialysis.activity.AllBookActivity;
import com.reader.dialysis.activity.HomeActivity;
import com.reader.dialysis.activity.ReadProgressActivity;
import com.reader.dialysis.activity.SettingActivity;
import com.reader.dialysis.activity.WordsActivity;
import com.reader.dialysis.fragment.UserBookFragment;

import test.dorothy.graduation.activity.R;

/**
 * Created by dorothy on 15/5/9.
 */
public class DrawerView extends LinearLayout implements View.OnClickListener {
    public static final String BOOK_TAG = UserBookFragment.class.getName();

    private TextView mTvUsername;
    private TextView mBookContainer;
    private TextView mWordContainer;
    private TextView mReadProgressContainer;
    private TextView mSettingContainer;
    private TextView mAllBookContainer;
    private OnDrawerItemClickListener mListener;
    private Context mContext;

    public interface OnDrawerItemClickListener {
        void renderPanel(String tag);
        void closeDrawer();
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_drawer, this);
        mTvUsername = (TextView) findViewById(R.id.username);
        mBookContainer = (TextView) findViewById(R.id.my_book);
        mWordContainer = (TextView) findViewById(R.id.words);
        mReadProgressContainer = (TextView) findViewById(R.id.progress);
        mSettingContainer = (TextView) findViewById(R.id.setting);
        mAllBookContainer = (TextView) findViewById(R.id.all_book);

        mBookContainer.setOnClickListener(this);
        mWordContainer.setOnClickListener(this);
        mReadProgressContainer.setOnClickListener(this);
        mSettingContainer.setOnClickListener(this);
        mAllBookContainer.setOnClickListener(this);
        mContext = context;
    }

    public DrawerView(Context context) {
        this(context, null);
    }

    public DrawerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setUserName(String username) {
        mTvUsername.setText(username);
    }

    public void setOnDrawerItemClickListener(OnDrawerItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }
        selectTab(v.getId());
    }

    private void selectTab(int tabId) {
        mBookContainer.setSelected(false);
        mWordContainer.setSelected(false);
        mReadProgressContainer.setSelected(false);
        mAllBookContainer.setSelected(false);
        mSettingContainer.setSelected(false);
        mListener.closeDrawer();

        switch (tabId) {
            case R.id.my_book:
                mBookContainer.setSelected(true);
                mListener.renderPanel(BOOK_TAG);
                break;
            case R.id.all_book:
                mAllBookContainer.setSelected(true);
                Intent intent1 = new Intent(mContext, AllBookActivity.class);
                ((Activity) mContext).startActivityForResult(intent1, UserBookFragment.REQUEST_CODE_ADD_BOOK);
                break;
            case R.id.words:
                mWordContainer.setSelected(true);
                mContext.startActivity(new Intent(mContext, WordsActivity.class));
                break;
            case R.id.progress:
                mReadProgressContainer.setSelected(true);
                mContext.startActivity(new Intent(mContext, ReadProgressActivity.class));
                break;
            case R.id.setting:
                mSettingContainer.setSelected(true);
                Intent intent2 = new Intent(mContext, SettingActivity.class);
                ((Activity) mContext).startActivityForResult(intent2, HomeActivity.REQUEST_CODE_HOME);
                break;
        }
    }
}
