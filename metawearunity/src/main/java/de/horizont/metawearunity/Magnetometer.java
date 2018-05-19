package de.horizont.metawearunity;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.data.MagneticField;
import com.mbientlab.metawear.module.MagnetometerBmm150;

import bolts.Continuation;
import bolts.Task;

public class Magnetometer extends AbstractSensor<Magnetometer, MagnetometerBmm150, IMagnetometerHandler> {
    private static String TAG = "MetaWearMagnetometer";

    public Magnetometer(Handler handler, MetaWearBoard board) {
        super(handler, board, MagnetometerBmm150.class);
    }

    @Override
    public void Start(IMagnetometerHandler handler) {
        Log.d(TAG, "Magnetometer.Start");

        sensor.magneticField().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            MagneticField magneticField = data.value(MagneticField.class);
            float x = magneticField.x();
            float y = magneticField.y();
            float z = magneticField.z();

            Log.i(TAG, "Magnetometer.magneticField " + x + " " + y + " " + z);

            this.handler.post(() -> handler.OnNewValue(x, y, z));
        })).continueWith((Continuation<Route, Void>) task -> {
            sensor.magneticField().start();
            sensor.start();
            return null;
        });

    }

    @Override
    public void Stop() {
        Log.d(TAG, "Magnetometer.Stop");
        sensor.stop();
    }
}
