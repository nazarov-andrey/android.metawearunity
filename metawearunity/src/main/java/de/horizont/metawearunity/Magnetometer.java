package de.horizont.metawearunity;

import android.os.Handler;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.data.MagneticField;
import com.mbientlab.metawear.module.MagnetometerBmm150;

import bolts.Continuation;

public class Magnetometer extends AbstractSensor<Magnetometer, MagnetometerBmm150, IMagnetometerHandler> {
    private static String TAG = "MetaWearMagnetometer";

    public Magnetometer(Handler handler, MetaWearBoard board, boolean loggable) {
        super(handler, board, MagnetometerBmm150.class, loggable);
    }

    @Override
    public void Start(IMagnetometerHandler handler) {
        Log("Magnetometer.Start");

        sensor.magneticField().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            MagneticField magneticField = data.value(MagneticField.class);
            float x = magneticField.x();
            float y = magneticField.y();
            float z = magneticField.z();

            Log("Magnetometer.magneticField " + x + " " + y + " " + z);

            this.handler.post(() -> handler.OnNewValue(x, y, z));
        })).continueWith((Continuation<Route, Void>) task -> {
            sensor.usePreset(MagnetometerBmm150.Preset.REGULAR);
            sensor.magneticField().start();
            sensor.start();
            return null;
        });

    }

    @Override
    public void Stop() {
        Log("Magnetometer.Stop");
        sensor.stop();
    }
}
