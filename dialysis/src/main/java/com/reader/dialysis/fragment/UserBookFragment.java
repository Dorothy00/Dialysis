package com.reader.dialysis.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.reader.dialysis.Model.AVUserBook;
import com.reader.dialysis.activity.AllBookActivity;
import com.reader.dialysis.activity.ReaderActivity;
import com.reader.dialysis.adapter.UserbookAdapter;
import com.reader.dialysis.util.UserCache;
import com.reader.dialysis.view.IndicatorWrapper;

import java.util.ArrayList;
import java.util.List;

import test.dorothy.graduation.activity.R;

public class UserBookFragment extends DialysisFragment implements AdapterView
        .OnItemClickListener, View.OnClickListener, UserbookAdapter.DeleteUserBookCallBack {

    public static final int REQUEST_CODE_ADD_BOOK = 0x23;
    public static final int RESULT_CODE_ADD_BOOK = 0x24;

    private IndicatorWrapper mIndicatorWrapper;
    private GridView mUserbookGrid;
    private ImageView mIvAdd;
    private UserbookAdapter mUserbookAdapter;
    private List<AVUserBook> mAVUserBookList = new ArrayList<>();
    private AlertDialog.Builder mDialogBuilder;

    public UserBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_book, container, false);
        mIndicatorWrapper = (IndicatorWrapper) rootView.findViewById(R.id.indicator_wrapper);
        mUserbookGrid = (GridView) rootView.findViewById(R.id.userbook_grid);
        mIvAdd = (ImageView) rootView.findViewById(R.id.add_book);
        mUserbookAdapter = new UserbookAdapter(getActivity(), this);
        mUserbookGrid.setAdapter(mUserbookAdapter);
        mUserbookGrid.setOnItemClickListener(this);
        mIvAdd.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchUserBooks();
    }

    public void fetchUserBooks() {
        showIndicatorWrapper();
        int userId = UserCache.getUserId(getActivity());
        AVQuery<AVUserBook> query = new AVQuery<AVUserBook>("UserBook");
        query.whereEqualTo("user_id", userId);
        query.findInBackground(new FindCallback<AVUserBook>() {
            @Override
            public void done(List<AVUserBook> list, AVException e) {
                if (e == null) {
                    renderView(list);
                } else {
                    showToast(e.getMessage());
                }
                hideIndicatorWrapper();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_BOOK && resultCode == RESULT_CODE_ADD_BOOK) {
            fetchUserBooks();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 0 || position >= mAVUserBookList.size()) {
            return;
        }
        AVUserBook AVUserBook = mAVUserBookList.get(position);
        // TODO ALL BOOK
        if (AVUserBook.getBookId() == 2) {
            // TODO render
            startActivity(ReaderActivity.createIntent(getActivity(), 2, 1));
        }
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(getActivity(), AllBookActivity.class),
                REQUEST_CODE_ADD_BOOK);
    }

    @Override
    public void delete(final int pos) {
        if (pos < 0 || pos >= mAVUserBookList.size()) {
            return;
        }
        showDialog(pos);
    }

    private void showDialog(final int pos) {
        mDialogBuilder = new AlertDialog.Builder(getActivity());
        mDialogBuilder.setMessage("确定删除这本书吗？");
        mDialogBuilder.setPositiveButton("删 除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBookInBackground(pos);
                dialog.dismiss();
            }
        });
        mDialogBuilder.setNegativeButton("取 消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialogBuilder.create().show();
    }

    private void deleteBookInBackground(final int pos) {
        AVUserBook avUserBook = mAVUserBookList.get(pos);
        showIndicatorWrapper();
        avUserBook.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    mAVUserBookList.remove(pos);
                    renderView(mAVUserBookList);

                } else {
                    showToast(e.getMessage());
                }
                hideIndicatorWrapper();
            }
        });
    }

    private void renderView(List<AVUserBook> list) {
        if (list == null || list.isEmpty()) {
            mUserbookGrid.setVisibility(View.GONE);
            mIvAdd.setVisibility(View.VISIBLE);
        } else {
            mUserbookGrid.setVisibility(View.VISIBLE);
            mIvAdd.setVisibility(View.GONE);
            mAVUserBookList = list;
            mUserbookAdapter.setData(list);
        }
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
