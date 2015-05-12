package com.reader.dialysis.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.reader.dialysis.Model.PageSpan;
import com.reader.dialysis.Model.PaintInfo;
import com.reader.dialysis.Model.WordBounds;
import com.reader.dialysis.Model.WordSpan;

/**
 * Created by dorothy on 15/5/2.
 * <p/>
 * Note: should not setPadding to this custom View
 */
public class PageView extends View {

    private PageSpan pageSpan;


    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (pageSpan == null) {
            return;
        }
        int width = measureSize(widthMeasureSpec, true);
        int height = measureSize(heightMeasureSpec, false);
        setMeasuredDimension(width, height);
    }

    private int measureSize(int measureSpec, boolean isWidth) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int result = MeasureSpec.getSize(measureSpec);

        if (specMode != MeasureSpec.EXACTLY) {
            PaintInfo paintInfo = pageSpan.getPaintInfo();
            result = paintInfo.getScreenW() - paintInfo.getPaddingLeft() - paintInfo
                    .getPaddingRight();
            if (!isWidth) {
                result = paintInfo.getScreenH() - paintInfo.getPaddingTop() - paintInfo
                        .getPaddingBottom();
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pageSpan == null || pageSpan.isEmpty()) {
            return;
        }
        pageSpan.draw(canvas);
    }

    public void invalidateWordSpan(WordBounds wordBounds, boolean isSelected) {
        WordSpan wordSpan = wordBounds.getWordSpan();
        wordSpan.setSelected(isSelected);
        invalidate((int) wordBounds.getLeft(), (int) wordBounds.getTop(), (int) wordBounds
                .getRight(), (int)
                wordBounds.getBottom());
    }

    public void setPageSpan(PageSpan pageSpan) {
        this.pageSpan = pageSpan;
        invalidate();
    }
}
