package com.example.android.wassupworld.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by dell on 7/1/2017.
 */

public class SyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private SyncAdapter mSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SyncService", "onCreate - SyncService");
        synchronized (sSyncAdapterLock) {
            if (mSyncAdapter == null) {
                mSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSyncAdapter.getSyncAdapterBinder();
    }
}