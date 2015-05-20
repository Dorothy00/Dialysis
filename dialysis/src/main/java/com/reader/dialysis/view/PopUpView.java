package com.reader.dialysis.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.SaveCallback;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.reader.dialysis.Model.AVWord;
import com.reader.dialysis.Model.SimpleWord;
import com.reader.dialysis.Model.Word;
import com.reader.dialysis.util.JsonUtil;

import org.apache.http.Header;

import java.util.List;

import test.dorothy.graduation.activity.R;

/**
 * Created by dorothy on 15/5/16.
 */
public class PopUpView extends LinearLayout implements View.OnClickListener {

    private TextView mTvWord;
    private TextView mTvPronunciation;
    private TextView mTvDefinition;
    private TextView mTvExample;
    private ImageButton mIbAddWord;

    private IndicatorWrapper mIndicatorWrapper;
    private Context mContext;

    private Word mWord;
    private SimpleWord mSimpleWord;
    private boolean mIsAdded;
    private int mUserId;

    private static final String mashpeKey = "hCRt9XvzkImshtZFHJPdbyx6GbKlp1NoAJXjsnnznhkqbXBBpl";
    private String url = "https://wordsapiv1.p.mashape.com/words/{word}?mashape-key=" + mashpeKey;
    private AsyncHttpClient mClient;

    public PopUpView(Context context) {
        this(context, null);
    }

    public PopUpView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopUpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_popup, this);
        mContext = context;
        mClient = new AsyncHttpClient();
        mTvWord = (TextView) findViewById(R.id.word);
        mTvPronunciation = (TextView) findViewById(R.id.pronunciation);
        mTvDefinition = (TextView) findViewById(R.id.definition);
        mTvExample = (TextView) findViewById(R.id.example);
        mIbAddWord = (ImageButton) findViewById(R.id.add);
        mIndicatorWrapper = (IndicatorWrapper) findViewById(R.id.indicator_wrapper);

        mIbAddWord.setOnClickListener(this);
    }

    public void fetchWord(final String word, int userId) {
        showIndicatorWrapper();
        mUserId = userId;
        mIbAddWord.setVisibility(View.VISIBLE);
        mSimpleWord = null;
        mWord = null;
        mIsAdded = false;
        AVQuery.doCloudQueryInBackground("select * from Word where user_id=? and word=?", new
                CloudQueryCallback<AVCloudQueryResult>() {


                    @Override
                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                        if (e == null) {
                            if (avCloudQueryResult.getResults().size() > 0) {
                                AVWord avWord = (AVWord) avCloudQueryResult.getResults().get(0);
                                mWord = avWord.getWordObj();
                                mIsAdded = true;
                                renderView(mWord, true);
                                hideIndicatorWrapper();
                            } else {
                                mIsAdded = false;
                                searchWord(word);
                            }
                        } else {
                            hideIndicatorWrapper();
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }, AVWord.class, userId, word);
    }

    public void searchWord(String word) {
        if (TextUtils.isEmpty(word)) {
            return;
        }
        String search_url = url.replace("{word}", word.trim());
        mClient.get(mContext, search_url, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                hideIndicatorWrapper();
                Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideIndicatorWrapper();
                try {
                    mWord = JsonUtil.createModel(responseString, Word.class);
                    renderView(mWord, false);
                } catch (Exception e) {
                    mSimpleWord = JsonUtil.createModel(responseString, SimpleWord.class);
                    renderSimpleView(mSimpleWord);
                }

            }
        });
    }

    private void renderView(Word word, boolean isAdded) {
        int resId = R.drawable.selector_add_word;
        if (isAdded) {
            resId = R.drawable.ic_done_white;
        }
        mIbAddWord.setBackgroundResource(resId);
        mTvWord.setText(word.getWord());
        mTvPronunciation.setText(Html.fromHtml("[" + word.getPronunciation().getAll() + "]"));
        List<Word.Definition> definitions = word.getResults();
        mTvExample.setText("");
        mTvDefinition.setText("");
        if (definitions != null && !definitions.isEmpty()) {
            Word.Definition definition = definitions.get(0);
            int highLightColor = Resources.getSystem().getColor(android.R.color.holo_blue_light);
            SpannableString defSpan = new SpannableString("def: " + definition.getDefinition());
            defSpan.setSpan(new ForegroundColorSpan(highLightColor), 0, 4, Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvDefinition.setText(defSpan);
            if (definition.getExamples() != null && !definition.getExamples().isEmpty()) {
                SpannableString egSpan = new SpannableString("eg: " + definition.getExamples()
                        .get(0));
                egSpan.setSpan(new ForegroundColorSpan(highLightColor), 0, 3, Spanned
                        .SPAN_EXCLUSIVE_EXCLUSIVE);
                mTvExample.setText(egSpan);
            }
        }
    }

    private void renderSimpleView(SimpleWord simpleWord) {
        mIbAddWord.setVisibility(View.INVISIBLE);
        mTvWord.setText(simpleWord.getWord());
        mTvPronunciation.setText(Html.fromHtml("[" + simpleWord.getPronunciation() + "]"));
        mTvExample.setText("");
        mTvDefinition.setText("");
    }

    private void showIndicatorWrapper() {
        if (mIndicatorWrapper != null) {
            mIndicatorWrapper.showIndicator();
        }
    }

    private void hideIndicatorWrapper() {
        if (mIndicatorWrapper != null) {
            mIndicatorWrapper.hideIndicator();
        }
    }

    @Override
    public void onClick(View v) {
        if (mWord == null || mIsAdded) {
            return;
        }
        AVWord avWord = new AVWord(mUserId, mWord);
        avWord.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    mIbAddWord.setBackgroundResource(R.drawable.ic_done_white);
                    Toast.makeText(mContext, "单词已添加到单词本", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "单词添加失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
