package com.reader.dialysis.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.reader.dialysis.Model.AVBook;
import com.reader.dialysis.Model.AVUserBook;
import com.reader.dialysis.Model.Book;
import com.reader.dialysis.adapter.AllBookAdapter;
import com.reader.dialysis.adapter.AllBookAdapter.AddBookCallBack;
import com.reader.dialysis.fragment.UserBookFragment;
import com.reader.dialysis.util.UserCache;
import com.reader.dialysis.view.IndicatorWrapper;

import java.util.ArrayList;
import java.util.List;

import test.dorothy.graduation.activity.R;

public class AllBookActivity extends DialysisActivity implements AddBookCallBack {

    private ListView mAllBookListView;
    private AllBookAdapter mAdapter;
    private IndicatorWrapper mIndicatorWrapper;

    private List<AVUserBook> mUserBookList = new ArrayList<>();
    private List<AVBook> mBookList = new ArrayList<>();
    private List<Book> mBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIndicatorWrapper = (IndicatorWrapper) findViewById(R.id.indicator_wrapper);
        mAllBookListView = (ListView) findViewById(R.id.all_book);
        mAdapter = new AllBookAdapter(this);
        mAllBookListView.setAdapter(mAdapter);

        fetchUserBook();
    }

    private void fetchAllBook() {
        AVQuery<AVBook> query = new AVQuery<>("Book");
        showIndicatorWrapper();
        query.findInBackground(new FindCallback<AVBook>() {
            @Override
            public void done(List<AVBook> list, AVException e) {
                if (e == null) {
                    mBookList = list;
                    mBooks = buildBookList(mBookList, mUserBookList);
                    mAdapter.setData(mBooks);
                } else {
                    showToast(e.getMessage());
                }
                hideIndicatorWrapper();
            }
        });
    }

    private void fetchUserBook() {
        int userId = UserCache.getUserId(this);
        AVQuery<AVUserBook> query = new AVQuery<>("UserBook");
        query.whereEqualTo("user_id", userId);
        query.findInBackground(new FindCallback<AVUserBook>() {
            @Override
            public void done(List<AVUserBook> list, AVException e) {
                if (e == null) {
                    if (list != null) {
                        mUserBookList = list;
                    }
                    fetchAllBook();
                } else {
                    showToast(e.getMessage());
                }
            }
        });
    }

    private List<Book> buildBookList(List<AVBook> avBookList, List<AVUserBook> avUserBookList) {
        List<Book> bookList = new ArrayList<>();
        List<Integer> userBookIds = new ArrayList<Integer>();
        for (AVUserBook avUserBook : avUserBookList) {
            userBookIds.add(avUserBook.getBookId());
        }

        for (AVBook avBook : avBookList) {
            Book book = new Book(avBook);
            if (userBookIds.contains(avBook.getBookId())) {
                book.setIsAdded(true);
            }
            bookList.add(book);
        }
        return bookList;
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
    public void addBook(int pos) {
        if (pos < 0 || pos >= mBookList.size()) {
            return;
        }
        AVBook avBook = mBookList.get(pos);
        int userId = UserCache.getUserId(this);
        AVUserBook avUserBook = new AVUserBook(userId, avBook.getBookId(),avBook.getTitle(), avBook.getAuthor(),
                avBook.getCoverUrl());
        avUserBook.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("收藏成功！");
                    setResult(UserBookFragment.RESULT_CODE_ADD_BOOK);
                    finish();
                } else {
                    showToast(e.getMessage());
                }
            }
        });
    }
}
