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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.android.wassupworld.Adapter.LaterAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;

import static android.R.attr.data;

public class WatchLaterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, LaterAdapter.LaterAdapterOnClickHandler {

    public static final String TYPE_KEY = "type";
    public static final String TYPE_VALUE = "value";
    public static final String SOURCE = "source";
    private ProgressBar progressBar;
    private LinearLayout emptyLayout;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LaterAdapter
            mLaterAdapter;

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
        mLaterAdapter = new LaterAdapter(getContext(), null, this);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_later);
        emptyLayout = (LinearLayout) rootView.findViewById(R.id.empty_layout_later);

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

        progressBar.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);

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
    public void onClick(String url,int tag) {
        if(tag==0){
            Intent i = new Intent(getContext(), WebViewActivity.class);
            i.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (tag == 1) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            i.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(i, "Share URL"));
        } else {
            Intent intent = new Intent(getActivity(), SourceNewsActivity.class);
            intent.putExtra(TYPE_KEY, SOURCE);
            intent.putExtra(TYPE_VALUE, data);
            startActivity(intent);
        }
    }




}
