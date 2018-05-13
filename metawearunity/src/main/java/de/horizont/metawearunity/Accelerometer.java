package de.horizont.metawearunity;

import android.app.Activity;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.data.Acceleration;

import bolts.Continuation;

public class Accelerometer {
    private static String TAG = "Accelerometer";

    private com.mbientlab.metawear.module.Accelerometer accelerometer;
    private Activity activity;

    public Accelerometer(Activity activity, MetaWearBoard board) {
        this.accelerometer = board.getModule(com.mbientlab.metawear.module.Accelerometer.class);
        this.activity = activity;
    }

    public void Start (final IAccelerometerHandler handler)
    {
        Log.d(TAG, "Accelerometer.Start");

        accelerometer.acceleration().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            Acceleration acceleration = data.value(Acceleration.class);
            float x = acceleration.x();
            float y = acceleration.y();
            final float z = acceleration.z();

            Log.i(TAG, "Accelerometer.acceperation " + x + " " + y + " " + z);

            activity.runOnUiThread(() -> handler.OnNewValue(x, y, z));
        })).continueWith((Continuation<Route, Void>) task -> {
            accelerometer.acceleration().start();
            accelerometer.start();
            return null;
        });
    }

    public void Stop ()
    {
        Log.d(TAG, "Accelerometer.Stop");
        accelerometer.stop();
    }
}
