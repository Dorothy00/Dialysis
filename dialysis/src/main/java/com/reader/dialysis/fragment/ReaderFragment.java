package com.reader.dialysis.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.loopj.android.http.AsyncHttpClient;
import com.reader.dialysis.Model.LineSpan;
import com.reader.dialysis.Model.PageSpan;
import com.reader.dialysis.Model.WordBounds;
import com.reader.dialysis.Model.WordSpan;
import com.reader.dialysis.util.JsonUtil;
import com.reader.dialysis.view.PageView;
import com.reader.dialysis.view.PopUpView;

import java.util.List;

import test.dorothy.graduation.activity.R;

/**
 * @author dorothy
 */
public class ReaderFragment extends DialysisFragment {

    private PopupWindow popupWindow;
    private PopUpView mPopUpView;
    private int popWindowWidth;
    private int popWindowHeight;
    private int preTargetLine;
    private int preTargetWord;
    private WordBounds wordBounds;
    private PageView mPageView;
    private PageSpan mPageSpan;
    private int mUserId;

    private static final String mashpeKey = "hCRt9XvzkImshtZFHJPdbyx6GbKlp1NoAJXjsnnznhkqbXBBpl";
    private String url = "https://wordsapiv1.p.mashape.com/words/{word}?mashape-key=" + mashpeKey;
    private AsyncHttpClient mClient;

    public static ReaderFragment newInstance(int userId, PageSpan pageSpan) {
        ReaderFragment fragment = new ReaderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("page_span", JsonUtil.toJson(pageSpan));
        bundle.putInt("user_id", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ReaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPopUpView = new PopUpView(getActivity());
        mClient = new AsyncHttpClient();
        String pageSpanStr = getArguments().getString("page_span");
        mPageSpan = JsonUtil.createModel(pageSpanStr, PageSpan.class);
        mUserId = getArguments().getInt("user_id");
        popWindowWidth = (int) getResources().getDimension(R.dimen.width120);
        popWindowHeight = (int) getResources().getDimension(R.dimen.height90);
        popupWindow = new PopupWindow(mPopUpView, popWindowWidth, popWindowHeight);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.screenBrightness = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reader, container, false);
        mPageView = (PageView) rootView.findViewById(R.id.page_view);
        mPageView.setPageSpan(mPageSpan);

        mPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dismissPopUpWindow();
                        // 将上一个被选中的wordSpan的状态置为未选中
                        mPageView.invalidateWordSpan(getWordBounds(preTargetLine, preTargetWord),
                                false);

                        float downX = event.getX();
                        float downY = event.getY();

                        int targetLine = findTargetLine(downY);
                        int targetWord = -1;
                        if (targetLine != -1) {
                            targetWord = findTargetWord(downX, mPageSpan.getLineSpanList().get
                                    (targetLine));
                        }
                        if (targetLine != -1 && targetWord != -1) {
                            wordBounds = getWordBounds(targetLine, targetWord);
                            preTargetLine = targetLine;
                            preTargetWord = targetWord;
                        }
                        break;
                }
                return false;
            }
        });

        mPageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (wordBounds != null) {
                    mPageView.invalidateWordSpan(wordBounds, true);
                    showPopupWindow(wordBounds);
                    wordBounds = null;
                }
                return false;
            }
        });

        mPageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
                if (compatActivity.getSupportActionBar().isShowing()) {
                    compatActivity.getSupportActionBar().hide();
                } else {
                    compatActivity.getSupportActionBar().show();
                }
            }
        });

        return rootView;
    }

    private void showPopupWindow(WordBounds wordBounds) {
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
        mPopUpView.fetchWord(wordBounds.getWordSpan().getWord(),mUserId);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissPopUpWindow();
        WordSpan wordSpan = mPageSpan.getLineSpanList().get(preTargetLine).getLineSpan().get
                (preTargetWord);
        wordSpan.setSelected(false);
    }
}
