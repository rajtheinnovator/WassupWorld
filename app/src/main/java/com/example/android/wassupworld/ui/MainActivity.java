package com.example.android.wassupworld.ui;

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
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.sync.SyncAdapter;
import com.example.android.wassupworld.utils.BottomNavigationViewHelper;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String SEARCH_KEY = "search";
    private final static String ACTION = "com.example.android.wassupworld.Sync.SyncStatus";
    private final static String SYNCING_STATUS = "syncing";
    private final static String STOPPING = "stopping";
    private final static String FAILED = "failed";
    private SearchView mSearchView;
    private CoordinatorLayout mPlaceSankBar;
    private Fragment selectedFragment = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BottomNavigationView bottomNavigationView;
    private SyncReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mReceiver = new SyncReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(mReceiver, filter);
        mPlaceSankBar = (CoordinatorLayout) findViewById(R.id.placeSnackBar);
        mSearchView = (SearchView) findViewById(R.id.searchView);
        if (!isNetworkConnected())
            showSnackBar();

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
                startSearchActivity(query);
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
                            case R.id.action_latest:
                                selectedFragment = new NewsListFragment();
                                mSwipeRefreshLayout.setEnabled(true);
                                break;
                            case R.id.action_sources:
                                selectedFragment = new SourcesFragment();
                                mSwipeRefreshLayout.setEnabled(true);
                                break;
                            case R.id.action_bookmark:
                                selectedFragment = new ReadLaterFragment();
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
                    startSearchActivity(searchWrd);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void startSearchActivity(String query) {
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra(SEARCH_KEY, query);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }

    private void showSnackBar() {

        Snackbar snackbar = Snackbar.make(findViewById(R.id.placeSnackBar),
                R.string.no_internet, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));

        snackbar.setAction(R.string.retry_string, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkConnected())
                    showSnackBar();
                else {
                    SyncAdapter.syncImmediately(MainActivity.this);

                }
            }
        });
        snackbar.show();
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    private void refreshContent() {
        if (isNetworkConnected()) {
            SyncAdapter.syncImmediately(MainActivity.this);
        } else
            mSwipeRefreshLayout.setRefreshing(false);
    }

    public void refreshClick(View view) {
        if (isNetworkConnected()) {
            refreshContent();
            mSwipeRefreshLayout.setRefreshing(true);
        }


    }

    public class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();


            if (extras != null) {

                if (extras.get(SYNCING_STATUS).equals(STOPPING)) {
                    mSwipeRefreshLayout.setRefreshing(false);
                } else if (extras.get(SYNCING_STATUS).equals(FAILED)) {
                    mSwipeRefreshLayout.setRefreshing(false);


                }

            }
        }

    }
}