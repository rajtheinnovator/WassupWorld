package com.example.android.wassupworld.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.wassupworld.Adapter.NewsAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Sync.SyncAdapter;
import com.example.android.wassupworld.Utils.BottomNavigationViewHelper;
import com.lapism.searchview.SearchView;

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private NewsAdapter mNewsAdapter;
    private Fragment selectedFragment = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BottomNavigationView bottomNavigationView;
    public static final String SEARCH_KEY = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SyncAdapter.initializeSyncAdapter(this);
        SyncAdapter.syncImmediately(this);


        final SearchView mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
            @Override
            public boolean onClose() {
               bottomNavigationView.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onOpen() {
                bottomNavigationView.setVisibility(View.GONE);
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshContent();
                    }
                }
        );

bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottom_navigation);
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
                                mSwipeRefreshLayout.setEnabled(false);
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
        }
        checkInternet();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    private void startSerachActivity(String query) {
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra(SEARCH_KEY, query);
        startActivity(intent);
    }
public void showSnackBar(){


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
    public void checkInternet(){
     if(!isNetworkConnected())
         showSnackBar();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SyncAdapter.syncImmediately(MainActivity.this);
                if (selectedFragment instanceof NewsListFragment)
                    ((NewsListFragment) selectedFragment).restartLoader();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }


}
