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
    private boolean loggable;

    public MetaWearManager(Activity activity, Runnable serviceConnectedCallback, boolean loggable) {
        Log("MetaWearManager");

        this.handler = new Handler();
        this.activity = activity;
        this.serviceConnectedCallback = serviceConnectedCallback;
        this.loggable = loggable;

        activity
                .getApplication()
                .bindService(new Intent(activity, BtleService.class),
                        this, Context.BIND_AUTO_CREATE);
    }

    private void Log (String message)
    {
        Log.d(TAG, message);
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
        Log("MetaWearManager.onServiceConnected");
        serviceBinder = (BtleService.LocalBinder) service;

        if (serviceConnectedCallback != null)
            handler.post(serviceConnectedCallback);
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
        Log("GetBoard " + macAddress);

        final BluetoothDevice remoteDevice= GetBTAdapter().getRemoteDevice(macAddress);
        return serviceBinder.getMetaWearBoard(remoteDevice);
    }

    public void ConnectToTheBoard (MetaWearBoard board, final IBoardConnectionHandler handler)
    {
        Log("ConnectToTheBoard");

        board.connectAsync().continueWith((Continuation<Void, Void>) task -> {
            if (task.isFaulted()) {
                this.handler.post(() -> handler.OnFailed());
            } else {
                Log("Connected, board: " + board.getModelString());
                this.handler.post(() -> handler.OnConnected());
            }
            return null;
        });

        board.onUnexpectedDisconnect(status -> this.handler.post(() -> handler.OnUnexpectedDisconnect()));
    }

    public de.horizont.metawearunity.Accelerometer GetAcceleromenter (MetaWearBoard board)
    {
        return new de.horizont.metawearunity.Accelerometer(handler, board, loggable);
    }

    public Magnetometer GetMagnetometer (MetaWearBoard board)
    {
        return new Magnetometer(handler, board,loggable);
    }

    public Gyro GetGyro (MetaWearBoard board)
    {
        return new Gyro(handler, board,loggable);
    }

    public Light GetLight (MetaWearBoard board)
    {
        return new Light(handler, board,loggable);
    }

    public Barometer GetBarometer (MetaWearBoard board)
    {
        return new Barometer(handler, board, loggable);
    }

    public FusionSensor GetFusionSensor (MetaWearBoard board)
    {
        return new FusionSensor(handler, board, loggable);
    }

    public void Disconnect (MetaWearBoard board)
    {
        board.disconnectAsync();
    }
}
