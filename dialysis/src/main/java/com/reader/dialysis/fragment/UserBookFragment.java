package com.reader.dialysis.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.reader.dialysis.Model.AVUserBook;
import com.reader.dialysis.activity.ReaderActivity;
import com.reader.dialysis.adapter.UserbookAdapter;

import java.util.ArrayList;
import java.util.List;

import test.dorothy.graduation.activity.R;

public class UserBookFragment extends DialysisFragment implements AdapterView.OnItemClickListener{

    private GridView mUserbookGrid;
    private UserbookAdapter mUserbookAdapter;
    private List<AVUserBook> mAVUserBookList = new ArrayList<AVUserBook>();

    public UserBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_book, container, false);
        mUserbookGrid = (GridView) rootView.findViewById(R.id.userbook_grid);
        mUserbookAdapter = new UserbookAdapter(getActivity());
        mUserbookGrid.setAdapter(mUserbookAdapter);
        mUserbookGrid.setOnItemClickListener(this);

        fetchUserBooks();

        return rootView;
    }

    public void fetchUserBooks() {
        AVQuery<AVUserBook> query = new AVQuery<AVUserBook>("UserBook");
        query.findInBackground(new FindCallback<AVUserBook>() {
            @Override
            public void done(List<AVUserBook> list, AVException e) {
                if(e==null && list!=null){
                    mAVUserBookList = list;
                    mUserbookAdapter.setData(list);
                }else{
                    showToast(e.getMessage());
                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position<0 || position>= mAVUserBookList.size()){
            return;
        }
        AVUserBook AVUserBook = mAVUserBookList.get(position);
        // TODO ALL BOOK
        if(AVUserBook.getBookId()==2){
            // TODO render
            startActivity(ReaderActivity.createIntent(getActivity(),2,1));
        }

    }
}
