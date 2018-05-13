package de.horizont.metawearunity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.android.BtleService;
import com.unity3d.player.UnityPlayerActivity;

import bolts.Continuation;

public class MetaWearUnityActivity extends UnityPlayerActivity implements ServiceConnection {
    private static String TAG = "MetaWear";

    private BtleService.LocalBinder serviceBinder;
    private BluetoothAdapter bluetoothAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bind the service when the activity is created
        Log.d(TAG, "MetaWearUnityActivity.onCreate");
        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unbind the service when the activity is destroyed
        getApplicationContext().unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // Typecast the binder to the service's LocalBinder class
        Log.d(TAG, "MetaWearUnityActivity.onServiceConnected");
        serviceBinder = (BtleService.LocalBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) { }

    private BluetoothManager GetBTManager ()
    {
        return (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    }

    private BluetoothAdapter GetBTAdapter ()
    {
        return GetBTManager().getAdapter();
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {

                    Log.d(TAG, "mLeScanCallback.onLeScan " + device.getAddress() + " " + device.getName());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (deviceHandler != null)
                                deviceHandler.OnNewDevice(device);
                        }
                    });
                }
            };

    private IDeviceHandler deviceHandler;

    public void ScanDevices(final boolean enable, IDeviceHandler deviceHandler, long timeout) {
        Log.d(TAG, "MetaWearUnityActivity.ScanDevices " + enable);
        if (enable) {
            if (handler == null)
                handler = new Handler();

            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(() -> {
                Log.d(TAG, "MetaWearUnityActivity stop by timeout");
                GetBTAdapter().stopLeScan(mLeScanCallback);
            }, timeout * 10000);

            boolean result = GetBTAdapter().startLeScan(mLeScanCallback);
            Log.d(TAG, "startLeScan " + result);
        } else {
            GetBTAdapter().stopLeScan(mLeScanCallback);
        }
    }

    public MetaWearBoard GetBoard (String macAddress)
    {
        Log.d(TAG, "GetBoard " + macAddress);

        final BluetoothDevice remoteDevice= GetBTAdapter().getRemoteDevice(macAddress);
        return serviceBinder.getMetaWearBoard(remoteDevice);
    }

    public void ConnectToTheBoard (MetaWearBoard board, final IBoardConnectionHandler handler)
    {
        Log.d(TAG, "ConnectToTheBoard");

        board.connectAsync().continueWith((Continuation<Void, Void>) task -> {
            if (task.isFaulted()) {
                runOnUiThread(() -> handler.OnFailed());
            } else {
                runOnUiThread(() -> handler.OnConnected());
            }
            return null;
        });

        board.onUnexpectedDisconnect(status -> runOnUiThread(() -> handler.OnUnexpectedDisconnect()));
    }

    public de.horizont.metawearunity.Accelerometer GetAcceleromenter (MetaWearBoard board)
    {
        return new de.horizont.metawearunity.Accelerometer(this, board);
    }
}
