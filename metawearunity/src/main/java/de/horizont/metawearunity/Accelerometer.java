package de.horizont.metawearunity;

import android.os.Handler;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.data.Acceleration;

import bolts.Continuation;

public class Accelerometer extends AbstractSensor<Accelerometer, com.mbientlab.metawear.module.Accelerometer, IAccelerometerHandler> {
    private static String TAG = "MetaWearAccelerometer";

    public Accelerometer(Handler handler, MetaWearBoard board, boolean loggable) {
        super(handler, board, com.mbientlab.metawear.module.Accelerometer.class, loggable);
    }

    public void Start (final IAccelerometerHandler handler)
    {
        Log("Accelerometer.Start");

        sensor.acceleration().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            Acceleration acceleration = data.value(Acceleration.class);
            float x = acceleration.x();
            float y = acceleration.y();
            float z = acceleration.z();

            Log("Accelerometer.acceperation " + x + " " + y + " " + z);

            this.handler.post(() -> handler.OnNewValue(x, y, z));
        })).continueWith((Continuation<Route, Void>) task -> {
            sensor.acceleration().start();
            sensor.start();
            return null;
        });
    }

    public void Stop ()
    {
        Log("Accelerometer.Stop");
        sensor.stop();
    }
}
