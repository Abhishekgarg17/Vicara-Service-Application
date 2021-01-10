package com.abhishek.vicaraserviceapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class ConnectivityReceiver extends BroadcastReceiver {
    private ConnectivityReceiverListener mConnectivityReceiverListener;

    ConnectivityReceiver(ConnectivityReceiverListener listener) {
        mConnectivityReceiverListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mConnectivityReceiverListener.onNetworkConnectionChanged();

        String networkStatus = ConnectionManager.getNetworkState(context);
        String bluetoothStatus = ConnectionManager.getBluetoothState();

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, NotificationService.getNotification(context, networkStatus, bluetoothStatus));

    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged();
    }
}
