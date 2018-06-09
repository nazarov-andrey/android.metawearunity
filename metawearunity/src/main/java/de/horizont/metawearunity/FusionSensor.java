package de.horizont.metawearunity;

import android.os.Handler;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.data.EulerAngles;
import com.mbientlab.metawear.module.SensorFusionBosch;

import bolts.Continuation;

public class FusionSensor extends AbstractSensor<FusionSensor, SensorFusionBosch, IFusionSensorHandler> {
    private static String TAG = "MetaWearSensorFusion";

    public FusionSensor(Handler handler, MetaWearBoard board, boolean loggable) {
        super(handler, board, SensorFusionBosch.class, loggable);
    }

    @Override
    public void Start(IFusionSensorHandler handler) {
        Log("SensorFusion.Start");

        sensor.eulerAngles().addRouteAsync(source -> source.stream((Subscriber) (data, env) -> {
            EulerAngles eulerAngles = data.value(EulerAngles.class);

            Log("SensorFusion.eulerAngles " + eulerAngles);

            this.handler.post(() ->
                    handler.OnNewValue(
                            eulerAngles.heading(),
                            eulerAngles.pitch(),
                            eulerAngles.roll(),
                            eulerAngles.yaw()));

        })).continueWith((Continuation<Route, Void>) task -> {
            sensor
                    .configure()
                    .mode(SensorFusionBosch.Mode.M4G)
                    .accRange(SensorFusionBosch.AccRange.AR_8G)
                    .commit();

            sensor.eulerAngles().start();
            sensor.start();
            return null;
        });
    }

    @Override
    public void Stop() {
        Log("SensorFusion.Stop");
    }
}

