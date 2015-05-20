package com.reader.dialysis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.reader.dialysis.Model.AVUserBook;
import com.reader.dialysis.view.IndicatorWrapper;
import com.squareup.picasso.Picasso;

import java.util.List;

import test.dorothy.graduation.activity.R;

public class FinishReadingFragment extends DialysisFragment {

    private int mBookId;
    private ImageView mIvCover;
    private IndicatorWrapper mIndicatorWrapper;

    public static FinishReadingFragment newInstance(int bookId) {
        FinishReadingFragment fragment = new FinishReadingFragment();
        Bundle args = new Bundle();
        args.putInt("book_id", bookId);
        fragment.setArguments(args);

        return fragment;
    }

    public FinishReadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBookId = getArguments().getInt("book_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finish_reading, container, false);
        mIvCover = (ImageView) rootView.findViewById(R.id.cover);
        mIndicatorWrapper = (IndicatorWrapper) rootView.findViewById(R.id.indicator_wrapper);
        fetchUserBook();

        return rootView;
    }

    private void fetchUserBook() {
        AVQuery<AVUserBook> query = new AVQuery<>("UserBook");
        query.whereEqualTo("book_id", mBookId);
        mIndicatorWrapper.showIndicator();
        query.findInBackground(new FindCallback<AVUserBook>() {
            @Override
            public void done(List<AVUserBook> list, AVException e) {
                if (e == null) {
                    AVUserBook avUserBook = list.get(0);
                    AVFile avFile = avUserBook.getCoverUrl();
                    Picasso.with(getActivity()).load(avFile.getUrl()).into(mIvCover);
                }
                mIndicatorWrapper.hideIndicator();
            }
        });
    }
}
