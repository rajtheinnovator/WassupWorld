package com.example.android.wassupworld.Sync;

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
import android.util.Log;

import com.example.android.wassupworld.Model.News;
import com.example.android.wassupworld.Model.ResultNews;
import com.example.android.wassupworld.Model.ResultSources;
import com.example.android.wassupworld.Model.Sources;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;
import com.example.android.wassupworld.retro.ApiClient;
import com.example.android.wassupworld.retro.ApiInterface;

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
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dell on 7/1/2017.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 60 * 30;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private List<Sources> sources = new ArrayList<>();
    private final static String ACTION = "com.example.android.wassupworld.Sync.SyncStatus";
    private final static String SYNCING_STATUS = "syncing";
    private final static String RUNNING = "running";
    private final static String FAILED = "failed";
    public Context mContext;
    private final static String STOPPING = "stopping";
    public static int COLUMN_LATER_DEFAULT=0;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("ayat",  "on perform called");

        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra(SYNCING_STATUS, RUNNING);
        mContext.sendBroadcast(intent);
        String language = "en";
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResultSources> callSources = apiService.getSources("d9ebed9cf5694022bfa2bbe3e9b5d15a", language);

        callSources.enqueue(new Callback<ResultSources>() {


            @Override
            public void onResponse(Call<ResultSources> call, Response<ResultSources> response) {
                sources = response.body().getSources();
                Log.e("ayat", sources.size()+ " sources get");
                Vector<ContentValues> cVVector = new Vector<>(sources.size());
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
                    itemValues.put(NewsContract.SourcesEntry.COLUMN_COUNTRYY, country);
                    itemValues.put(NewsContract.SourcesEntry.COLUMN_DESCRIPTION, des);
                    itemValues.put(NewsContract.SourcesEntry.COLUMN_ID, id);
                    itemValues.put(NewsContract.SourcesEntry.COLUMN_NAME, name);
                    itemValues.put(NewsContract.SourcesEntry.COLUMN_URL_TO_IMAGE, urlToImage);
                    itemValues.put(NewsContract.SourcesEntry.COLUMN_URL, uri);

                    cVVector.add(itemValues);
                }
                if (cVVector.size() > 0) {
                    Log.e("ayat",  "sorce  added"+cVVector.size());

                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    int deleted=    getContext().getContentResolver().delete(NewsContract.SourcesEntry.CONTENT_URI, null, null);
                    Log.e("ayat",  "sorce  deleted"+deleted);

                    getContext().getContentResolver().bulkInsert(NewsContract.SourcesEntry.CONTENT_URI, cvArray);

                }
            }

            @Override
            public void onFailure(Call<ResultSources> call, Throwable t) {
                Log.e("ayat", t.getMessage()+ " failed to add sources");
                Intent intent = new Intent();
                intent.setAction(ACTION);
                intent.putExtra(SYNCING_STATUS, FAILED);
                mContext.sendBroadcast(intent);
            }
        });

        for (final Sources source : sources) {

            Call<ResultNews> callNews = apiService.getNews("d9ebed9cf5694022bfa2bbe3e9b5d15a", source.getId());

            callNews.enqueue(new Callback<ResultNews>() {


                @Override
                public void onResponse(Call<ResultNews> call, Response<ResultNews> response) {
                    List<News> news = response.body().getArticles();
                    Vector<ContentValues> cVVector = new Vector<ContentValues>(news.size());

                    for (News item : news) {
                        Log.e("ayat", source.getName()+ "get news");


                        String auther = item.getAuthor();
                        String title = item.getTitle();
                        String des = item.getDescription();
                        String date = item.getPublishedAt();
                        String uri = item.getUrl();
                        String urlToImage = item.getUrlToImage();

                        long dateUnix = getUnixTime(date);
                        ContentValues itemValues = new ContentValues();

                        itemValues.put(NewsContract.NewsnEntry.COLUMN_AUTHOR, auther);
                        itemValues.put(NewsContract.NewsnEntry.COLUMN_SOURCE, response.body().getSource());
                        itemValues.put(NewsContract.NewsnEntry.COLUMN_URL_TO_IMAGE, urlToImage);
                        itemValues.put(NewsContract.NewsnEntry.COLUMN_TITLE, title);
                        itemValues.put(NewsContract.NewsnEntry.COLUMN_DATE, dateUnix);
                        itemValues.put(NewsContract.NewsnEntry.COLUMN_URL, uri);
                        itemValues.put(NewsContract.NewsnEntry.COLUMN_DESCRIPTION, des);
                        itemValues.put(NewsContract.NewsnEntry.COLUMN_CATEGORY, source.getCategory());
                        itemValues.put(NewsContract.NewsnEntry.COLUMN_LATER, COLUMN_LATER_DEFAULT);

                        cVVector.add(itemValues);
                    }
                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);
                        int added=     getContext().getContentResolver().bulkInsert(NewsContract.NewsnEntry.CONTENT_URI, cvArray);
                        Log.e("ayat", added + "number of added news");


                    }
                }

                @Override
                public void onFailure(Call<ResultNews> call, Throwable t) {
                    Log.e("ayat", t.getMessage()+ " failed to add news");
                    Intent intent = new Intent();
                    intent.setAction(ACTION);
                    intent.putExtra(SYNCING_STATUS, FAILED);
                    mContext.sendBroadcast(intent);
                }
            });
        }


        int deleted = getContext().getContentResolver().delete(NewsContract.NewsnEntry.CONTENT_URI,
                NewsContract.NewsnEntry.COLUMN_DATE + " <= ?",
                new String[]{getUnixTimeBefore(2) + ""});
        Log.e("ayat", deleted + "number of deleted news");

        intent.putExtra(SYNCING_STATUS, STOPPING);
        mContext.sendBroadcast(intent);
    }

    private long getUnixTime(String dateString) {
        if (dateString != null) {
            DateFormat dateFormat = null;
            String d = dateString.substring(0, 19);
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            Date date = null;
            try {
                date = dateFormat.parse(d);
                Date today = new Date();
                if (date.after(today)) {
                    date = getDateBefore(1);
                }
            } catch (ParseException e) {
                Log.e("ayat", e.getErrorOffset() + "");
                return 0;
            }
            long unixTime = (long) date.getTime() / 1000;
            return unixTime;
        } else {
            Log.e("ayat", "string date equal null" + "");
            return getUnixTimeBefore(1);
        }
    }

    public static void syncImmediately(Context context) {
        Log.e("ayat", "syncImmediately");
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
    public static Account getSyncAccount(Context context) {
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

    public Date getDateBefore(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        Date date = cal.getTime();
        return date;
    }

    public long getUnixTimeBefore(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        Date date = cal.getTime();
        long unixTime = (long) date.getTime() / 1000;
        return unixTime;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
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

}
