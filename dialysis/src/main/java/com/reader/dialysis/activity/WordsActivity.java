package com.reader.dialysis.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.reader.dialysis.Model.AVWord;
import com.reader.dialysis.fragment.WordDetailDialog;
import com.reader.dialysis.util.UserCache;
import com.reader.dialysis.view.IndicatorWrapper;

import java.util.ArrayList;
import java.util.List;

import test.dorothy.graduation.activity.R;


public class WordsActivity extends DialysisActivity implements View.OnClickListener,
        WordDetailDialog.WordCallBack {

    private IndicatorWrapper mIndicatorWrapper;
    private LinearLayout mWordsContainer;
    private Button[] btnList;
    private List<AVWord> mWordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_words);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mIndicatorWrapper = (IndicatorWrapper) findViewById(R.id.indicator_wrapper);
        mWordsContainer = (LinearLayout) findViewById(R.id.words_container);

        int userId = UserCache.getUserId(this);
        fetchWords(userId);
    }

    private void fetchWords(int userId) {
        showIndicatorWrapper();
        AVQuery<AVWord> avQuery = new AVQuery<>("Word");
        avQuery.whereEqualTo("user_id", userId);
        avQuery.whereEqualTo("master", false);
        avQuery.findInBackground(new FindCallback<AVWord>() {
            @Override
            public void done(List<AVWord> list, AVException e) {
                if (e == null) {
                    mWordList.clear();
                    if (list.size() > 0) {
                        mWordList.addAll(list);
                        renderView(mWordList);
                    }
                } else {
                    showToast(e.getMessage());
                }
                hideIndicator();
            }
        });

    }

    private void renderView(List<AVWord> wordList) {
        btnList = new Button[wordList.size()];
        for (int i = 0; i < wordList.size(); i += 2) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_word_row, null);
            Button btnWordLeft = (Button) view.findViewById(R.id.left_word);
            Button btnWordRight = (Button) view.findViewById(R.id.right_word);
            btnWordLeft.setText(wordList.get(i).getWord());
            btnList[i] = btnWordLeft;
            btnWordLeft.setOnClickListener(this);
            if (i + 1 < wordList.size()) {
                btnWordRight.setText(wordList.get(i + 1).getWord());
                btnList[i + 1] = btnWordRight;
                btnWordRight.setOnClickListener(this);
            }
            mWordsContainer.addView(view);
        }
        int count = mWordsContainer.getChildCount();
        View childView = mWordsContainer.getChildAt(count - 1);
        childView.findViewById(R.id.line).setVisibility(View.GONE);
    }

    private void refreshView(int index) {
        mWordList.remove(index);
        mWordsContainer.removeAllViews();
        renderView(mWordList);
    }

    @Override
    public void onClick(View v) {
        int size = btnList.length;
        for (int i = 0; i < size; i++) {
            if (v == btnList[i]) {
                WordDetailDialog dialog = WordDetailDialog.newInstance(mWordList.get(i).getWordObj
                        (), i, mWordList.get(i).getObjectId());
                dialog.setWordCallBack(this);
                dialog.show(getSupportFragmentManager(), "dialog");
                break;
            }
        }
    }

    @Override
    public void deleteWord(final int index) {
        if (index >= 0 && index < mWordList.size()) {
            AVWord avWord = mWordList.get(index);
            avWord.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        showToast("单词删除成功！");
                        refreshView(index);
                    } else {
                        showToast("删除失败！" + e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void masterWord(final int index) {
        if (index >= 0 && index < mWordList.size()) {
            AVWord avWord = mWordList.get(index);
            avWord.setMaster(true);
            avWord.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        showToast("单词已掌握！");
                        refreshView(index);
                    } else {
                        showToast(e.getMessage());
                    }
                }
            });
        }
    }

    private void showIndicatorWrapper() {
        if (mIndicatorWrapper != null) {
            mIndicatorWrapper.showIndicator();
        }
    }

    private void hideIndicator() {
        if (mIndicatorWrapper != null) {
            mIndicatorWrapper.hideIndicator();
        }
    }
}
