package de.horizont.metawearunity;

import android.bluetooth.BluetoothDevice;

public interface IDeviceHandler
{
    void OnNewDevice(BluetoothDevice device);
}
