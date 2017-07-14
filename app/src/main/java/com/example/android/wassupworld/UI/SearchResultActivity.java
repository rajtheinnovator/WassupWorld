package com.example.android.wassupworld.UI;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.wassupworld.Adapter.NewsAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;

public class SearchResultActivity extends AppCompatActivity  implements NewsAdapter.AdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mNewsAdapter;

    private String mSearch;

    public static final String SEARCH_KEY = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch_result);
        Intent intent = getIntent();
        mSearch = intent.getStringExtra(SEARCH_KEY);

        mNewsAdapter = new NewsAdapter(this, null, this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecycleView = (RecyclerView) findViewById(R.id.search_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mNewsAdapter);
        getSupportLoaderManager().initLoader(500, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = NewsContract.NewsnEntry.COLUMN_DATE + " DESC";
        final String sa1 = "%"+mSearch+"%";
        String  selection =NewsContract.NewsnEntry.COLUMN_DESCRIPTION + " LIKE  ? COLLATE NOCASE  ";
      String selectionArgs[]={sa1};
        if (id == 500) {
            return new CursorLoader(this,
                    NewsContract.NewsnEntry.CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    sortOrder);

        }
        return null;
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
        Intent i = new Intent(SearchResultActivity.this, WebViewActivity.class);
        i.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(i);

    }
}