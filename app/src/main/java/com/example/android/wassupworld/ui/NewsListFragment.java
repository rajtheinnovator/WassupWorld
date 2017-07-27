package com.example.android.wassupworld.ui;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.adapter.NewsAdapter;
import com.example.android.wassupworld.provider.NewsContract;


public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, NewsAdapter.AdapterOnClickHandler {

    private static final String TYPE_KEY = "type";
    private static final String TYPE_VALUE = "value";
    private static final String SOURCE = "source";
    private final static String ACTION = "com.example.android.wassupworld.Sync.SyncStatus";
    private final static String SYNCING_STATUS = "syncing";
    private final static String RUNNING = "running";
    private final static String STOPPING = "stopping";
    private final static String FAILED = "failed";
    private static final int NEWS_FRAGMENT_LOADER = 0;
    private static final int WEB_VIEW_ACTIVITY_KEY = 0;
    private static final int SHARE_KEY = 1;
    private ProgressBar mProgressBar;
    private SyncReceiver mReceiver;
    private RecyclerView mRecycleView;
    private NewsAdapter mNewsAdapter;
    private TextView mEmptyLayout;
    public NewsListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        mReceiver = new SyncReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        getActivity().registerReceiver(mReceiver, filter);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_news);
        mEmptyLayout = (TextView) rootView.findViewById(R.id.ll_empty_latest);
        mNewsAdapter = new NewsAdapter(getContext(), null, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.news_list_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mNewsAdapter);
        getActivity().getSupportLoaderManager().initLoader(NEWS_FRAGMENT_LOADER, null, this);
        return rootView;
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = NewsContract.NewsEntry.COLUMN_DATE + " DESC";

        if (id == NEWS_FRAGMENT_LOADER) {
            return new CursorLoader(getContext(),
                    NewsContract.NewsEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    sortOrder);

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
            mNewsAdapter.swapCursor(data);
            mRecycleView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);

    }

    @Override
    public void onClick(String data, int key) {
        if (key == WEB_VIEW_ACTIVITY_KEY) {
            Intent i = new Intent(getContext(), WebViewActivity.class);
            i.putExtra(Intent.EXTRA_TEXT, data);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (key == SHARE_KEY) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            i.putExtra(Intent.EXTRA_TEXT, data);
            startActivity(Intent.createChooser(i, "Share URL"));
        } else {
            Intent intent = new Intent(getActivity(), SourceNewsActivity.class);
            intent.putExtra(TYPE_KEY, SOURCE);
            intent.putExtra(TYPE_VALUE, data);
            startActivity(intent);
        }
    }


    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }


    public class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();


            if (extras != null) {
                if (extras.get(SYNCING_STATUS).equals(RUNNING)) {
                    if (mNewsAdapter.getItemCount() == 0) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mEmptyLayout.setVisibility(View.GONE);
                    }

                } else if (extras.get(SYNCING_STATUS).equals(STOPPING)) {

                    mNewsAdapter.notifyDataSetChanged();
                }

            }
        }
    }
}

