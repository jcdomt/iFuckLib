package com.wzjer.fucklib.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Beacon {

    private static final String TAG = "BeaconTransmitter";
    private BluetoothLeAdvertiser advertiser;
    private AdvertiseCallback advertiseCallback;




    public Beacon(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Bluetooth is not enabled.");
            return;
        }

        advertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        if (advertiser == null) {
            Log.e(TAG, "Failed to create BluetoothLeAdvertiser.");
            return;
        }

        startAdvertising();
    }

    @SuppressLint("MissingPermission")
    private void startAdvertising() {
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setConnectable(false)
                .build();

        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .addManufacturerData(0x004C, buildIBeaconData())
                .build();

        advertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.i(TAG, "Advertising successfully started");
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e(TAG, "Advertising failed to start: " + errorCode);
            }
        };


        advertiser.startAdvertising(settings, advertiseData, advertiseCallback);
    }

    private byte[] buildIBeaconData() {
        ByteBuffer buffer = ByteBuffer.allocate(23);
        buffer.put((byte) 0x02); // Length
        buffer.put((byte) 0x15); // Type
        buffer.putLong(UUID.fromString("FDA50693-A4E2-4FB1-AFCF-C6EB07647825").getMostSignificantBits()); // UUID
        buffer.putLong(UUID.fromString("FDA50693-A4E2-4FB1-AFCF-C6EB07647825").getLeastSignificantBits()); // UUID
        buffer.putShort((short) 10199); // Major
        buffer.putShort((short) 42474); // Minor
        buffer.put((byte) 0xC5); // Tx Power
        return buffer.array();
    }

    @SuppressLint("MissingPermission")
    public void stopAdvertising() {
        if (advertiser != null && advertiseCallback != null) {

            advertiser.stopAdvertising(advertiseCallback);
            Log.i(TAG, "Advertising stopped");
        }
    }
}