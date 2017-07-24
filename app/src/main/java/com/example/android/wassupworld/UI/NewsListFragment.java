package com.example.android.wassupworld.UI;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.android.wassupworld.Adapter.NewsAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;


public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, NewsAdapter.AdapterOnClickHandler {

    public static final String TYPE_KEY = "type";
    public static final String TYPE_VALUE = "value";
    public static final String SOURCE = "source";
    private final static String ACTION = "com.example.android.wassupworld.Sync.SyncStatus";
    private final static String SYNCING_STATUS = "syncing";
    private final static String RUNNING = "running";
    private final static String STOPPING = "stopping";
    public ProgressBar progressBar;

    public SyncReceiver myReceiver;
    float offset;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mNewsAdapter;
    private LinearLayout mEmptyLayout;

    public NewsListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        myReceiver = new SyncReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        getActivity().registerReceiver(myReceiver, filter);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_news);
        mEmptyLayout = (LinearLayout) rootView.findViewById(R.id.ll_empty_latest);

        mNewsAdapter = new NewsAdapter(getContext(), null, this);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        offset = getResources().getDimensionPixelSize(R.dimen.offset_y);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.news_list_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mNewsAdapter);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    public void setprogressVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = NewsContract.NewsnEntry.COLUMN_DATE + " DESC";

        if (id == 0) {
            Log.e("ayat", "loader start");


            return new CursorLoader(getContext(),
                    NewsContract.NewsnEntry.CONTENT_URI,
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
            Log.e("ayat", " cursor data zero");
            mRecycleView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            Log.e("ayat", " cursor " + data.getCount());
            mNewsAdapter.swapCursor(data);
            mRecycleView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
        Log.e("ayat", " loader restart");

    }

    @Override
    public void onClick(String data, int tag) {
        if (tag == 0) {
            Intent i = new Intent(getContext(), WebViewActivity.class);
            i.putExtra(Intent.EXTRA_TEXT, data);
            startActivity(i);

        } else if (tag == 1) {
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
        if (myReceiver != null) {
            getActivity().unregisterReceiver(myReceiver);
            myReceiver = null;
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
                        progressBar.setVisibility(View.VISIBLE);
                        mEmptyLayout.setVisibility(View.GONE);
                    }
                    Log.e("ayat", "sync running");
                } else if (extras.get(SYNCING_STATUS).equals(STOPPING)) {
                    Log.e("ayat", "sync stopped fragment");
                    mNewsAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}

