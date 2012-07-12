package com.advantej.mobile.android.tests;

import android.os.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.R;

/**
 * Created by IntelliJ IDEA.
 * User: Tejas
 * Date: 7/2/12
 * Time: 1:12 PM
 */
public class MyFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>{

    private static final String TAG = "MyFragment";
    private LoaderManager mLoaderManager;
    private static final int MY_LOADER = 1;

    private TextView mTxtSomeTxt;
    private Button mRefresh;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated, myTid : " + android.os.Process.myTid());

        mLoaderManager = getLoaderManager();
        mLoaderManager.initLoader(MY_LOADER, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_myfrag, container);
        mTxtSomeTxt = (TextView) fragmentView.findViewById(R.id.txt_someTxt);
        mRefresh = (Button) fragmentView.findViewById(R.id.btn_refresh);
        mRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mLoaderManager.restartLoader(MY_LOADER, null, MyFragment.this);
            }
        });
        return fragmentView;
    }

    @Override
    public Loader<Object> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, "onCreateLoader, myTid : " + android.os.Process.myTid());
        return new MyLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        Log.d(TAG, "onLoadFinished, myTid : " + android.os.Process.myTid());
        if(mTxtSomeTxt != null){
            mTxtSomeTxt.setText((String)data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> objectLoader) {
        Log.d(TAG, "onLoaderReset, myTid : " + android.os.Process.myTid());
        if(mTxtSomeTxt != null){
            mTxtSomeTxt.setText("");
        }
    }
}
