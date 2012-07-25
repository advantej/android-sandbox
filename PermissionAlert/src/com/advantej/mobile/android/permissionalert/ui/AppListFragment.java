package com.advantej.mobile.android.permissionalert.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.advantej.mobile.android.R;
import com.advantej.mobile.android.permissionalert.content.AppListLoader;

/**
 * Created by IntelliJ IDEA.
 * User: Tejas
 * Date: 7/20/12
 * Time: 11:57 AM
 */
public class AppListFragment extends Fragment implements LoaderManager.LoaderCallbacks {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.id.fragment_app_list, container, false);

        ListView appList = (ListView) root.findViewById(R.id.lst_app_list);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new AppListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onLoaderReset(Loader loader) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
