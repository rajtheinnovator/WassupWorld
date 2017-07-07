package com.example.android.wassupworld.UI;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.wassupworld.Adapter.NewsAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Sync.SyncAdapter;

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private NewsAdapter mNewsAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SyncAdapter.initializeSyncAdapter(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshContent();
                    }
                }
        );

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                              selectedFragment = new NewsListFragment();
                                mSwipeRefreshLayout.setEnabled(true);
                                break;
                            case R.id.action_schedules:
                                selectedFragment = new SourcesFragment();
                                mSwipeRefreshLayout.setEnabled(false);
                                break;
                            case R.id.action_music:
                                selectedFragment = new WatchLaterFragment();

                                mSwipeRefreshLayout.setEnabled(false);
                    break;
                            case R.id.action_Explore:

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
        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);

    }


    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SyncAdapter.syncImmediately(MainActivity.this);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }


}
