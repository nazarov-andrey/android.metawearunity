package de.horizont.metawearunity;

import android.app.Activity;

import com.mbientlab.metawear.MetaWearBoard;

public abstract class AbstractSensor<TSelf extends AbstractSensor<TSelf, TSensor, THandler>, TSensor extends MetaWearBoard.Module, THandler> {
    protected TSensor sensor;
    protected Activity activity;

    public AbstractSensor(Activity activity, MetaWearBoard board, Class<TSensor> sensorClass) {
        this.sensor = board.getModule(sensorClass);
        this.activity = activity;
    }

    public abstract void Start (THandler handler);
    public abstract void Stop ();
}
