package com.example.android.wassupworld.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.wassupworld.provider.NewsContract.NewsnEntry;



public class NewsDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "newsdata.db";


    private NewsDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NEWS_TABLE =

                "CREATE TABLE " + NewsnEntry.TABLE_NAME + " (" +

                        NewsnEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        NewsnEntry.COLUMN_DATE + " INTEGER  , " +
                        NewsnEntry.COLUMN_AUTHOR + " TEXT , " +
                        NewsnEntry.COLUMN_CATEGORY + " TEXT  , " +
                        NewsnEntry.COLUMN_DESCRIPTION + " TEXT , " +
                        NewsnEntry.COLUMN_SOURCE + " TEXT , " +
                        NewsnEntry.COLUMN_TITLE + " TEXT , " +
                        NewsnEntry.COLUMN_URL + " TEXT , " +
                        NewsnEntry.COLUMN_URL_TO_IMAGE + " TEXT , " +
                        NewsnEntry.COLUMN_LATER + " INTEGER DEFAULT 0 , " +

                " UNIQUE (" + NewsnEntry.COLUMN_TITLE + ") ON CONFLICT REPLACE);";


        final String SQL_CREATE_WATCH_LATER_NEWS_TABLE =

                "CREATE TABLE " + NewsContract.WatchLaterEntry.TABLE_NAME + " (" +


                        NewsContract.WatchLaterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        NewsContract.WatchLaterEntry.COLUMN_DATE + " INTEGER  , " +
                        NewsContract.WatchLaterEntry.COLUMN_AUTHOR + " TEXT , " +
                        NewsContract.WatchLaterEntry.COLUMN_CATEGORY + " TEXT  , " +
                        NewsContract.WatchLaterEntry.COLUMN_DESCRIPTION + " TEXT , " +
                        NewsContract.WatchLaterEntry.COLUMN_SOURCE + " TEXT , " +
                        NewsContract.WatchLaterEntry.COLUMN_TITLE + " TEXT , " +
                        NewsContract.WatchLaterEntry.COLUMN_URL + " TEXT , " +
                        NewsContract.WatchLaterEntry.COLUMN_URL_TO_IMAGE + " TEXT , " +

                        " UNIQUE (" + NewsContract.WatchLaterEntry.COLUMN_TITLE + ") ON CONFLICT REPLACE);";


        final String SQL_CREATE_SOURCES_TABLE = "CREATE TABLE " + NewsContract.SourcesEntry.TABLE_NAME + " (" +


                NewsContract.SourcesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                NewsContract.SourcesEntry.COLUMN_CATEGORY + " TEXT  , " +
                NewsContract.SourcesEntry.COLUMN_COUNTRYY + " TEXT , " +
                NewsContract.SourcesEntry.COLUMN_DESCRIPTION + " TEXT , " +
                NewsContract.SourcesEntry.COLUMN_ID + " TEXT , " +
                NewsContract.SourcesEntry.COLUMN_NAME + " TEXT , " +
                NewsContract.SourcesEntry.COLUMN_URL + " TEXT , " +
                NewsContract.SourcesEntry.COLUMN_URL_TO_IMAGE + " TEXT , " +
                " UNIQUE (" + NewsContract.SourcesEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";


        db.execSQL(SQL_CREATE_NEWS_TABLE);
        db.execSQL(SQL_CREATE_WATCH_LATER_NEWS_TABLE);
        db.execSQL(SQL_CREATE_SOURCES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NewsnEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.SourcesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.WatchLaterEntry.TABLE_NAME);
        onCreate(db);

    }
}