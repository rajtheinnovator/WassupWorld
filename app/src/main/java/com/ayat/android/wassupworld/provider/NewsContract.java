package com.ayat.android.wassupworld.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dell on 7/1/2017.
 */

public class NewsContract {

    public static final String CONTENT_AUTHORITY = "com.ayat.android.wassupworld.provider.provider";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SOURCES = "sources";

    public static final String PATH_NEWS = "news";
    public static final String PATH__WATCH_LATER_NEWS = "watch_later_news";

    public static final class NewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;

        // Table name
        public static final String TABLE_NAME = "news";


        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_CATEGORY = "category";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_URL = "url";

        public static final String COLUMN_URL_TO_IMAGE = "url_to_image";

        public static final String COLUMN_SOURCE = "source";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_LATER = "later";



    }

    public static final class WatchLaterEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH__WATCH_LATER_NEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH__WATCH_LATER_NEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH__WATCH_LATER_NEWS;

        public static final String TABLE_NAME = "watch_later_news";


        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_CATEGORY = "category";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_URL = "url";

        public static final String COLUMN_URL_TO_IMAGE = "url_to_image";

        public static final String COLUMN_SOURCE = "source";

        public static final String COLUMN_DATE = "date";
    }

    public static final class SourcesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SOURCES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SOURCES;

        public static final String TABLE_NAME = "sources";


        public static final String COLUMN_ID = "id";


        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_URL = "url";

        public static final String COLUMN_URL_TO_IMAGE = "url_to_image";

        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_COUNTRY = "country";

    }
}