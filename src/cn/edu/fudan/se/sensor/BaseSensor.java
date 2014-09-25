package cn.edu.fudan.se.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import cn.edu.fudan.se.recorder.Recorder;

/**
 * Created by Dawnwords on 2014/9/19.
 */
public class BaseSensor extends AbstractSensor {
    private Sensor sensor;
    private SensorManager manager;

    public BaseSensor(Context context, int sensorType, Recorder recorder) {
        super(context, recorder);
        this.manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.sensor = manager.getDefaultSensor(sensorType);
    }

    @Override
    protected String getTupleTag() {
        return sensor.getName();
    }

    public void start() {
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        manager.unregisterListener(this);
    }

}
