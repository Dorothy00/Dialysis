package com.reader.dialysis.activity;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by dorothy on 15/5/6.
 */
public class DialysisActivity extends Activity {

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
