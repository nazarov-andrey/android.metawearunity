package de.horizont.metawearunity;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.data.AngularVelocity;
import com.mbientlab.metawear.module.GyroBmi160;

import bolts.Continuation;

public class Gyro extends AbstractSensor<Gyro, GyroBmi160, IGyroHandler> {
    private static String TAG = "MetaWearGyro";

    public Gyro(Handler handler, MetaWearBoard board, boolean loggable) {
        super(handler, board, GyroBmi160.class, loggable);
    }

    @Override
    public void Start(IGyroHandler handler) {
        Log("Gyro.Start");
        sensor.angularVelocity().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            AngularVelocity angularVelocity = data.value(AngularVelocity.class);
            float x = angularVelocity.x();
            float y = angularVelocity.y();
            float z = angularVelocity.z();

            Log("Gyro.angularVelocity " + x + " " + y + " " + z);

            this.handler.post(() -> handler.OnNewValue(x, y, z));
        })).continueWith((Continuation<Route, Void>) task -> {
            sensor.angularVelocity();
            sensor.start();
            return null;
        });
    }

    @Override
    public void Stop() {
        Log("Gyro.Stop");
        sensor.stop();
    }
}
