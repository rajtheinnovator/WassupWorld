package com.example.android.wassupworld.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.wassupworld.Adapter.NewsAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;


public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, NewsAdapter.AdapterOnClickHandler {

    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mNewsAdapter;
    public ProgressBar progressBar;
    public TextView mEmptyListTextView;
    float offset;
    private final static String FAILED = "failed";
    private final static String ACTION = "com.example.android.wassupworld.Sync.SyncStatus";
    private final static String SYNCING_STATUS = "syncing";
    private final static String RUNNING = "running";
    private final static String STOPPING = "stopping";
    private RecyclerViewUtils.ShowHideToolbarOnScrollingListener showHideToolbarListener;
    private AppBarLayout toolbar;
    public SyncReceiver myReceiver;

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
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mEmptyListTextView= (TextView) rootView.findViewById(R.id.empty_list_text_view);

        mNewsAdapter = new NewsAdapter(getContext(), null, this);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        offset = getResources().getDimensionPixelSize(R.dimen.offset_y);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.news_list_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mNewsAdapter);

        return rootView;
    }
public void setprogressVisible(){
    progressBar.setVisibility(View.VISIBLE);
}
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//if(getLoaderManager().getLoader(0)==null)
        getLoaderManager().initLoader(0, null, this);
//     else
//    getLoaderManager().restartLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
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
            mRecycleView.setVisibility(View.VISIBLE);
            mEmptyListTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

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

        mNewsAdapter.swapCursor(data);

        if (data.getCount() == 0) {
           Log.e("ayat", " cursor data zero");
            mRecycleView.setVisibility(View.GONE);
            mEmptyListTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            Log.e("ayat", " cursor " + data.getCount());

            mRecycleView.setVisibility(View.VISIBLE);
            mEmptyListTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
        Log.e("ayat", " loader restart");

    }

    @Override
    public void onClick(String url,int tag) {
if(tag==0){
        Intent i = new Intent(getContext(), WebViewActivity.class);
        i.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(i);

    }
    else {
    Intent i = new Intent(Intent.ACTION_SEND);
    i.setType("text/plain");
    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
    i.putExtra(Intent.EXTRA_TEXT, url);
    startActivity(Intent.createChooser(i, "Share URL"));
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

    public void restartLoader() {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);

    }
    public class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();


            if (extras != null) {
                Object extrasString=extras.get(SYNCING_STATUS);
                if (extras.get(SYNCING_STATUS).equals(RUNNING)) {
                         progressBar.setVisibility(View.VISIBLE);

                        mEmptyListTextView.setVisibility(View.GONE);

                    Log.e("ayat", "sync running");
                } else if (extras.get(SYNCING_STATUS).equals(STOPPING)){
                    // progressBar.setVisibility(View.GONE);
                    // mEmptyListTextView.setVisibility(View.GONE);
                }
                else if (extras.get(SYNCING_STATUS).equals(FAILED)) {
                        progressBar.setVisibility(View.GONE);
                        mEmptyListTextView.setVisibility(View.VISIBLE);

                }
            }
        }
    }
}
