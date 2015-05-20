package com.reader.dialysis.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reader.dialysis.fragment.ReadProgressFragment;
import com.reader.dialysis.fragment.UserBookFragment;
import com.reader.dialysis.fragment.WordsFragment;

import test.dorothy.graduation.activity.R;

/**
 * Created by dorothy on 15/5/9.
 */
public class DrawerView extends LinearLayout implements View.OnClickListener {
    public static final String BOOK_TAG = UserBookFragment.class.getName();
    public static final String WORD_TAG = WordsFragment.class.getName();
    public static final String PROGRESS_TAG = ReadProgressFragment.class.getName();

    private TextView mTvUsername;
    private LinearLayout mBookContainer;
    private LinearLayout mWordContainer;
    private LinearLayout mReadProgressContainer;
    private OnDrawerItemClickListener mListener;

    public interface OnDrawerItemClickListener {
        void renderPanel(String tag);
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_drawer, this);
        mTvUsername = (TextView) findViewById(R.id.username);
        mBookContainer = (LinearLayout) findViewById(R.id.my_book);
        mWordContainer = (LinearLayout) findViewById(R.id.words);
        mReadProgressContainer = (LinearLayout) findViewById(R.id.progress);

        mBookContainer.setOnClickListener(this);
        mWordContainer.setOnClickListener(this);
        mReadProgressContainer.setOnClickListener(this);
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

        if (v.getId() == R.id.my_book) {
            mBookContainer.setSelected(true);
            mWordContainer.setSelected(false);
            mListener.renderPanel(BOOK_TAG);
        } else if (v.getId() == R.id.words) {
            mWordContainer.setSelected(true);
            mBookContainer.setSelected(false);
            mListener.renderPanel(WORD_TAG);
        } else if (v.getId() == R.id.progress) {
            mReadProgressContainer.setSelected(true);
            mListener.renderPanel(PROGRESS_TAG);
        }
    }
}
