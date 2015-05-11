package com.reader.dialysis.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.GetDataCallback;
import com.reader.dialysis.Model.AVChapter;
import com.reader.dialysis.Model.Chapter;
import com.reader.dialysis.Model.PageSpan;
import com.reader.dialysis.Model.PaintInfo;
import com.reader.dialysis.fragment.ReaderFragment;
import com.reader.dialysis.util.DialysisSpanGenerator;
import com.reader.dialysis.util.DialysisXmlParser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;

import test.dorothy.graduation.activity.R;

public class ReaderActivity extends FragmentActivity {

    private ViewPager mReaderViewPager;
    private List<PageSpan> mPageList;
    private int mBookId;
    private int mChapterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        mReaderViewPager = (ViewPager) findViewById(R.id.view_pager);
        mBookId = getIntent().getIntExtra("book_id", -1);
        mChapterId = getIntent().getIntExtra("chapter_id", -1);
        if (mBookId == -1 || mChapterId == -1) {
            return;
        }

        fetchChapter();
    }



    public PageSpan getPageSpan(int pos) {
        return mPageList.get(pos);
    }

    private class ReaderPagerAdapter extends FragmentStatePagerAdapter {

        public ReaderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ReaderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mPageList.size();
        }
    }

    private PaintInfo setPaintInfo() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;


            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }

        PaintInfo paintInfo = new PaintInfo();
        paintInfo.setScreenW(width);
        paintInfo.setScreenH(height-result);
        paintInfo.setSpaceH((int) getResources().getDimension(R.dimen.height8));
        paintInfo.setSpaceW((int) getResources().getDimension(R.dimen.width4));
        paintInfo.setPaddingTop((int) getResources().getDimension(R.dimen.padding10));
        paintInfo.setPaddingBottom((int) getResources().getDimension(R.dimen.padding10));
        paintInfo.setPaddingLeft((int) getResources().getDimension(R.dimen.padding5));
        paintInfo.setPaddingRight((int) getResources().getDimension(R.dimen.padding5));
        paintInfo.setTextSize(getResources().getDimension(R.dimen.textsize16));
        paintInfo.setForegroundColor(Color.BLACK);
        paintInfo.setBackgroundColor(Color.RED);

        return paintInfo;
    }

    private void fetchChapter(){
        AVQuery.doCloudQueryInBackground("select include content_xml,* from Chapter where " +
                "book_id=? and " +
                "chapter_id=?", new CloudQueryCallback<AVCloudQueryResult>() {

            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if (e == null) {
                    AVObject avObject = avCloudQueryResult.getResults().get(0);
                    AVFile avFile = avObject.getAVFile("content_xml");
                    fetchChapterPara(avFile);
                } else {
                    Toast.makeText(ReaderActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            }
        }, AVChapter.class, mBookId, mChapterId);
    }

    private void fetchChapterPara(AVFile avFile){
        avFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, AVException e) {
                if (e == null) {
                    try {
                        String contentXml = IOUtils.toString(bytes, "utf-8");
                        Chapter chapter = new DialysisXmlParser().parser(contentXml);
                        mPageList = new DialysisSpanGenerator().setupPage(chapter,
                                setPaintInfo());
                        mReaderViewPager.setAdapter(new ReaderPagerAdapter
                                (getSupportFragmentManager()));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("reader down----", "reader down------");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("reader up----", "reader up------");
                break;

        }
        return super.onTouchEvent(event);
    }

    public static Intent createIntent(Context context, int bookId, int chapterId) {
        Intent i = new Intent(context, ReaderActivity.class);
        i.putExtra("book_id", bookId);
        i.putExtra("chapter_id", chapterId);
        return i;
    }
}
