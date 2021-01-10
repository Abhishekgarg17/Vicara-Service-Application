package com.abhishek.vicaraserviceapp;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView bluetoothStatusTextView = null;
    private Switch bluetoothToggleSwitch = null;

    private String networkState = "";
    private String bluetoothState = "";

    static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StateChangeBroadcastReceiver receiver = new StateChangeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);

        createWifiView();
        createBluetoothView();
        startService();
        createServiceView();
    }

    private void createServiceView() {
        Button startButton = findViewById(R.id.startNotificationService);
        Button stopButton = findViewById(R.id.stopNotificationService);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
    }

    private void createWifiView() {
        TextView networkStatusTextView = findViewById(R.id.wifiStatus);
        networkState = ConnectionManager.getNetworkState(getApplicationContext());
        networkStatusTextView.setText(networkState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            scheduleJob();
    }

    private void scheduleJob() {
        JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, NetworkSchedulerService.class))
                .setMinimumLatency(500)
                .setOverrideDeadline(1000)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);
        Log.d("MainActivity","Job Scheduled");
    }

    @SuppressLint("SetTextI18n")
    private void createBluetoothView() {
        bluetoothState = ConnectionManager.getBluetoothState();

        bluetoothStatusTextView = findViewById(R.id.bluetoothStatus);
        bluetoothStatusTextView.setText(bluetoothState);

        bluetoothToggleSwitch = findViewById(R.id.bluetoothToggleSwitch);

        if (bluetoothState.equals(ConnectionManager.BluetoothStateOn)) {
            bluetoothToggleSwitch.setChecked(true);
            bluetoothToggleSwitch.setText("Turn Off Bluetooth");
        } else if (bluetoothState.equals(ConnectionManager.BluetoothStateOff)) {
            bluetoothToggleSwitch.setChecked(false);
            bluetoothToggleSwitch.setText("Turn On Bluetooth");
        } else if (bluetoothState.equals(ConnectionManager.BluetoothStateTurningOn)) {
            bluetoothToggleSwitch.setText("Turning On Bluetooth");
        } else if (bluetoothState.equals(ConnectionManager.BluetoothStateTurningOff)) {
            bluetoothToggleSwitch.setText("Turning Off Bluetooth");
        }

        bluetoothToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if ((!bluetoothToggleSwitch.isChecked())) {
                    ConnectionManager.disableBluetooth();
                    bluetoothToggleSwitch.setText("Turn On Bluetooth");
                    bluetoothStatusTextView.setText("OFF");
                } else {
                    ConnectionManager.enableBluetooth();
                    bluetoothToggleSwitch.setText("Turn Off Bluetooth");
                    bluetoothStatusTextView.setText("ON");
                }
            }
        });
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("network_data_extra", networkState);
        serviceIntent.putExtra("bluetooth_data_extra", bluetoothState);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }

    public void updateUI() {
        createWifiView();
        createBluetoothView();
    }
}