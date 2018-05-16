package de.horizont.metawearunity;

import android.app.Activity;
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
import de.horizont.activitylifecycle.IActivityLifecycleHandler;

public class MetaWearManager implements IActivityLifecycleHandler, ServiceConnection {
    private static String TAG = "MetaWear";

    private Activity activity;
    private BtleService.LocalBinder serviceBinder;
    private BluetoothAdapter bluetoothAdapter;
    private Handler handler;
    private Runnable serviceConnectedCallback;

    public MetaWearManager(Activity activity, Runnable serviceConnectedCallback) {
        Log.d(TAG, "MetaWearManager");

        this.activity = activity;
        this.serviceConnectedCallback = serviceConnectedCallback;

        activity
                .getApplication()
                .bindService(new Intent(activity, BtleService.class),
                        this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        activity
                .getApplicationContext()
                .unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "MetaWearManager.onServiceConnected");
        serviceBinder = (BtleService.LocalBinder) service;

        if (serviceConnectedCallback != null)
            activity.runOnUiThread(serviceConnectedCallback);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    private BluetoothManager GetBTManager ()  {
        return (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    private BluetoothAdapter GetBTAdapter ()
    {
        return GetBTManager().getAdapter();
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
                activity.runOnUiThread(() -> handler.OnFailed());
            } else {
                activity.runOnUiThread(() -> handler.OnConnected());
            }
            return null;
        });

        board.onUnexpectedDisconnect(status -> activity.runOnUiThread(() -> handler.OnUnexpectedDisconnect()));
    }

    public de.horizont.metawearunity.Accelerometer GetAcceleromenter (MetaWearBoard board)
    {
        return new de.horizont.metawearunity.Accelerometer(activity, board);
    }

    public Magnetometer GetMagnetometer (MetaWearBoard board)
    {
        return new Magnetometer(activity, board);
    }

    public Gyro GetGyro (MetaWearBoard board)
    {
        return new Gyro(activity, board);
    }

    public Light GetLight (MetaWearBoard board)
    {
        return new Light(activity, board);
    }
}
