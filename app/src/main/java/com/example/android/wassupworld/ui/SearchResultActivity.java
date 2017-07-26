package com.example.android.wassupworld.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.adapter.NewsAdapter;
import com.example.android.wassupworld.provider.NewsContract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class SearchResultActivity extends AppCompatActivity implements NewsAdapter.AdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {
    private final static String SOURCE = "source";
    private final static String CATEGORY = "category";
    private static final String SEARCH_KEY = "search";
    private static final int SEARCH_RESULT_ACTIVITY_LOADER = 5;
    private static final int WEB_VIEW_ACTIVITY_KEY = 0;
    private static final int SHARE_KEY = 1;
    private static final String TYPE_KEY = "type";
    private static final String TYPE_VALUE = "value";
    private RecyclerView mRecycleView;
    private NewsAdapter mNewsAdapter;
    private ProgressBar progressBar;
    private String mSearch;
    private TextView emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        progressBar = (ProgressBar) findViewById(R.id.result_progress_bar);
        Intent intent = getIntent();
        mSearch = intent.getStringExtra(SEARCH_KEY);
        emptyLayout = (TextView) findViewById(R.id.empty_view_result_recycle);
        mNewsAdapter = new NewsAdapter(this, null, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecycleView = (RecyclerView) findViewById(R.id.search_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mNewsAdapter);
        getSupportLoaderManager().initLoader(SEARCH_RESULT_ACTIVITY_LOADER, null, this);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_result);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Result for " + mSearch);
        AdView mAdView = (AdView) findViewById(R.id.adView_search);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
        String sortOrder = NewsContract.NewsEntry.COLUMN_DATE + " DESC";
        final String searchResult = "%" + mSearch + "%";
        String selection = "(" + NewsContract.NewsEntry.COLUMN_TITLE + " LIKE  ? COLLATE NOCASE ) " +
                "OR (" + NewsContract.NewsEntry.COLUMN_DESCRIPTION + " LIKE  ? COLLATE NOCASE)  ";
        String selectionArgs[] = {searchResult, searchResult};
        if (id == SEARCH_RESULT_ACTIVITY_LOADER) {
            return new CursorLoader(this,
                    NewsContract.NewsEntry.CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    sortOrder);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressBar.setVisibility(View.GONE);

        if (data.getCount() > 0) {
            mNewsAdapter.swapCursor(data);
            mRecycleView.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            mRecycleView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String url, int key) {
        if (key == WEB_VIEW_ACTIVITY_KEY) {
            Intent i = new Intent(SearchResultActivity.this, WebViewActivity.class);
            i.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (key == SHARE_KEY) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            i.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(i, "Share URL"));
        } else {
            Intent intent = new Intent(SearchResultActivity.this, SourceNewsActivity.class);
            intent.putExtra(TYPE_KEY, SOURCE);
            intent.putExtra(TYPE_VALUE, url);
            startActivity(intent);
        }
    }


}