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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.android.wassupworld.Adapter.NewsAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SearchResultActivity extends AppCompatActivity implements NewsAdapter.AdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SEARCH_KEY = "search";
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mNewsAdapter;
    private ProgressBar progressBar;
    private String mSearch;
    private LinearLayout emptyLayout;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        progressBar = (ProgressBar) findViewById(R.id.result_progress_bar);
        Intent intent = getIntent();
        mSearch = intent.getStringExtra(SEARCH_KEY);
        emptyLayout= (LinearLayout) findViewById(R.id.empty_layout_result);
        mNewsAdapter = new NewsAdapter(this, null, this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecycleView = (RecyclerView) findViewById(R.id.search_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mNewsAdapter);
        getSupportLoaderManager().initLoader(500, null, this);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_result);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Result for " + mSearch);
        mAdView = (AdView) findViewById(R.id.adView_search);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
        String sortOrder = NewsContract.NewsnEntry.COLUMN_DATE + " DESC";
        final String searchResult = "%" + mSearch + "%";
        String selection = "(" + NewsContract.NewsnEntry.COLUMN_TITLE + " LIKE  ? COLLATE NOCASE ) " +
                "OR (" + NewsContract.NewsnEntry.COLUMN_DESCRIPTION + " LIKE  ? COLLATE NOCASE)  ";
        String selectionArgs[] = {searchResult, searchResult};
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
    public void onClick(String url, int tag) {
        if (tag == 0) {
            Intent i = new Intent(SearchResultActivity.this, WebViewActivity.class);
            i.putExtra(Intent.EXTRA_TEXT, url);

            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            i.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(i, "Share URL"));
        }
    }

}