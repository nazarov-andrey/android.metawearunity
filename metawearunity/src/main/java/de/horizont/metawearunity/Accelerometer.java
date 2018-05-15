package de.horizont.metawearunity;

import android.app.Activity;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.data.Acceleration;

import bolts.Continuation;

public class Accelerometer extends AbstractSensor<Accelerometer, com.mbientlab.metawear.module.Accelerometer, IAccelerometerHandler> {
    private static String TAG = "MetaWearAccelerometer";

    public Accelerometer(Activity activity, MetaWearBoard board) {
        super(activity, board, com.mbientlab.metawear.module.Accelerometer.class);
    }

    public void Start (final IAccelerometerHandler handler)
    {
        Log.d(TAG, "Accelerometer.Start");

        sensor.acceleration().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            Acceleration acceleration = data.value(Acceleration.class);
            float x = acceleration.x();
            float y = acceleration.y();
            float z = acceleration.z();

            Log.i(TAG, "Accelerometer.acceperation " + x + " " + y + " " + z);

            activity.runOnUiThread(() -> handler.OnNewValue(x, y, z));
        })).continueWith((Continuation<Route, Void>) task -> {
            sensor.acceleration().start();
            sensor.start();
            return null;
        });
    }

    public void Stop ()
    {
        Log.d(TAG, "Accelerometer.Stop");
        sensor.stop();
    }
}
