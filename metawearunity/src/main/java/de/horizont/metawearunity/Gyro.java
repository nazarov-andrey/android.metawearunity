package de.horizont.metawearunity;

import android.app.Activity;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.data.AngularVelocity;
import com.mbientlab.metawear.module.GyroBmi160;

import bolts.Continuation;

public class Gyro extends AbstractSensor<Gyro, GyroBmi160, IGyroHandler> {
    private static String TAG = "MetaWearGyro";

    public Gyro(Activity activity, MetaWearBoard board) {
        super(activity, board, GyroBmi160.class);
    }

    @Override
    public void Start(IGyroHandler handler) {
        Log.d(TAG, "Gyro.Start");
        sensor.angularVelocity().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            AngularVelocity angularVelocity = data.value(AngularVelocity.class);
            float x = angularVelocity.x();
            float y = angularVelocity.y();
            float z = angularVelocity.z();

            Log.i(TAG, "Gyro.angularVelocity " + x + " " + y + " " + z);

            activity.runOnUiThread(() -> handler.OnNewValue(x, y, z));
        })).continueWith((Continuation<Route, Void>) task -> {
            sensor.angularVelocity();
            sensor.start();
            return null;
        });
    }

    @Override
    public void Stop() {
        Log.d(TAG, "Gyro.Stop");
        sensor.stop();
    }
}
