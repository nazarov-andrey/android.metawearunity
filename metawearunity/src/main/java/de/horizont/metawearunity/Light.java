package de.horizont.metawearunity;

import android.os.Handler;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.module.AmbientLightLtr329;

import bolts.Continuation;

public class Light extends AbstractSensor<Light, AmbientLightLtr329, ILightHandler> {
    private static String TAG = "MetaWearLight";

    public Light(Handler handler, MetaWearBoard board, boolean loggable) {
        super(handler, board, AmbientLightLtr329.class, loggable);
    }

    @Override
    public void Start(ILightHandler handler) {
        Log("Light.Start");
        sensor.illuminance().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            float light = data.value(Float.class);

            Log("Light.light " + light);

            this.handler.post(() -> handler.OnNewValue(light));
        })).continueWith((Continuation<Route, Void>) task -> {
            sensor.illuminance().start();
            return null;
        });
    }

    @Override
    public void Stop() {
        Log("Light.Stop");
    }
}
