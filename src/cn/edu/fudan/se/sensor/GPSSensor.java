package cn.edu.fudan.se.sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import cn.edu.fudan.se.recorder.Recorder;

import java.lang.reflect.Field;

/**
 * Created by Dawnwords on 2014/9/24.
 */
public class GPSSensor extends AbstractSensor {

    private LocationManager manager;
    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            float longitude = (float) location.getLongitude();
            float latitude = (float) location.getLatitude();
            int accuracy = SensorManager.SENSOR_STATUS_ACCURACY_HIGH;
            long timestamp = System.currentTimeMillis();
            try {
                SensorEvent event = SensorEvent.class.newInstance();
                forceSet(event, "values", new float[]{longitude, latitude});
                forceSet(event, "accuracy", accuracy);
                forceSet(event, "timestamp", timestamp);

                onSensorChanged(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }

        private void forceSet(SensorEvent event, String fieldName, Object value) throws Exception {
            Field values = SensorEvent.class.getDeclaredField(fieldName);
            values.setAccessible(true);
            values.set(event, value);
        }

    };

    public GPSSensor(Context context, Recorder recorder) {
        super(context, recorder);
        this.manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected String getTupleTag() {
        //TODO check other sensors' names
        return null;
    }

    @Override
    public void start() {
        manager.requestLocationUpdates(getBestProvider(), 1000, 1, listener);
    }

    @Override
    public void stop() {
        manager.removeUpdates(listener);
    }


    private String getBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return manager.getBestProvider(criteria, true);
    }
}
