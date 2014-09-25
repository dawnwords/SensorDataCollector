package cn.edu.fudan.se.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import cn.edu.fudan.se.SensorDataCollector.R;
import cn.edu.fudan.se.recorder.RecordService;
import cn.edu.fudan.se.recorder.Recorder;
import cn.edu.fudan.se.sensor.BaseSensor;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {
    private List<BaseSensor> sensors;
    private Recorder recorder;
    private ServiceConnection connection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bindRecordService();
        initSensors();
        startSensors();
    }

    @Override
    protected void onDestroy() {
        stopSensors();
        unbindService(connection);
        super.onDestroy();
    }


    private void bindRecordService() {
        Intent intent = new Intent(this, RecordService.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                recorder = (RecordService.RecordBinder) iBinder;
                Log.d("Service", "Connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                recorder = null;
                Log.d("Service", "Disconnected");
            }
        };
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void initSensors() {
        sensors = new LinkedList<BaseSensor>();
        sensors.add(new BaseSensor(this, Sensor.TYPE_GRAVITY, recorder));
    }

    private void startSensors() {
        for (BaseSensor sensor : sensors) {
            sensor.start();
        }
    }

    private void stopSensors() {
        for (BaseSensor sensor : sensors) {
            sensor.stop();
        }
    }
}
