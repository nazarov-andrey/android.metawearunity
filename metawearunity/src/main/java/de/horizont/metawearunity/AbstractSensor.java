package de.horizont.metawearunity;

import android.os.Handler;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;

public abstract class AbstractSensor<TSelf extends AbstractSensor<TSelf, TSensor, THandler>, TSensor extends MetaWearBoard.Module, THandler> {
    private boolean loggable;
    protected TSensor sensor;
    protected Handler handler;

    public AbstractSensor(Handler handler, MetaWearBoard board, Class<TSensor> sensorClass, boolean loggable) {
        this.sensor = board.getModule(sensorClass);
        Log.d("AbstractSensor", "sensor: " + sensor);
        this.handler = handler;
        this.loggable = loggable;
    }

    protected void Log (String message)
    {
        if (loggable)
            Log.d(GetTag(), message);
    }

    protected String GetTag ()
    {
        return this.getClass().getName();
    }

    public abstract void Start (THandler handler);
    public abstract void Stop ();
}
