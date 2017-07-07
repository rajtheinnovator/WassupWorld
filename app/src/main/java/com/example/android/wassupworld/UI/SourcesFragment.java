package com.example.android.wassupworld.UI;

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

import com.example.android.wassupworld.Adapter.SourcesAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Utils.Icons;
import com.example.android.wassupworld.provider.NewsContract;


public class SourcesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SourcesAdapter.AdapterOnClickHandlerSources {

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
        mLayoutManager = new GridLayoutManager(getContext(),mNoOfColumns);

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
    public void onClick(String url) {

    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        if(id==1) {
            return new CursorLoader(getContext(),
                    NewsContract.SourcesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

        }return  null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mSourcesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSourcesAdapter.swapCursor(null);
    }

}
