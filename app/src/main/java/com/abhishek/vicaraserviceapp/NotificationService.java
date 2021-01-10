package com.abhishek.vicaraserviceapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.abhishek.vicaraserviceapp.AppNotificationChannel.CHANNEL_ID;

public class NotificationService extends Service {

    public static Notification getNotification(Context context, String networkState, String bluetoothState){
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Network Service State")
                .setContentText("Network : " + networkState + "\nBluetooth : " + bluetoothState)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .build();
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String networkState = intent.getStringExtra("network_data_extra");
        String bluetoothState = intent.getStringExtra("bluetooth_data_extra");

        Notification notification = getNotification(this,networkState,bluetoothState);
        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
