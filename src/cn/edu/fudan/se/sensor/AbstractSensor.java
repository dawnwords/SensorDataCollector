package cn.edu.fudan.se.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import cn.edu.fudan.se.recorder.Recorder;
import cn.edu.fudan.se.recorder.Tuple;

/**
 * Created by Dawnwords on 2014/9/24.
 */
public abstract class AbstractSensor implements SensorEventListener {
    private Recorder recorder;
    private Context context;

    protected AbstractSensor(Context context, Recorder recorder) {
        this.recorder = recorder;
        this.context = context;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (recorder != null) {
            //TODO need to check length of sensorEvent.values?
            //TODO check if sensor name can be a table name
            recorder.record(new Tuple(getTupleTag(), sensorEvent.values));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    protected Object getSystemService(String serviceName) {
        return context.getSystemService(serviceName);
    }

    protected abstract String getTupleTag();

    public abstract void start();

    public abstract void stop();
}