package com.example.android.wassupworld;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.android.wassupworld.provider.NewsContract;

/**
 * Created by dell on 7/2/2017.
 */

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, NewsAdapter.AdapterOnClickHandler {

    private NewsAdapter mForecastAdapter;

    private RecyclerView mRecycleView;

    public ListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mForecastAdapter = new NewsAdapter(getContext(), null, this);

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recycle_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        ;
        // mRecycleView.setEmptyView(emptyList);
        //.RecycleListView
        mRecycleView.setAdapter(mForecastAdapter);

        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = NewsContract.NewsnEntry.COLUMN_DATE + " DESC";


        return new CursorLoader(getActivity(),
                NewsContract.NewsnEntry.CONTENT_URI,
                null,
                null,
                null,
                sortOrder);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mForecastAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String url) {
        Intent i=new Intent(getActivity(),WebViewActivity.class);
        i.putExtra(Intent.EXTRA_TEXT,url);
        startActivity(i);

    }
}
