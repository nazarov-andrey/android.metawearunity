package de.horizont.metawearunity;

import android.app.Activity;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.module.AmbientLightLtr329;

import bolts.Continuation;

public class Light extends AbstractSensor<Light, AmbientLightLtr329, ILightHandler> {
    private static String TAG = "MetaWearLight";

    public Light(Activity activity, MetaWearBoard board) {
        super(activity, board, AmbientLightLtr329.class);
    }

    @Override
    public void Start(ILightHandler handler) {
        Log.d(TAG, "Light.Start");
        sensor.illuminance().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            float light = data.value(Float.class);

            Log.i(TAG, "Light.light " + light);

            activity.runOnUiThread(() -> handler.OnNewValue(light));
        })).continueWith((Continuation<Route, Void>) task -> {
            sensor.illuminance().start();
            return null;
        });
    }

    @Override
    public void Stop() {
        Log.d(TAG, "Light.Stop");
    }
}
