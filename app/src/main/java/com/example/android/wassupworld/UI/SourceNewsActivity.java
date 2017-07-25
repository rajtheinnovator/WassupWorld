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

import com.example.android.wassupworld.Adapter.SourceListAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SourceNewsActivity extends AppCompatActivity implements SourceListAdapter.SourceListAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {


    public final static String SOURCE = "source";
    public final static String CATEGORY = "category";
    public static final String TYPE_KEY = "type";
    public static final String TYPE_VALUE = "value";
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SourceListAdapter mSourceListAdapter;
    private String mTypeName;
    private String mType;
    private ProgressBar progressBar;
    private AdView mAdView;
    private LinearLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_news);
        Intent intent = getIntent();
        mTypeName = intent.getStringExtra(TYPE_VALUE);
        mType = intent.getStringExtra(TYPE_KEY);
        mSourceListAdapter = new SourceListAdapter(this, null, this, mType);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycleView = (RecyclerView) findViewById(R.id.click_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mSourceListAdapter);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_source_news);
        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout_source_news);
        mAdView = (AdView) findViewById(R.id.adView_source);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_source_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mTypeName);
        getSupportLoaderManager().initLoader(205, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);

        String sortOrder = NewsContract.NewsnEntry.COLUMN_DATE + " DESC";
        String selection = "";
        if (mType.equals(CATEGORY)) {
            selection = NewsContract.NewsnEntry.COLUMN_CATEGORY + "= ?";
        } else {
            selection = NewsContract.NewsnEntry.COLUMN_SOURCE + "= ?";

        }

        String selectionArgs[] = {mTypeName.toLowerCase()};


        if (id == 205) {
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
            mSourceListAdapter.swapCursor(data);
            mRecycleView.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            mRecycleView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSourceListAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String url, int tag) {
        if (tag == 0) {
            Intent i = new Intent(SourceNewsActivity.this, WebViewActivity.class);
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
