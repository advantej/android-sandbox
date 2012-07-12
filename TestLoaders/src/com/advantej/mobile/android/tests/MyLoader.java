package com.advantej.mobile.android.tests;

import android.content.Context;
import android.os.*;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Tejas
 * Date: 7/2/12
 * Time: 1:20 PM
 */
public class MyLoader extends AsyncTaskLoader<Object> {
    private static final String TAG = "MyLoader";

    public MyLoader(Context context) {
        super(context);
    }


    //Runs on a worker thread
    @Override
    public Object loadInBackground() {
        Log.d(TAG, "loadInBackground, myTid : " + android.os.Process.myTid());
        //Do some fancy stuff and return an Object as the result !
        // After return, onLoadFinished() of MyFragment should be called.
        SystemClock.sleep(1000 * 10);
        String abc = "abc " + SystemClock.currentThreadTimeMillis();
        return (Object)abc;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading, myTid : " + android.os.Process.myTid());
        /* Follow this pattern
        if(dataIsReady) {
            deliverResult(data);
          } else {
            forceLoad();
        }
        */
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        Log.d(TAG, "onStopLoading, myTid : " + android.os.Process.myTid());
        super.onStopLoading();
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset, myTid : " + android.os.Process.myTid());
        super.onReset();
    }

    @Override
    protected void onForceLoad() {
        Log.d(TAG, "onForceLoad, myTid : " + android.os.Process.myTid());
        super.onForceLoad();
    }
}

