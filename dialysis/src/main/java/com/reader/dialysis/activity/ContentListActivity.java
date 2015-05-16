package com.reader.dialysis.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.reader.dialysis.Model.Content;
import com.reader.dialysis.adapter.ContentAdapter;
import com.reader.dialysis.util.JsonUtil;

import java.util.List;

import test.dorothy.graduation.activity.R;

public class ContentListActivity extends AppCompatActivity implements AdapterView
        .OnItemClickListener {

    private List<Content> mContentList;
    private ListView mContentListView;
    private ContentAdapter mContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);

        mContentListView = (ListView) findViewById(R.id.listview);
        int chapterId = getIntent().getIntExtra("chapter_id", -1);
        String contentsStr = getIntent().getStringExtra("content_list");
        mContentList = JsonUtil.createList(contentsStr, Content.class);
        mContentAdapter = new ContentAdapter(this);
        mContentListView.setAdapter(mContentAdapter);
        mContentListView.setOnItemClickListener(this);
        mContentAdapter.setData(mContentList, chapterId);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 0 || position >= mContentList.size()) {
            return;
        }
        Intent i = new Intent();
        i.putExtra("chapter_id", mContentList.get(position).getChapterId());
        setResult(ReaderActivity.RESULT_CODE_CONTENTS, i);
        finish();
    }

    public static Intent createIntent(Context context, List<Content> contentList, int chapterId) {
        Intent intent = new Intent(context, ContentListActivity.class);
        String listStr = JsonUtil.toJson(contentList);
        intent.putExtra("content_list", listStr);
        intent.putExtra("chapter_id", chapterId);
        return intent;
    }
}
