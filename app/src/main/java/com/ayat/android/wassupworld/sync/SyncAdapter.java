package com.ayat.android.wassupworld.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import com.ayat.android.wassupworld.BuildConfig;
import com.ayat.android.wassupworld.R;
import com.ayat.android.wassupworld.model.News;
import com.ayat.android.wassupworld.model.ResultNews;
import com.ayat.android.wassupworld.model.ResultSources;
import com.ayat.android.wassupworld.model.Sources;
import com.ayat.android.wassupworld.provider.NewsContract;
import com.ayat.android.wassupworld.retro.ApiClient;
import com.ayat.android.wassupworld.retro.ApiInterface;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import retrofit2.Call;

/**
 * Created by dell on 7/1/2017.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String ACTION_DATA_UPDATED =
            "com.ayat.android.wassupworld.ACTION_DATA_UPDATED";
    private static final int SYNC_INTERVAL = 60 * 30;
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private final static String ACTION = "com.ayat.android.wassupworld.Sync.SyncStatus";
    private final static String SYNCING_STATUS = "syncing";
    private final static String RUNNING = "running";
    private final static String STOPPING = "stopping";
    private final static String FAILED = "failed";
    private static final String API_KEY = BuildConfig.API_KEY;
    private Context mContext;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;

    }

    public static void syncImmediately(Context context) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    private static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    private void updateWidgets() {
        Context context = getContext();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra(SYNCING_STATUS, RUNNING);
        mContext.sendBroadcast(intent);
        Vector<ContentValues> cVVectorNews = new Vector<>();
        Vector<ContentValues> cVVectorSources = new Vector<>();
        ApiInterface taskService = ApiClient.getClient(ApiInterface.class);
        List<Sources> sources = new ArrayList<>();
        Call<ResultSources> call = taskService.getSources(API_KEY, "en");

        try {
            sources = call.execute().body().getSources();
            for (Sources item : sources) {
                String category = item.getCategory();
                String country = item.getCountry();
                String des = item.getDescription();
                String name = item.getName();
                String uri = item.getUrl();
                String urlToImage = item.getUrlToImage();
                String id = item.getId();
                ContentValues itemValues = new ContentValues();

                itemValues.put(NewsContract.SourcesEntry.COLUMN_CATEGORY, category);
                itemValues.put(NewsContract.SourcesEntry.COLUMN_COUNTRY, country);
                itemValues.put(NewsContract.SourcesEntry.COLUMN_DESCRIPTION, des);
                itemValues.put(NewsContract.SourcesEntry.COLUMN_ID, id);
                itemValues.put(NewsContract.SourcesEntry.COLUMN_NAME, name);
                itemValues.put(NewsContract.SourcesEntry.COLUMN_URL_TO_IMAGE, urlToImage);
                itemValues.put(NewsContract.SourcesEntry.COLUMN_URL, uri);

                cVVectorSources.add(itemValues);
            }


        } catch (IOException e) {
            e.printStackTrace();
            intent.setAction(ACTION);
            intent.putExtra(SYNCING_STATUS, FAILED);
            mContext.sendBroadcast(intent);
        }
        for (Sources item : sources) {
            Call<ResultNews> callnews = taskService.getNews(API_KEY, item.getId());

            try {
                ResultNews resultNews = callnews.execute().body();
                List<News> news = resultNews.getArticles();
                String source = resultNews.getSource();
                for (News newsItem : news) {
                    String author = newsItem.getAuthor();
                    String title = newsItem.getTitle();
                    String des = newsItem.getDescription();
                    String date = newsItem.getPublishedAt();
                    String uri = newsItem.getUrl();
                    String urlToImage = newsItem.getUrlToImage();
                    long dateUnix = getUnixTime(date);


                    ContentValues itemValues = new ContentValues();
                    itemValues.put(NewsContract.NewsEntry.COLUMN_AUTHOR, author);
                    itemValues.put(NewsContract.NewsEntry.COLUMN_SOURCE, source);
                    itemValues.put(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE, urlToImage);
                    itemValues.put(NewsContract.NewsEntry.COLUMN_TITLE, title);
                    itemValues.put(NewsContract.NewsEntry.COLUMN_DATE, dateUnix);
                    itemValues.put(NewsContract.NewsEntry.COLUMN_URL, uri);
                    itemValues.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, des);
                    itemValues.put(NewsContract.NewsEntry.COLUMN_CATEGORY, item.getCategory());
                    int COLUMN_LATER_DEFAULT = 0;
                    itemValues.put(NewsContract.NewsEntry.COLUMN_LATER, COLUMN_LATER_DEFAULT);
                    cVVectorNews.add(itemValues);


                }
            } catch (IOException e) {
                e.printStackTrace();
                intent.setAction(ACTION);
                intent.putExtra(SYNCING_STATUS, FAILED);
                mContext.sendBroadcast(intent);
            }


        }

        if (cVVectorSources.size() > 0) {

            ContentValues[] cvArray = new ContentValues[cVVectorSources.size()];
            cVVectorSources.toArray(cvArray);

            getContext().getContentResolver().bulkInsert(NewsContract.SourcesEntry.CONTENT_URI, cvArray);


            if (cVVectorNews.size() > 0) {
                ContentValues[] cvArrayn = new ContentValues[cVVectorNews.size()];
                cVVectorNews.toArray(cvArrayn);
                getContext().getContentResolver().bulkInsert(NewsContract.NewsEntry.CONTENT_URI, cvArrayn);
                Intent stopIntent = new Intent();
                stopIntent.putExtra(SYNCING_STATUS, STOPPING);
                getContext().getContentResolver().delete(NewsContract.NewsEntry.CONTENT_URI,
                        NewsContract.NewsEntry.COLUMN_DATE + " <= ?",
                        new String[]{getUnixTimeBefore(2) + ""});
                stopIntent.setAction(ACTION);
                updateWidgets();
                mContext.sendBroadcast(stopIntent);

            }
        }
    }

    private long getUnixTime(String dateString) {
        if (dateString != null) {
            DateFormat dateFormat;
            String d = dateString.substring(0, 19);
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            Date date;
            try {
                date = dateFormat.parse(d);
                Date today = new Date();
                if (date.after(today)) {
                    date = getDateBefore(1);
                }
            } catch (ParseException e) {

                return 0;
            }

            return date.getTime() / 1000;
        } else {

            return getUnixTimeBefore(1);
        }
    }

    private Date getDateBefore(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }

    private long getUnixTimeBefore(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        Date date = cal.getTime();
        return date.getTime() / 1000;
    }

}
