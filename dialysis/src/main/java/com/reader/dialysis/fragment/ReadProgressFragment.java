package com.reader.dialysis.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.reader.dialysis.Model.AVReadTime;
import com.reader.dialysis.Model.AVWord;
import com.reader.dialysis.util.UserCache;
import com.reader.dialysis.view.IndicatorWrapper;
import com.reader.dialysis.view.ReadProgressView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import test.dorothy.graduation.activity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadProgressFragment extends DialysisFragment {

    private TextView mTvTotalReadingTime;
    private TextView mTvActiveWords;
    private TextView mTvMasterWords;
    private ReadProgressView mProgressView;
    private IndicatorWrapper mIndicatorWrapper;

    public ReadProgressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_read_progress, container, false);
        mProgressView = (ReadProgressView) rootView.findViewById(R.id.progress_view);
        mTvTotalReadingTime = (TextView) rootView.findViewById(R.id.total_reading_time);
        mTvActiveWords = (TextView) rootView.findViewById(R.id.word_active);
        mTvMasterWords = (TextView) rootView.findViewById(R.id.word_master);
        mIndicatorWrapper = (IndicatorWrapper) rootView.findViewById(R.id.indicator_wrapper);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchReadTime();
    }

    private void fetchReadTime() {
        AVQuery<AVReadTime> avQuery = new AVQuery<>("ReadTime");
        Date maxDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date minDate = calendar.getTime();
        avQuery.whereLessThan("read_date", maxDate);
        avQuery.whereGreaterThan("read_date", minDate);
        showIndicator();
        avQuery.findInBackground(new FindCallback<AVReadTime>() {

            @Override
            public void done(List<AVReadTime> list, AVException e) {
                if (e == null) {
                    mProgressView.setReadTime(list);
                }
                fetchTotalReadTime();
            }
        });

    }

    private void fetchTotalReadTime() {
        int userId = UserCache.getUserId(getActivity());
        AVQuery<AVReadTime> query = new AVQuery<>("ReadTime");
        query.whereEqualTo("user_id", userId);
        query.findInBackground(new FindCallback<AVReadTime>() {
            @Override
            public void done(List<AVReadTime> list, AVException e) {
                if (e == null) {
                    long totalTime = 0;
                    for (AVReadTime readTime : list) {
                        totalTime += readTime.getReadTime();
                    }
                    long min = totalTime / 1000 / 60;
                    mTvTotalReadingTime.setText(Long.toString(min));
                }
                fetchTotalActiveWords();
            }
        });
    }

    private void fetchTotalActiveWords() {
        int userId = UserCache.getUserId(getActivity());
        AVQuery<AVWord> query = new AVQuery<>("Word");
        query.whereEqualTo("user_id", userId);
        query.findInBackground(new FindCallback<AVWord>() {
            @Override
            public void done(List<AVWord> list, AVException e) {
                if (e == null) {
                    mTvActiveWords.setText(Integer.toString(list.size()));
                    int masterWords = 0;
                    for (AVWord avWord : list) {
                        if (avWord.isMaster()) {
                            masterWords++;
                        }
                    }
                    mTvMasterWords.setText(Integer.toString(masterWords));
                }
                hideIndicator();
            }
        });
    }

    private void showIndicator() {
        if (mIndicatorWrapper != null) {
            mIndicatorWrapper.showIndicator();
        }
    }

    private void hideIndicator() {
        if (mIndicatorWrapper != null) {
            mIndicatorWrapper.hideIndicator();
        }
    }

}
