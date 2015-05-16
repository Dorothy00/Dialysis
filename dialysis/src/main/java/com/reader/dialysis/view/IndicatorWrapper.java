package com.reader.dialysis.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import test.dorothy.graduation.activity.R;


public class IndicatorWrapper extends ViewGroup implements OnClickListener {
    private static final String TAG = "IndicatorWrapper";
    private final ImageView mIndicatorView;
    private final TextView mMessageView;
    private boolean mShowIndicator;
    private Animation mAnimation;

    private int w_screen;
    int h_screen;

    private ImageView mIndicatorFailureView;
    private TextView mFailureMessageView;

    private onHandleFailureListener mFailureListener;

    public IndicatorWrapper(Context context) {
        this(context, null, 0);
    }

    public IndicatorWrapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int textColor = getResources().getColor(R.color.accent_material_light);
        int textSize = (int) getResources().getDimension(R.dimen.textsize13);
        mIndicatorView = new ImageView(context);
        mIndicatorFailureView = new ImageView(context);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        w_screen = dm.widthPixels;
        h_screen = dm.heightPixels;

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.IndicatorWrapper);

        try {
            Drawable d = a.getDrawable(R.styleable.IndicatorWrapper_drawable);
            if (d != null && d instanceof AnimationDrawable) {
                setIndicatorAnimationDrawable((AnimationDrawable) d);
            } else {
                mIndicatorView.setImageDrawable(d);
            }
            mAnimation = AnimationUtils.loadAnimation(context, a.getResourceId(
                    R.styleable.IndicatorWrapper_animation, R.anim.rotate));

        } finally {
            a.recycle();
        }

        mIndicatorView.setVisibility(View.GONE);
        addView(mIndicatorView);

        mIndicatorFailureView.setVisibility(View.GONE);
        Drawable drawable = getResources().getDrawable(
                R.drawable.icon_loading_failure);
        mIndicatorFailureView.setImageDrawable(drawable);
        mIndicatorFailureView.setOnClickListener(this);
        addView(mIndicatorFailureView);

        mFailureMessageView = new TextView(context, attrs, defStyle);
        mFailureMessageView.setVisibility(View.GONE);
        mFailureMessageView.setText("加载失败，点击重新加载");
        mFailureMessageView.setTextColor(textColor);
        mFailureMessageView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        addView(mFailureMessageView);

        mMessageView = new TextView(context, attrs, defStyle);
        mMessageView.setVisibility(View.GONE);
        mMessageView.setTextColor(textColor);
        mMessageView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        addView(mMessageView);

    }

    public IndicatorWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        final int layoutWidth = widthSize - getPaddingLeft()
                - getPaddingRight();
        final int layoutHeight = heightSize - getPaddingTop()
                - getPaddingBottom();

        final int count = getChildCount();

        if (count > 5) {
            Log.e(TAG,
                    "onMeasure: More than one child views are not supported.");
        }

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child == mIndicatorView) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidthSpec;
            if (lp.width == LayoutParams.WRAP_CONTENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(layoutWidth,
                        MeasureSpec.AT_MOST);
            } else if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(layoutWidth,
                        MeasureSpec.EXACTLY);
            } else {
                childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width,
                        MeasureSpec.EXACTLY);
            }

            int childHeightSpec;
            if (lp.height == LayoutParams.WRAP_CONTENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(layoutHeight,
                        MeasureSpec.AT_MOST);
            } else if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(layoutHeight,
                        MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height,
                        MeasureSpec.EXACTLY);
            }

            child.measure(childWidthSpec, childHeightSpec); //

            width = Math.max(width, getPaddingLeft() + getPaddingRight()
                    + child.getMeasuredWidth());
            height = Math.max(height, getPaddingTop() + getPaddingBottom()
                    + child.getMeasuredHeight());
        }

        mIndicatorView.measure(
                MeasureSpec.makeMeasureSpec(layoutWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(layoutHeight, MeasureSpec.AT_MOST));
        mIndicatorView.measure(
                MeasureSpec.makeMeasureSpec(layoutWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(layoutHeight, MeasureSpec.AT_MOST));

        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {

            View child = getChildAt(i);
            if (child == mIndicatorView || child == mIndicatorFailureView) {
                continue;
            }
            child.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft()
                            + child.getMeasuredWidth(),
                    getPaddingTop() + child.getMeasuredHeight());
        }

        int width = Math.min(w_screen, getMeasuredWidth());
        // 普通的indicator
        int x = (width - mIndicatorView.getMeasuredWidth()) / 2;
        int y = (getHeight() - mIndicatorView.getMeasuredHeight()) / 2;
        mIndicatorView.layout(x, y, x + mIndicatorView.getMeasuredWidth(), y
                + mIndicatorView.getMeasuredHeight());

        x = (getMeasuredWidth() - mMessageView.getMeasuredWidth()) / 2;
        y = y + mIndicatorView.getMeasuredHeight();
        mMessageView.layout(x, y, x + mMessageView.getMeasuredWidth(), y
                + mMessageView.getMeasuredHeight());

        x = (width - mIndicatorFailureView.getMeasuredWidth()) / 2;
        y = (getHeight() - mIndicatorFailureView.getMeasuredHeight()) * 5 / 12;

        // 失败的indicator
        mIndicatorFailureView.layout(x, y,
                x + mIndicatorFailureView.getMeasuredWidth(), y
                        + mIndicatorFailureView.getMeasuredHeight());
        int marginTop = (int) getResources().getDimension(R.dimen.margin3);
        x = (getMeasuredWidth() - mFailureMessageView.getMeasuredWidth()) / 2;
        y = y + mIndicatorFailureView.getMeasuredHeight();
        mFailureMessageView.layout(x, y + marginTop,
                x + mFailureMessageView.getMeasuredWidth(), y
                        + mFailureMessageView.getMeasuredHeight() + marginTop);

    }

    public void showIndicator() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == mIndicatorView || child == mMessageView) {
                continue;
            }
            child.setVisibility(View.INVISIBLE);
        }
        mIndicatorView.setVisibility(View.VISIBLE);
        mMessageView.setVisibility(View.VISIBLE);
        if (mAnimationDrawable != null) {
            mAnimationDrawable.start();
        } else {
            mIndicatorView.startAnimation(mAnimation);

        }
        mShowIndicator = true;
    }

    public void hideIndicator() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == mIndicatorView || child == mMessageView
                    || child == mIndicatorFailureView
                    || child == mFailureMessageView) {
                continue;
            }
            child.setVisibility(View.VISIBLE);
        }
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }
        mAnimation.cancel();
        mIndicatorView.clearAnimation();
        mIndicatorView.setVisibility(View.GONE);
        mMessageView.setVisibility(View.GONE);
        mIndicatorFailureView.setVisibility(View.GONE);
        mFailureMessageView.setVisibility(View.GONE);
        mShowIndicator = false;
    }

    public void showFailureIndicator() {
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }
        mAnimation.cancel();
        mIndicatorView.clearAnimation();
        mIndicatorView.setVisibility(View.GONE);
        mMessageView.setVisibility(View.GONE);
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == mIndicatorFailureView
                    || child == mIndicatorFailureView) {
                continue;
            }
            child.setVisibility(View.INVISIBLE);
        }
        mIndicatorFailureView.setVisibility(View.VISIBLE);
        mFailureMessageView.setVisibility(View.VISIBLE);
        mShowIndicator = true;
    }

    public void setText(CharSequence text) {
        mMessageView.setText(text);
    }

    public CharSequence getText() {
        return mMessageView.getText();
    }

    /**
     * @return the mShowIndicator
     */
    public boolean isShowIndicator() {
        return mShowIndicator;
    }

    private AnimationDrawable mAnimationDrawable;

    public void setIndicatorAnimation(int resId) {
        setIndicatorAnimationDrawable((AnimationDrawable) (getResources()
                .getDrawable(resId)));

    }

    public void setIndicatorAnimationDrawable(AnimationDrawable drawable) {
        if (drawable != mAnimationDrawable) {
            this.mAnimationDrawable = drawable;
            mIndicatorView.setImageDrawable(drawable);
        }
    }

    public AnimationDrawable getAnimationDrawable() {
        if (mAnimationDrawable == null) {
          // setIndicatorAnimation(R.drawable.indicator); // FIXME bug
        }
        return mAnimationDrawable;
    }

    @Override
    public void onClick(View v) {
        if (v == mIndicatorFailureView) {
            showIndicator();
            if (mFailureListener != null) {
                mFailureListener.onTryAgain();
            }
        }

    }

    public void setOnHandleFailureListener(onHandleFailureListener listener) {
        mFailureListener = listener;
    }

    public interface onHandleFailureListener {
        public void onTryAgain();
    }

}

