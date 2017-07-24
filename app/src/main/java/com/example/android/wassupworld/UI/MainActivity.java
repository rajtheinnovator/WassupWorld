package com.example.android.wassupworld.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.android.wassupworld.Adapter.NewsAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Sync.SyncAdapter;
import com.example.android.wassupworld.Utils.BottomNavigationViewHelper;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;

import static com.example.android.wassupworld.R.id.searchView;

public class MainActivity extends AppCompatActivity {
    public static final String SEARCH_KEY = "search";
    private final static String ACTION = "com.example.android.wassupworld.Sync.SyncStatus";
    private final static String SYNCING_STATUS = "syncing";
    private final static String RUNNING = "running";
    private final static String STOPPING = "stopping";
    public SyncReceiver myReceiver;
    public SearchView mSearchView;
    public CoordinatorLayout mPlaceSankBar;
    private boolean mTwoPane;
    private NewsAdapter mNewsAdapter;
    private LinearLayout mLinearLayout;
    private Fragment selectedFragment = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myReceiver = new SyncReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(myReceiver, filter);
        mLinearLayout = (LinearLayout) findViewById(R.id.coordinator_layout_contatiner);
        mPlaceSankBar = (CoordinatorLayout) findViewById(R.id.placeSnackBar);
        final String PREFS_NAME = "MyPrefsFile";

//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//
//        if (settings.getBoolean("my_first_time", true)) {
//            //the app is being launched for first time, do something
//            SyncAdapter.syncImmediately(this);
//
//            // first time task
//
//            // record the fact that the app has been started at least once
//            settings.edit().putBoolean("my_first_time", false).commit();
//        }


        mSearchView = (SearchView) findViewById(searchView);
        mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
            @Override
            public boolean onClose() {
                bottomNavigationView.setVisibility(View.VISIBLE);
                mPlaceSankBar.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onOpen() {
                bottomNavigationView.setVisibility(View.GONE);
                mPlaceSankBar.setVisibility(View.GONE);
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSerachActivity(query);
                mSearchView.clearFocus();
                mSearchView.setQuery("", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSearchView.setVoice(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshContent();
                    }
                }
        );


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_lastet:
                                selectedFragment = new NewsListFragment();
                                mSwipeRefreshLayout.setEnabled(true);
                                break;
                            case R.id.action_sources:
                                selectedFragment = new SourcesFragment();
                                mSwipeRefreshLayout.setEnabled(true);
                                break;
                            case R.id.action_bookmark:
                                selectedFragment = new WatchLaterFragment();
                                mSwipeRefreshLayout.setEnabled(false);
                                break;
                            case R.id.action_explore:

                                selectedFragment = new CategoryFragment();
                                mSwipeRefreshLayout.setEnabled(false);
                                break;

                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.fragment_container, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new NewsListFragment());
            transaction.commit();
            selectedFragment = new NewsListFragment();
            mSwipeRefreshLayout.setEnabled(true);
        }
        SyncAdapter.initializeSyncAdapter(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd, false);
                    startSerachActivity(searchWrd);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void startSerachActivity(String query) {
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra(SEARCH_KEY, query);
        startActivity(intent);
    }

    public void showSnackBar() {


        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.placeSnackBar),
                R.string.no_internet, Snackbar.LENGTH_LONG);
        mySnackbar.setActionTextColor(getResources().getColor(R.color.red));

        mySnackbar.setAction(R.string.retry_string, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternet();
            }
        });
        mySnackbar.show();
    }

    public void checkInternet() {
        if (!isNetworkConnected())
            showSnackBar();
//        else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                   if (selectedFragment instanceof NewsListFragment)
//                        ((NewsListFragment) selectedFragment).restartLoader();
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
//            }, 3000);
//            ;
//        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    private void refreshContent() {
        SyncAdapter.syncImmediately(MainActivity.this);


    }

    public class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();


            if (extras != null) {

                if (extras.get(SYNCING_STATUS).equals(STOPPING)) {
                    Log.e("ayat", "sync stopped");
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        }

    }
}