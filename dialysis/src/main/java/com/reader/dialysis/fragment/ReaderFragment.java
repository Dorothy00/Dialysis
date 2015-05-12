package com.reader.dialysis.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.reader.dialysis.Model.LineSpan;
import com.reader.dialysis.Model.PageSpan;
import com.reader.dialysis.Model.WordBounds;
import com.reader.dialysis.Model.WordSpan;
import com.reader.dialysis.activity.ReaderActivity;
import com.reader.dialysis.view.PageView;

import java.util.List;

import test.dorothy.graduation.activity.R;

/**
 * @author dorothy
 */
public class ReaderFragment extends Fragment {

    private PopupWindow popupWindow;
    private int popWindowWidth;
    private int popWindowHeight;
    private int preTargetLine;
    private int preTargetWord;
    private PageView mPageView;
    private PageSpan mPageSpan;
    private ReaderActivity mActivity;

    public static ReaderFragment newInstance(int pos) {
        ReaderFragment fragment = new ReaderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ReaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ReaderActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View popView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popup, null);
        popWindowWidth = (int) getResources().getDimension(R.dimen.width40);
        popWindowHeight = (int) getResources().getDimension(R.dimen.height40);
        popupWindow = new PopupWindow(popView, popWindowWidth, popWindowHeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reader, container, false);
        mPageView = (PageView) rootView.findViewById(R.id.page_view);
        mPageSpan = mActivity.getPageSpan(getArguments().getInt("pos"));
        mPageView.setPageSpan(mPageSpan);

        mPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dismissPopUpWindow();
                    // 将上一个被选中的wordSpan的状态置为未选中
                    mPageView.invalidateWordSpan(getWordBounds(preTargetLine, preTargetWord),
                            false);

                    float actionX = event.getX();
                    float actionY = event.getY();

                    int targetLine = findTargetLine(actionY);
                    int targetWord = -1;
                    if (targetLine != -1) {
                        targetWord = findTargetWord(actionX, mPageSpan.getLineSpanList().get
                                (targetLine));
                    }
                    if (targetLine != -1 && targetWord != -1) {
                        showPopupWindow(targetLine, targetWord);
                        mPageView.invalidateWordSpan(getWordBounds(targetLine, targetWord), true);
                        preTargetLine = targetLine;
                        preTargetWord = targetWord;
                    }
                }
                return false;
            }
        });

        return rootView;
    }


    private void showPopupWindow(int targetLine, int targetWord) {
        WordBounds wordBounds = getWordBounds(targetLine, targetWord);
        float spaceH = mPageSpan.getPaintInfo().getSpaceH();
        float lineH = mPageSpan.getLineH();

        WordSpan wordSpan = wordBounds.getWordSpan();

        float leftDis = wordBounds.getLeft() + wordSpan.getWidth() / 2;
        float rightDis = mPageSpan.getPaintInfo().getScreenW() - wordBounds.getRight() + wordSpan
                .getWidth() / 2;
        float popX;
        float popY;
        if (popWindowWidth / 2 <= leftDis && popWindowWidth / 2 <= rightDis) {
            // 1. 中间
            popX = leftDis - popWindowWidth / 2;
        } else if (popWindowWidth / 2 <= leftDis) {
            // 2. 右边
            popX = mPageSpan.getPaintInfo().getScreenW() - mPageSpan.getPaintInfo().getPaddingRight
                    () / 2 - popWindowWidth;
        } else {
            // 3. 左边
            popX = mPageSpan.getPaintInfo().getPaddingLeft() / 2;
        }

        float topDis = wordBounds.getTop() + lineH / 2;
        if (topDis - spaceH >= popWindowHeight) {
            popY = wordBounds.getTop() - spaceH - popWindowHeight; // 1. 上面
        } else {
            popY = wordBounds.getTop() + lineH + spaceH; //2. 下面
        }
        popY += getStatusBarHeight();

        popupWindow.setAnimationStyle(R.style.Animation_AppCompat_Dialog);
        popupWindow.showAtLocation(mPageView, Gravity.NO_GRAVITY, (int) popX, (int) popY);
    }

    private void dismissPopUpWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private int findTargetLine(float actionY) {
        int targetLine = -1;
        float minY = mPageSpan.getPaintInfo().getPaddingTop();
        float maxY = minY;
        float spaceH = mPageSpan.getPaintInfo().getSpaceH();
        float lineH = mPageSpan.getLineH();
        int lines = mPageSpan.getLineSize();

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
        float minX = mPageSpan.getPaintInfo().getPaddingLeft();
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

        float spaceH = mPageSpan.getPaintInfo().getSpaceH();
        float lineH = mPageSpan.getLineH();

        LineSpan lineSpan = mPageSpan.getLineSpanList().get(targetLine);
        WordSpan wordSpan = lineSpan.getLineSpan().get(targetWord);

        float top = targetLine * spaceH + targetLine * lineH + mPageSpan
                .getPaintInfo().getPaddingTop();
        float bottom = top + lineH;
        float left = mPageSpan.getPaintInfo().getPaddingLeft();
        for (int i = 0; i < targetWord; i++) {
            left += lineSpan.getLineSpan().get(i).getWidth();
            left += lineSpan.getSpaceW();
        }
        float right = left + wordSpan.getWidth();

        wordBounds.setLeft(left);
        wordBounds.setTop(top);
        wordBounds.setRight(right);
        wordBounds.setBottom(bottom);
        wordBounds.setWordSpan(wordSpan);

        return wordBounds;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
