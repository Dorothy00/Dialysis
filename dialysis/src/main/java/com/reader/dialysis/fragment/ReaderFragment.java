package com.reader.dialysis.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reader.dialysis.Model.PageSpan;
import com.reader.dialysis.activity.ReaderActivity;
import com.reader.dialysis.view.PageView;

import test.dorothy.graduation.activity.R;

/**
 * @author dorothy
 */
public class ReaderFragment extends Fragment {

    private PageView mPageView;
    private PageSpan mPageSpan;
    private ReaderActivity mActivity;

    public static ReaderFragment newInstance(int pos){
        ReaderFragment fragment = new ReaderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos",pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ReaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ReaderActivity)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reader, container, false);
        mPageView = (PageView)rootView.findViewById(R.id.page_view);
        mPageSpan = mActivity.getPageSpan(getArguments().getInt("pos"));
        mPageView.setPageSpan(mPageSpan);

        return rootView;
    }


}
