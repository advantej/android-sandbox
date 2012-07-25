package com.advantej.mobile.android.tests.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.advantej.mobile.android.R;

public class MainActivity extends FragmentActivity
{
    private static final String TAG = "MainActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate, myTid : " + android.os.Process.myTid());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
