package com.reader.dialysis.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.reader.dialysis.Model.LineSpan;
import com.reader.dialysis.Model.PageSpan;
import com.reader.dialysis.Model.PaintInfo;
import com.reader.dialysis.Model.WordSpan;

import java.util.List;

import test.dorothy.graduation.activity.R;

/**
 * Created by dorothy on 15/5/2.
 * <p/>
 * Note: should not setPadding to this custom View
 */
public class PageView extends View {

    private PopupWindow popupWindow;
    private int popWindowWidth;
    private int popWindowHeight;
    private PageSpan pageSpan;
    private int preTargetLine;
    private int preTargetWord;

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View popView = LayoutInflater.from(context).inflate(R.layout.layout_popup, null);
        popWindowWidth = (int) context.getResources().getDimension(R.dimen.width40);
        popWindowHeight = (int) context.getResources().getDimension(R.dimen.height40);
        popupWindow = new PopupWindow(popView, popWindowWidth, popWindowHeight);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dismissPopUpWindow();
                // 将上一个被选中的wordSpan的状态置为未选中
                invalidateWordSpan(preTargetLine, preTargetWord, false);

                float actionX = event.getX();
                float actionY = event.getY();

                int targetLine = findTargetLine(actionY);
                int targetWord = -1;
                if (targetLine != -1) {
                    targetWord = findTargetWord(actionX, pageSpan.getLineSpanList().get
                            (targetLine));
                }
                if (targetLine != -1 && targetWord != -1) {
                    showPopupWindow(targetLine, targetWord);
                    invalidateWordSpan(targetLine, targetWord, true);
                }
                Log.d("pageview down--", "pageview down--");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("pageview up--", "pageview up--");
                break;
        }
        return super.onTouchEvent(event);
    }

    private void invalidateWordSpan(int targetLine, int targetWord, boolean isSelected) {
        WordBounds wordBounds = getWordBounds(targetLine, targetWord);
        WordSpan wordSpan = getTargetWordSpan(targetLine, targetWord);
        wordSpan.setSelected(isSelected);
        invalidate((int) wordBounds.left, (int) wordBounds.top, (int) wordBounds.right, (int)
                wordBounds.bottom);
        preTargetLine = targetLine;
        preTargetWord = targetWord;
    }

    private void showPopupWindow(int targetLine, int targetWord) {
        WordBounds wordBounds = getWordBounds(targetLine, targetWord);
        float spaceH = pageSpan.getPaintInfo().getSpaceH();
        float lineH = pageSpan.getLineH();

        WordSpan wordSpan = getTargetWordSpan(targetLine, targetWord);

        float leftDis = wordBounds.left + wordSpan.getWidth() / 2;
        float rightDis = pageSpan.getPaintInfo().getScreenW() - wordBounds.right + wordSpan
                .getWidth() / 2;
        float popX;
        float popY;
        if (popWindowWidth / 2 <= leftDis && popWindowWidth / 2 <= rightDis) {
            // 1. 中间
            popX = leftDis - popWindowWidth / 2;
        } else if (popWindowWidth / 2 <= leftDis) {
            // 2. 右边
            popX = pageSpan.getPaintInfo().getScreenW() - pageSpan.getPaintInfo().getPaddingRight
                    () / 2 - popWindowWidth;
        } else {
            // 3. 左边
            popX = pageSpan.getPaintInfo().getPaddingLeft() / 2;
        }

        float topDis = wordBounds.top + lineH / 2;
        if (topDis - spaceH >= popWindowHeight) {
            popY = wordBounds.top - spaceH - popWindowHeight; // 1. 上面
        } else {
            popY = wordBounds.top + lineH + spaceH; //2. 下面
        }
        popY += getStatusBarHeight();

        popupWindow.setAnimationStyle(R.style.Animation_AppCompat_Dialog);
        popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, (int) popX, (int) popY);
    }

    private void dismissPopUpWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private int findTargetLine(float actionY) {
        int targetLine = -1;
        float minY = pageSpan.getPaintInfo().getPaddingTop();
        float maxY = minY;
        float spaceH = pageSpan.getPaintInfo().getSpaceH();
        float lineH = pageSpan.getLineH();
        int lines = pageSpan.getLineSize();

        for (int i = 0; i < lines; i++) {
            maxY += lineH;
            if (actionY >= minY && actionY <= maxY) {
                targetLine = i;
                break;
            }
            minY = maxY + spaceH;
            maxY = minY;
        }
        return targetLine;
    }

    private int findTargetWord(float actionX, LineSpan lineSpan) {
        int targetWord = -1;
        float minX = pageSpan.getPaintInfo().getPaddingLeft();
        float maxX = minX;

        float spaceW = lineSpan.getSpaceW();
        List<WordSpan> wordSpanList = lineSpan.getLineSpan();
        int words = lineSpan.getWordSize();
        for (int i = 0; i < words; i++) {
            maxX += wordSpanList.get(i).getWidth();
            if (actionX >= minX && actionX <= maxX) {
                targetWord = i;
                break;
            }
            minX = maxX + spaceW;
            maxX = minX;
        }
        return targetWord;
    }

    private WordBounds getWordBounds(int targetLine, int targetWord) {
        WordBounds wordBounds = new WordBounds();

        float spaceH = pageSpan.getPaintInfo().getSpaceH();
        float lineH = pageSpan.getLineH();

        LineSpan lineSpan = pageSpan.getLineSpanList().get(targetLine);
        WordSpan wordSpan = lineSpan.getLineSpan().get(targetWord);

        float top = targetLine * spaceH + targetLine * lineH + pageSpan
                .getPaintInfo().getPaddingTop();
        float bottom = top + lineH;
        float left = pageSpan.getPaintInfo().getPaddingLeft();
        for (int i = 0; i < targetWord; i++) {
            left += lineSpan.getLineSpan().get(i).getWidth();
            left += lineSpan.getSpaceW();
        }
        float right = left + wordSpan.getWidth();

        wordBounds.left = left;
        wordBounds.top = top;
        wordBounds.right = right;
        wordBounds.bottom = bottom;

        return wordBounds;
    }

    private WordSpan getTargetWordSpan(int targetLine, int targetWord) {
        LineSpan lineSpan = pageSpan.getLineSpanList().get(targetLine);
        WordSpan wordSpan = lineSpan.getLineSpan().get(targetWord);
        return wordSpan;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setPageSpan(PageSpan pageSpan) {
        this.pageSpan = pageSpan;
        invalidate();
    }

    private class WordBounds {
        float left;
        float right;
        float top;
        float bottom;
    }
}
