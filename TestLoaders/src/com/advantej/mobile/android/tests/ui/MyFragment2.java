package com.advantej.mobile.android.tests.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.advantej.mobile.android.R;
import com.advantej.mobile.android.tests.content.MyLoader;
import com.advantej.mobile.android.tests.content.DBContract;
import com.advantej.mobile.android.tests.content.MyDB;

/**
 * Created by IntelliJ IDEA.
 * User: Tejas
 * Date: 7/25/12
 * Time: 12:10 PM
 */
public class MyFragment2 extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MyFragment2";
    private LoaderManager mLoaderManager;
    private static final int MY_LOADER = 1;

    // Handler to sync onLoadFinished/onLoaderReset with the UI thread
    private Handler mHandler = new Handler();

    private TextView mTxtSomeTxt;
    private Button mRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View fragmentView = inflater.inflate(R.layout.fragment_myfrag2, container);
        mTxtSomeTxt = (TextView) fragmentView.findViewById(R.id.txt_someTxt2);
        mRefresh = (Button) fragmentView.findViewById(R.id.btn_refresh_table2);
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Put test data
                MyDB.getDataBaseManager(getActivity()).delete(DBContract.TABLE2, null, null);
                ContentValues cv = new ContentValues();
                cv.put(DBContract.Table2Columns.TABLE2COL1, "Frag2 " + SystemClock.currentThreadTimeMillis());
                MyDB.getDataBaseManager(getActivity()).insert(DBContract.TABLE2, null, cv);
            }
        });
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated, myTid : " + android.os.Process.myTid());

        mLoaderManager = getLoaderManager();
        Loader loader = mLoaderManager.getLoader(MY_LOADER);
        if (loader != null) {
            if(!loader.isStarted()){
                Log.d(TAG, "Loader is not null and not started");
                mLoaderManager.initLoader(MY_LOADER, null, this);
            } else {
                Log.d(TAG, "Loader is not null and is started");
            }
        } else {
            Log.d(TAG, "Loader is null");
            mLoaderManager.initLoader(MY_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        return new MyLoader(getActivity(), DBContract.TABLE2, null, null, null, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished, myTid : " + android.os.Process.myTid());

        if(cursor.moveToFirst()){
            //TODO : Run over the cursor to do something with the whole data (later ad a list + adapter)
            final String val =  cursor.getString(cursor.getColumnIndex(DBContract.Table2Columns.TABLE2COL1));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mTxtSomeTxt != null) {
                        mTxtSomeTxt.setText(val);
                    }
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.d(TAG, "onLoaderReset, myTid : " + android.os.Process.myTid());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mTxtSomeTxt != null) {
                    mTxtSomeTxt.setText("");
                }
            }
        });
    }
}
