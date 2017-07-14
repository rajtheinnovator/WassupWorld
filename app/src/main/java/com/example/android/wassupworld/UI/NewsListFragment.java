package com.example.android.wassupworld.UI;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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


public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, NewsAdapter.AdapterOnClickHandler {

    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mNewsAdapter;
 private RecyclerViewUtils.ShowHideToolbarOnScrollingListener showHideToolbarListener;
private AppBarLayout toolbar;
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


        mNewsAdapter = new NewsAdapter(getContext(), null, this);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecycleView = (RecyclerView) rootView.findViewById(R.id.news_list_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mNewsAdapter);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        return rootView; }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = NewsContract.NewsnEntry.COLUMN_DATE + " DESC";

if(id==0) {
    return new CursorLoader(getContext(),
            NewsContract.NewsnEntry.CONTENT_URI,
            null,
            null,
            null,
            sortOrder);

}
return  null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mNewsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String url) {
        Intent i = new Intent(getContext(), WebViewActivity.class);
        i.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(i);

    }


    public void restartLoader() {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);

    }
}
