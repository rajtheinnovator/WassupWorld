package com.ayat.android.wassupworld.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Created by dell on 7/1/2017.
 */

public final class NewsProvider extends android.content.ContentProvider {

    private static final int CODE_NEWS = 100;
    private static final int CODE_WATCH_LATER_NEWS = 105;
    private static final int CODE_SOURCES = 110;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NewsDatabase mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, NewsContract.PATH_NEWS, CODE_NEWS);
        matcher.addURI(authority, NewsContract.PATH__WATCH_LATER_NEWS, CODE_WATCH_LATER_NEWS);
        matcher.addURI(authority, NewsContract.PATH_SOURCES, CODE_SOURCES);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new NewsDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;
    switch (sUriMatcher.match(uri)) {

            case CODE_NEWS: {

                cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        NewsContract.NewsEntry.TABLE_NAME,
                         projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

        case CODE_WATCH_LATER_NEWS: {


            cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                    NewsContract.WatchLaterEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);

            break;
        }
        case CODE_SOURCES: {


            cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                    NewsContract.SourcesEntry.TABLE_NAME,
                    projection,
                   selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);

            break;
        }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case CODE_NEWS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
              break;
            case CODE_SOURCES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(NewsContract.SourcesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;

            default:
                return super.bulkInsert(uri, values);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        long id;
        Uri returnUri;

        switch (match) {
            case CODE_WATCH_LATER_NEWS :{

                 id = db.insert(NewsContract.WatchLaterEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(NewsContract.WatchLaterEntry.CONTENT_URI, id);
                } else {throw new android.database.SQLException("Unknown uri: " + uri);
                }
                break;
            }
            case CODE_NEWS :{

                id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(NewsContract.NewsEntry.CONTENT_URI, id);
                } else {throw new android.database.SQLException("Unknown uri: " + uri);
                }
                break;
            }
            case CODE_SOURCES:{

             id = db.insert(NewsContract.SourcesEntry.TABLE_NAME,null,values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(NewsContract.SourcesEntry.CONTENT_URI, id);
                } else {throw new android.database.SQLException("Unknown uri: " + uri);
                }
                break;}
            default:
                throw new UnsupportedOperationException("insert");}


        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
      int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_NEWS:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        NewsContract.NewsEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case CODE_SOURCES:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        NewsContract.SourcesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            case CODE_WATCH_LATER_NEWS:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        NewsContract.WatchLaterEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int mRowsUpdated;
   switch (sUriMatcher.match(uri)) {

       case CODE_NEWS:
           mRowsUpdated = mOpenHelper.getWritableDatabase().update(
                   NewsContract.NewsEntry.TABLE_NAME,
                   values,
                   selection,
                   selectionArgs);

           break;
       default:
           throw new UnsupportedOperationException("Unknown uri: " + uri);

   }
        if (mRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
   return mRowsUpdated;

   }}
