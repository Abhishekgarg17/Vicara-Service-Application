package com.abhishek.vicaraserviceapp;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

public class ConnectionManager {

    final public static String NetworkStateOn = "ON";
    final public static String NetworkStateOff = "OFF";
    final public static String NetworkStateMobileData = "Mobile Data";

    final public static String BluetoothStateOn = "ON";
    final public static String BluetoothStateOff = "OFF";
    final public static String BluetoothStateTurningOn = "TURNING_ON";
    final public static String BluetoothStateTurningOff = "TURNING_OFF";

    private static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public static String getNetworkState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities currNetwork = cm.getNetworkCapabilities(cm.getActiveNetwork());

        if (currNetwork == null)
            return NetworkStateOff;
        else if (currNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            return NetworkStateMobileData;
        else if (currNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            return NetworkStateOn;

        return NetworkStateOff;
    }

    public static String getBluetoothState() {
        String currState = "";

        if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
            currState = BluetoothStateOn;
        else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF)
            currState = BluetoothStateOff;
        else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON)
            currState = BluetoothStateTurningOn;
        else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_OFF)
            currState = BluetoothStateTurningOff;

        return currState;
    }

    public static void enableBluetooth() {
        bluetoothAdapter.enable();
    }

    public static void disableBluetooth() {
        bluetoothAdapter.disable();
    }
}
