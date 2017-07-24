package com.example.android.wassupworld.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.wassupworld.Adapter.SourcesAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Utils.Icons;
import com.example.android.wassupworld.provider.NewsContract;


public class SourcesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SourcesAdapter.AdapterOnClickHandlerSources {

    public static final String TYPE_KEY = "type";
    public static final String TYPE_VALUE = "value";
    public static final String SOURCE = "source";
    private final static String FAILED = "failed";
    private final static String ACTION = "com.example.android.wassupworld.Sync.SyncStatus";
    private final static String SYNCING_STATUS = "syncing";
    private final static String RUNNING = "running";
    private final static String STOPPING = "stopping";
    public ProgressBar progressBar;
    public TextView mEmptyListTextView;
    private RecyclerView mRecycleView;
    private GridLayoutManager mLayoutManager;
    private SourcesAdapter mSourcesAdapter;
    public SourcesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sources, container, false);
        mSourcesAdapter = new SourcesAdapter(getContext(), null, this);
        int mNoOfColumns = Icons.calculateNoOfColumnsSourcesList(getContext());
        mLayoutManager = new GridLayoutManager(getContext(), mNoOfColumns);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_sources);
        mEmptyListTextView = (TextView) rootView.findViewById(R.id.empty_list_text_view_sources);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.sources_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mSourcesAdapter);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);
        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(String name) {
        Intent intent = new Intent(getActivity(), SourceNewsActivity.class);
        intent.putExtra(TYPE_KEY, SOURCE);
        intent.putExtra(TYPE_VALUE, name);
        startActivity(intent);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        if (id == 1) {
            Log.e("ayat", "loader start source");
            mRecycleView.setVisibility(View.VISIBLE);
            mEmptyListTextView.setVisibility(View.GONE);
            if (mSourcesAdapter.getItemCount() == 0)
                progressBar.setVisibility(View.VISIBLE);

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
            Log.e("ayat", " cursor data zero");
            mRecycleView.setVisibility(View.GONE);
            mEmptyListTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            Log.e("ayat", "cursor source" + data.getCount());
            mSourcesAdapter.swapCursor(data);
            mRecycleView.setVisibility(View.VISIBLE);
            mEmptyListTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
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
                Object extrasString = extras.get(SYNCING_STATUS);
                if (extras.get(SYNCING_STATUS).equals(RUNNING)) {
                    progressBar.setVisibility(View.VISIBLE);

                    mEmptyListTextView.setVisibility(View.GONE);

                    Log.e("ayat", "sync running");
                } else if (extras.get(SYNCING_STATUS).equals(FAILED)) {
                    progressBar.setVisibility(View.GONE);
                    mEmptyListTextView.setVisibility(View.VISIBLE);

                }
            }
        }
    }
}
