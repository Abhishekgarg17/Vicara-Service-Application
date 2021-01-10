package com.abhishek.vicaraserviceapp;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class NetworkSchedulerService extends JobService implements
            ConnectivityReceiver.ConnectivityReceiverListener {

        private ConnectivityReceiver mConnectivityReceiver;

        @Override
        public void onCreate() {
            super.onCreate();
            mConnectivityReceiver = new ConnectivityReceiver(this);
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d("TAG", "onStartCommand");
            return START_NOT_STICKY;
        }


        @Override
        public boolean onStartJob(JobParameters params) {
            Log.d("TAG", "onStartJob" + mConnectivityReceiver);
            IntentFilter it = new IntentFilter();
            it.addAction(CONNECTIVITY_SERVICE);
            it.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(mConnectivityReceiver, it);
            return true;
        }

        @Override
        public boolean onStopJob(JobParameters params) {
            Log.d("TAG", "onStopJob");
            unregisterReceiver(mConnectivityReceiver);
            return true;
        }

        @Override
        public void onNetworkConnectionChanged() {
            MainActivity.instance.updateUI();
        }

}
