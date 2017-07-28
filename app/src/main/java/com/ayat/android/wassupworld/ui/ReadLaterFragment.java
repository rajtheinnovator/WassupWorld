package com.ayat.android.wassupworld.ui;

import android.content.Intent;
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

import com.ayat.android.wassupworld.R;
import com.ayat.android.wassupworld.adapter.LaterAdapter;
import com.ayat.android.wassupworld.provider.NewsContract;

public class ReadLaterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, LaterAdapter.LaterAdapterOnClickHandler {

    private static final String TYPE_KEY = "type";
    private static final String TYPE_VALUE = "value";
    private static final String SOURCE = "source";
    private static final int READ_LATER_FRAGMENT_LOADER = 10;
    private static final int WEB_VIEW_ACTIVITY_KEY = 0;
    private static final int SHARE_KEY = 1;
    private ProgressBar progressBar;
    private TextView emptyLayout;
    private RecyclerView mRecycleView;
    private LaterAdapter mLaterAdapter;

    public ReadLaterFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_watch_later, container, false);
        mLaterAdapter = new LaterAdapter(getContext(), null, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_later);
        emptyLayout = (TextView) rootView.findViewById(R.id.empty_layout_later);

        mRecycleView = (RecyclerView) rootView.findViewById(R.id.later_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mLaterAdapter);
        getActivity().getSupportLoaderManager().initLoader(READ_LATER_FRAGMENT_LOADER, null, this);
        return rootView;
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        progressBar.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);

        String sortOrder = NewsContract.WatchLaterEntry._ID + " DESC";

        if (id == READ_LATER_FRAGMENT_LOADER) {
            return new CursorLoader(getContext(),
                    NewsContract.WatchLaterEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    sortOrder);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressBar.setVisibility(View.GONE);
        if (data.getCount() > 0) {
            mLaterAdapter.swapCursor(data);
            mRecycleView.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            mRecycleView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLaterAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String url, int key) {
        if (key == WEB_VIEW_ACTIVITY_KEY) {
            Intent i = new Intent(getContext(), WebViewActivity.class);
            i.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (key == SHARE_KEY) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            i.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(i, "Share URL"));
        } else {
            Intent intent = new Intent(getActivity(), SourceNewsActivity.class);
            intent.putExtra(TYPE_KEY, SOURCE);
            intent.putExtra(TYPE_VALUE, url);
            startActivity(intent);
        }
    }


}
