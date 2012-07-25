package com.advantej.mobile.android.tests.content;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Tejas
 * Date: 7/2/12
 * Time: 1:20 PM
 */
public class MyLoader extends AsyncTaskLoader<Cursor> {
    private static final String TAG = "MyLoader";

    private Cursor mCursor;
    private final ForceLoadContentObserver mObserver;
    private MyDB myDB;

    private String mTableName;
    private String[] mTableColumns;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mGroupBy;
    private String mHaving;
    private String mOrderBy;
    private String mLimit;

    public MyLoader(Context context, String table, String[] columns,
                    String selection, String[] selectionArgs, String groupBy, String having,
                    String orderBy, String limit){
        super(context);
        mObserver = new ForceLoadContentObserver();
        myDB = MyDB.getDataBaseManager(getContext()); // Is this safe ? or getApplicationContext is needed ?

        mTableName = table;
        mTableColumns = columns;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mGroupBy = groupBy;
        mHaving = having;
        mOrderBy = orderBy;
        mLimit = limit;
    }


    //Runs on a worker thread
    @Override
    public Cursor loadInBackground() {
        Log.d(TAG, "loadInBackground, myTid : " + android.os.Process.myTid());
        Log.d(TAG, "loadInBackground, ObjectId : " + System.identityHashCode(MyLoader.this));

        // After return, onLoadFinished() of MyFragment should be called.
        Cursor cursor = myDB.query(mTableName, mTableColumns, mSelection, mSelectionArgs, mGroupBy,
                                   mHaving, mOrderBy, mLimit);
        if(cursor != null){
            cursor.getCount();
            cursor.registerContentObserver(mObserver);
        }

        return cursor;
    }

    @Override
    public void deliverResult(Cursor cursorToDeliver) {
        if(isReset()){
           if(cursorToDeliver != null) {
              cursorToDeliver.close();
           }
           return;
        }

        Cursor oldCursor = mCursor;
        mCursor = cursorToDeliver;

        if(isStarted()){
            super.deliverResult(cursorToDeliver);
        }

        if (oldCursor != null && oldCursor != cursorToDeliver && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading, myTid : " + android.os.Process.myTid());
        if(mCursor != null){
           deliverResult(mCursor);
        }

        if(mCursor == null || takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        Log.d(TAG, "onStopLoading, myTid : " + android.os.Process.myTid());
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset, myTid : " + android.os.Process.myTid());
        super.onReset();

        stopLoading();

        if(mCursor!= null && !mCursor.isClosed()){
            mCursor.close();
        }

        mCursor = null;

    }

    @Override
    protected void onForceLoad() {
        Log.d(TAG, "onForceLoad, myTid : " + android.os.Process.myTid());
        super.onForceLoad();
    }

    @Override
    public void onContentChanged() {
        Log.d(TAG, "onContentChanged, myTid : " + android.os.Process.myTid());
        super.onContentChanged();
    }

}

