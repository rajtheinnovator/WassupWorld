package com.example.android.wassupworld;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.android.wassupworld.Adapter.NewsAdapter;
import com.example.android.wassupworld.Sync.SyncAdapter;
import com.example.android.wassupworld.provider.NewsContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NewsAdapter.AdapterOnClickHandler {
    private boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private NewsAdapter mNewsAdapter;

    private RecyclerView mRecycleView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SyncAdapter.initializeSyncAdapter(this);

        mNewsAdapter = new NewsAdapter(this, null, this);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycleView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mNewsAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshContent();
                           }
                }
        );

       BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites:

                            case R.id.action_schedules:

                            case R.id.action_music:

                        }
                        return true;
                    }
                });

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = NewsContract.NewsnEntry.COLUMN_DATE + " DESC";


        return new CursorLoader(this,
                NewsContract.NewsnEntry.CONTENT_URI,
                null,
                null,
                null,
                sortOrder);


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
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(i);

    }

    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SyncAdapter.syncImmediately(MainActivity.this);

                mSwipeRefreshLayout.setRefreshing(false);
          }
        }, 5000);
        getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
    }


}
