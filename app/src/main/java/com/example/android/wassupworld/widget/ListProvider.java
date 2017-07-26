package com.example.android.wassupworld.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    // these indices must match the projection
    private static final int INDEX_ID = 0;
    private static final int INDEX_DATE = 1;
    private static final int INDEX_SOURCE = 2;
    private static final int INDEX_TITLE = 3;
    private static final int INDEX_IMAGE_URL = 4;
    private static final int INDEX_URL = 5;
    private final String[] FORECAST_COLUMNS = {
            NewsContract.NewsEntry.TABLE_NAME + "." + NewsContract.NewsEntry._ID,
            NewsContract.NewsEntry.COLUMN_DATE,
            NewsContract.NewsEntry.COLUMN_SOURCE,
            NewsContract.NewsEntry.COLUMN_TITLE,
            NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE,
            NewsContract.NewsEntry.COLUMN_URL
    };
    private Context context = null;
    private Cursor data = null;

    public ListProvider(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (data != null) {
            data.close();
        }
        // This method is called by the app hosting the widget (e.g., the launcher)
        // However, our ContentProvider is not exported so it doesn't have access to the
        // data. Therefore we need to clear (and finally restore) the calling identity so
        // that calls use our process and permission
        final long identityToken = Binder.clearCallingIdentity();


        data = context.getContentResolver().query(NewsContract.NewsEntry.CONTENT_URI,
                FORECAST_COLUMNS,
                null,
                null,
                NewsContract.NewsEntry.COLUMN_DATE + " DESC LIMIT 10");


        Binder.restoreCallingIdentity(identityToken);
    }


    @Override
    public void onDestroy() {
        if (data != null) {
            data.close();
            data = null;
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                data == null || !data.moveToPosition(position)) {
            return null;
        }
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_list_item);

        String urlImage = data.getString(INDEX_IMAGE_URL);
        String titie = data.getString(INDEX_TITLE);
        String url = data.getString(INDEX_URL);
        Bitmap image = null;
        if (!TextUtils.isEmpty(urlImage))
            try {
                image = Picasso.with(context).load(urlImage).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

        if (image != null) {
            views.setImageViewBitmap(R.id.iv_widget_news, image);
        }

        setRemoteContentDescription(views, titie);

        if (!TextUtils.isEmpty(titie))
        views.setTextViewText(R.id.tv_news_title, titie);

        Intent i = new Intent();
        i.putExtra(Intent.EXTRA_TEXT, url);
        views.setOnClickFillInIntent(R.id.widget_list_item, i);
        return views;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.iv_widget_news, description);
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (data.moveToPosition(position))
            return data.getLong(INDEX_ID);
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}


