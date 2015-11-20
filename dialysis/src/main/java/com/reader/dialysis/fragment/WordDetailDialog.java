package com.reader.dialysis.fragment;


import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.reader.dialysis.Model.AVWord;
import com.reader.dialysis.Model.Word;
import com.reader.dialysis.util.JsonUtil;

import java.util.List;

import test.dorothy.graduation.activity.R;

/**
 * Created by dorothy on 15/5/17.
 */
public class WordDetailDialog extends DialogFragment implements View.OnClickListener {

    private Word mWord;
    private AVWord mAvWord;
    private int mIndex;

    private TextView mTvWord;
    private TextView mTvPron;
    private Button mBtnDelete;
    private Button mBtnMaster;
    private LinearLayout mDefContainer;
    private WordCallBack mCallBack;

    public interface WordCallBack {
        void deleteWord(int index);

        void masterWord(int index);
    }

    public static WordDetailDialog newInstance(Word word, int index, String objId) {
        WordDetailDialog dialog = new WordDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putString("word", JsonUtil.toJson(word));
        bundle.putInt("index", index);
        bundle.putString("obj_id", objId);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialog_word_detail, container);
        mWord = JsonUtil.createModel(getArguments().getString("word"), Word.class);
        String objId = getArguments().getString("obj_id");
        try {
            mAvWord = AVObject.createWithoutData(AVWord.class, objId);
        } catch (AVException e) {
            e.printStackTrace();
        }
        mIndex = getArguments().getInt("index");
        mTvWord = (TextView) rootView.findViewById(R.id.word);
        mTvPron = (TextView) rootView.findViewById(R.id.pron);
        mBtnDelete = (Button) rootView.findViewById(R.id.delete);
        mBtnMaster = (Button) rootView.findViewById(R.id.master);
        mDefContainer = (LinearLayout) rootView.findViewById(R.id.word_def);

        mBtnDelete.setOnClickListener(this);
        mBtnMaster.setOnClickListener(this);

        mTvWord.setText(mWord.getWord());
        mTvPron.setText(Html.fromHtml("[" + mWord.getPronunciation().getAll() + "]"));

        renderDefs();

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        return super.onCreateDialog(savedInstanceState);
    }

    private void renderDefs() {
        if (mWord.getResults() == null || mWord.getResults().size() <= 0) {
            return;
        }
        int highLightColor = Resources.getSystem().getColor(android.R.color.holo_blue_bright);

        List<Word.Definition> definitionList = mWord.getResults();
        for (int i = 0; i < definitionList.size(); i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_word_def, null);
            Word.Definition definition = definitionList.get(i);
            TextView tvPartOf = (TextView) view.findViewById(R.id.partofspeech);
            SpannableString span = new SpannableString((i + 1) + "." + definition.getPartOfSpeech
                    ());
            span.setSpan(new ForegroundColorSpan(highLightColor), 0, 1, Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
            tvPartOf.setText(span);

            TextView tvDef = (TextView) view.findViewById(R.id.def);
            tvDef.setText(definition.getDefinition());

            List<String> examples = definition.getExamples();
            TextView tvExample = (TextView) view.findViewById(R.id.example);
            if (examples != null && !examples.isEmpty()) {
                SpannableString exampleSpan = new SpannableString("eg: " + examples.get(0));
                exampleSpan.setSpan(new ForegroundColorSpan(highLightColor), 0, 3, Spanned
                        .SPAN_EXCLUSIVE_EXCLUSIVE);
                tvExample.setText(exampleSpan);
            } else {
                tvExample.setVisibility(View.GONE);
            }
            mDefContainer.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        if (mAvWord == null) {
            return;
        }
        if (v.getId() == R.id.delete) {
            deleteWord();
        } else if (v.getId() == R.id.master) {
            masterWord();
        }
    }

    private void deleteWord() {
        if (mCallBack != null) {
            mCallBack.deleteWord(mIndex);
        }
        getDialog().dismiss();
    }

    private void masterWord() {
        if (mCallBack != null) {
            mCallBack.masterWord(mIndex);
        }
        getDialog().dismiss();
    }

    public void setWordCallBack(WordCallBack wordCallBack) {
        mCallBack = wordCallBack;
    }
}
