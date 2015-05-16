package com.reader.dialysis.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.google.gson.JsonParseException;
import com.reader.dialysis.Model.AVChapter;
import com.reader.dialysis.Model.AVTableContents;
import com.reader.dialysis.Model.Chapter;
import com.reader.dialysis.Model.Content;
import com.reader.dialysis.Model.PageSpan;
import com.reader.dialysis.Model.PaintInfo;
import com.reader.dialysis.fragment.ReaderFragment;
import com.reader.dialysis.util.DialysisSpanGenerator;
import com.reader.dialysis.util.DialysisXmlParser;
import com.reader.dialysis.util.JsonUtil;
import com.reader.dialysis.util.MiscUtil;
import com.reader.dialysis.view.IndicatorWrapper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;

import test.dorothy.graduation.activity.R;

public class ReaderActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CONTENTS = 0x33;
    public static final int RESULT_CODE_CONTENTS = 0x34;

    private IndicatorWrapper mIndicatorWrapper;
    private ViewPager mReaderViewPager;
    private ReaderPagerAdapter mReaderPagerAdapter;
    private List<PageSpan> mPageList;
    private int mBookId;
    private int mChapterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reader);

        mIndicatorWrapper = (IndicatorWrapper) findViewById(R.id.indicator_wrapper);
        mReaderViewPager = (ViewPager) findViewById(R.id.view_pager);
        mReaderPagerAdapter = new ReaderPagerAdapter
                (getSupportFragmentManager());
        mBookId = getIntent().getIntExtra("book_id", -1);
        mChapterId = getIntent().getIntExtra("chapter_id", -1);
        if (mBookId == -1 || mChapterId == -1) {
            return;
        }
        MiscUtil.forceShowOverLap(this);
        fetchChapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reader, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.content_list) {
            fetchTableContents(mBookId);
        }
        return super.onOptionsItemSelected(item);
    }

    private class ReaderPagerAdapter extends FragmentStatePagerAdapter {

        public ReaderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return ReaderFragment.newInstance(mPageList.get(pos));
        }

        @Override
        public int getCount() {
            return mPageList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
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
        paintInfo.setScreenH(height - result);
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

    private void fetchChapter() {
        showIndicatorWrapper();
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

    private void fetchChapterPara(AVFile avFile) {
        avFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, AVException e) {
                if (e == null) {
                    String contentXml = null;
                    try {
                        contentXml = IOUtils.toString(bytes, "utf-8");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (TextUtils.isEmpty(contentXml)) {
                        return;
                    }
                    Chapter chapter = new DialysisXmlParser().parser(contentXml);
                    mPageList = new DialysisSpanGenerator().setupPage(chapter,
                            setPaintInfo());
                    if (mReaderViewPager.getAdapter() == null) {
                        mReaderViewPager.setAdapter(mReaderPagerAdapter);
                    } else {
                        mReaderPagerAdapter.notifyDataSetChanged();
                    }
                }
                hideIndicatorWrapper();
            }
        });
    }

    private void fetchTableContents(int bookId) {
        showIndicatorWrapper();
        AVQuery<AVTableContents> query = new AVQuery<>("TableContents");
        query.whereEqualTo("book_id", bookId);
        query.findInBackground(new FindCallback<AVTableContents>() {
            @Override
            public void done(List<AVTableContents> list, AVException e) {
                if (e == null && list.size() > 0) {
                    AVTableContents avTableContents = list.get(0);
                    String contentsStr = avTableContents.getAVContentList();
                    try {
                        List<Content> contentList = JsonUtil.createList(contentsStr, Content.class);
                        Intent intent = ContentListActivity.createIntent(ReaderActivity.this,
                                contentList, mChapterId);
                        // TODO FOR RESULT
                        startActivityForResult(intent, REQUEST_CODE_CONTENTS);
                    } catch (JsonParseException e1) {
                        e1.printStackTrace();
                    }
                    showIndicatorWrapper();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CONTENTS && resultCode == RESULT_CODE_CONTENTS) {
            mChapterId = data.getIntExtra("chapter_id", -1);
            if (mChapterId != -1) {
                fetchChapter();
            }
        }
    }

    public static Intent createIntent(Context context, int bookId, int chapterId) {
        Intent i = new Intent(context, ReaderActivity.class);
        i.putExtra("book_id", bookId);
        i.putExtra("chapter_id", chapterId);
        return i;
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
}
