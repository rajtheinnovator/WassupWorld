package com.ayat.android.wassupworld.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ayat.android.wassupworld.R;
import com.ayat.android.wassupworld.adapter.SourcesAdapter;
import com.ayat.android.wassupworld.provider.NewsContract;
import com.ayat.android.wassupworld.utils.Icons;


public class SourcesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SourcesAdapter.AdapterOnClickHandlerSources {

    private static final String TYPE_KEY = "type";
    private static final String TYPE_VALUE = "value";
    private static final String SOURCE = "source";
    private final static String SYNCING_STATUS = "syncing";
    private final static String RUNNING = "running";
    private final static String STOPPING = "stopping";
    private static final int SOURCES_FRAGMENT_LOADER = 1;
    private final static String ACTION = "com.ayat.android.wassupworld.Sync.SyncStatus";
    private ProgressBar mProgressBar;
    private TextView mEmptyLayout;
    private RecyclerView mRecycleView;
    private SourcesAdapter mSourcesAdapter;
    private SyncReceiver mReceiver;
    public SourcesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sources, container, false);
        mSourcesAdapter = new SourcesAdapter(getContext(), null, this);
        int mNoOfColumns = Icons.calculateNoOfColumnsSourcesList(getContext());
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), mNoOfColumns);
        mReceiver = new SyncReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        getActivity().registerReceiver(mReceiver, filter);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_sources);
        mEmptyLayout = (TextView) rootView.findViewById(R.id.ll_empty_source);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.sources_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mSourcesAdapter);
        getActivity().getSupportLoaderManager().initLoader(SOURCES_FRAGMENT_LOADER, null, this);
        return rootView;
    }




    @Override
    public void onClick(String name) {
        Intent intent = new Intent(getActivity(), SourceNewsActivity.class);
        intent.putExtra(TYPE_KEY, SOURCE);
        intent.putExtra(TYPE_VALUE, name);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        if (id == SOURCES_FRAGMENT_LOADER) {

            return new CursorLoader(getContext(),
                    NewsContract.SourcesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() == 0) {

            mRecycleView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mSourcesAdapter.swapCursor(data);
            mRecycleView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSourcesAdapter.swapCursor(null);
    }

    public class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();



            if (extras != null) {
                if (extras.get(SYNCING_STATUS).equals(RUNNING)) {
                    if (mSourcesAdapter.getItemCount() == 0) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mEmptyLayout.setVisibility(View.GONE);
                    }

                } else if (extras.get(SYNCING_STATUS).equals(STOPPING)) {

                    mSourcesAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
