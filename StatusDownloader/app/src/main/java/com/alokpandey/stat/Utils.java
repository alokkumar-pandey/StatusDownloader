package com.alokpandey.stat;

import android.app.Activity;
import android.app.ProgressDialog;

import com.alokpandey.stat.R;

public class Utils {




    public static ProgressDialog SetProgressBar(ProgressDialog mDialog, Activity activity)
    {
        mDialog= ProgressDialog.show(activity, "","");
        mDialog.setContentView(R.layout.progress_bar);
        mDialog.setCancelable(false);
        mDialog.setIndeterminate(false);
        mDialog.show();
        return mDialog;
    }




}
