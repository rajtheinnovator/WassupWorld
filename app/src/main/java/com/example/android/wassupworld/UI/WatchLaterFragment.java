package com.example.android.wassupworld.UI;

import android.content.Context;
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

import com.example.android.wassupworld.Adapter.NewsAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;

public class WatchLaterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, NewsAdapter.AdapterOnClickHandler {

    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mLaterAdapter;

    public WatchLaterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_watch_later, container, false);
        mLaterAdapter = new NewsAdapter(getContext(), null, this);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecycleView = (RecyclerView) rootView.findViewById(R.id.later_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mLaterAdapter);
        getActivity().getSupportLoaderManager().initLoader(100, null, this);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = NewsContract.WatchLaterEntry._ID + " DESC";

        if(id==100) {
            return new CursorLoader(getContext(),
                    NewsContract.WatchLaterEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    sortOrder);

        }
        return  null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mLaterAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLaterAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String url) {
        Intent i = new Intent(getContext(), WebViewActivity.class);
        i.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(i);

    }



}
