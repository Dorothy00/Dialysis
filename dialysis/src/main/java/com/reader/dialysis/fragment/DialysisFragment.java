package com.reader.dialysis.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by dorothy on 15/5/9.
 */
public class DialysisFragment extends Fragment {

    public void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
