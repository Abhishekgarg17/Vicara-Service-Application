package com.abhishek.vicaraserviceapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StateChangeBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String networkStatus = ConnectionManager.getNetworkState(context);
        String bluetoothStatus = ConnectionManager.getBluetoothState();

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, NetworkService.getNotification(context, networkStatus, bluetoothStatus));

        MainActivity.instance.updateUI();
    }
}
