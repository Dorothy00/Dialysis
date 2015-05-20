package com.reader.dialysis.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.reader.dialysis.Model.AVWord;
import com.reader.dialysis.util.UserCache;

import java.util.List;

import test.dorothy.graduation.activity.R;


public class WordsFragment extends DialysisFragment implements View.OnClickListener {

    public static final int REQUEST_CODE_DIALOG = 0x11;
    public static final int RESULT_CODE_DIALOG = 0x12;

    private LinearLayout mWordsContainer;
    private Button[] btnList;
    private List<AVWord> mWordList;
    private AppCompatActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AppCompatActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_words, container, false);
        mWordsContainer = (LinearLayout) rootView.findViewById(R.id.words_container);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int userId = UserCache.getUserId(getActivity());
        fetchWords(userId);
    }

    private void fetchWords(int userId) {
        AVQuery<AVWord> avQuery = new AVQuery<>("Word");
        avQuery.whereEqualTo("user_id", userId);
        avQuery.whereDoesNotExist("master");
        avQuery.findInBackground(new FindCallback<AVWord>() {
            @Override
            public void done(List<AVWord> list, AVException e) {
                if (e == null) {
                    // FIXME null pointer expectiation
                    mWordList = list;
                    renderView(list);
                } else {
                    showToast(e.getMessage());
                }
            }
        });

    }

    private void renderView(List<AVWord> wordList) {
        btnList = new Button[wordList.size()];
        for (int i = 0; i < wordList.size(); i += 2) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_word_row, null);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DIALOG && resultCode == RESULT_CODE_DIALOG) {
            int index = data.getIntExtra("index", -1);
            if (index >= 0 && index < mWordList.size()) {
                mWordList.remove(index);
                mWordsContainer.removeAllViews();
                renderView(mWordList);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int size = btnList.length;
        for (int i = 0; i < size; i++) {
            if (v == btnList[i]) {
                WordDetailDialog dialog = WordDetailDialog.newInstance(mWordList.get(i).getWordObj
                        (), i, mWordList.get(i).getObjectId());
                dialog.setTargetFragment(this, REQUEST_CODE_DIALOG);
                dialog.show(mActivity.getSupportFragmentManager(), "dialog");
                break;
            }
        }
    }

}
