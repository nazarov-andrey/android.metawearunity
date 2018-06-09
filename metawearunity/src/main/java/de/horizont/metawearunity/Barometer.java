package de.horizont.metawearunity;

import android.os.Handler;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.module.BarometerBosch;

import bolts.Continuation;

public class Barometer extends AbstractSensor<Barometer, BarometerBosch, IBarometerHandler> {
    private static String TAG = "MetaWearBarometer";

    public Barometer(Handler handler, MetaWearBoard board, boolean loggable) {
        super(handler, board, BarometerBosch.class, loggable);
    }

    @Override
    public void Start(IBarometerHandler handler) {
        Log("Barometer.Start");

        sensor.pressure().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            float pressure = data.value(Float.class);

            Log("Barometer.pressure " + pressure);

            this.handler.post(() -> handler.OnNewValue(pressure));
        })).continueWith((Continuation<Route, Void>) task -> {
            sensor.configure()
                    .filterCoeff(BarometerBosch.FilterCoeff.AVG_16)
                    .pressureOversampling(BarometerBosch.OversamplingMode.ULTRA_HIGH)
                    .standbyTime(0.5f)
                    .commit();

            sensor.pressure().start();
            sensor.start();
            return null;
        });

    }

    @Override
    public void Stop() {
        Log("Barometer.Stop");
        sensor.stop();
    }
}
