package de.horizont.metawearunity;

import android.os.Handler;

import com.mbientlab.metawear.MetaWearBoard;

public abstract class AbstractSensor<TSelf extends AbstractSensor<TSelf, TSensor, THandler>, TSensor extends MetaWearBoard.Module, THandler> {
    protected TSensor sensor;
    protected Handler handler;

    public AbstractSensor(Handler handler, MetaWearBoard board, Class<TSensor> sensorClass) {
        this.sensor = board.getModule(sensorClass);
        this.handler = handler;
    }

    public abstract void Start (THandler handler);
    public abstract void Stop ();
}
