package cn.edu.fudan.se.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import cn.edu.fudan.se.recorder.Recorder;
import cn.edu.fudan.se.recorder.Tuple;

/**
 * Created by Dawnwords on 2014/9/19.
 */
public class BaseSensor implements SensorEventListener {
    private Sensor sensor;
    private SensorManager manager;
    private Recorder recorder;

    public BaseSensor(SensorManager manager, int sensorType, Recorder recorder) {
        this.manager = manager;
        this.sensor = manager.getDefaultSensor(sensorType);
        this.recorder = recorder;
    }

    public void start() {
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (recorder != null) {
            //TODO need to check length of sensorEvent.values?
            //TODO check if sensor name can be a table name
            recorder.record(new Tuple(sensor.getName(), sensorEvent.values));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
