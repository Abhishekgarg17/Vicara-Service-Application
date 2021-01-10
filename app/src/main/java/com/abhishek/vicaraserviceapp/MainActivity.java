package com.abhishek.vicaraserviceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
        Intent serviceIntent = new Intent(this, NetworkService.class);
        serviceIntent.putExtra("network_data_extra", networkState);
        serviceIntent.putExtra("bluetooth_data_extra", bluetoothState);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, NetworkService.class);
        stopService(serviceIntent);
    }

    public void updateUI() {
        createWifiView();
        createBluetoothView();
    }
}