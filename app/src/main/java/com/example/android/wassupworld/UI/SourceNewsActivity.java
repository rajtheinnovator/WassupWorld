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

import com.example.android.wassupworld.Adapter.SourceListAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;

public class SourceNewsActivity extends AppCompatActivity implements SourceListAdapter.SourceListAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {


    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SourceListAdapter mSourceListAdapter;
    private String mTypeName;
    private String mType;

    public static final String TYPE_KEY="type";
    public static final String TYPE_VALUE="value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);
        Intent intent = getIntent();
        mTypeName = intent.getStringExtra(TYPE_VALUE);
        mType = intent.getStringExtra(TYPE_KEY);
        mSourceListAdapter = new SourceListAdapter(this, null, this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecycleView = (RecyclerView) findViewById(R.id.click_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mSourceListAdapter);
        getSupportLoaderManager().initLoader(205, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = NewsContract.NewsnEntry.COLUMN_DATE + " DESC";
        String selection="";
        if (mType.equals("category")) {
             selection = NewsContract.NewsnEntry.COLUMN_CATEGORY + "= ?";
        }
        else {
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

        mSourceListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSourceListAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String url) {
        Intent i = new Intent(SourceNewsActivity.this, WebViewActivity.class);
        i.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(i);

    }


}
