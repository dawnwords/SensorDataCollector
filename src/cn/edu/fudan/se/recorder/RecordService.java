package cn.edu.fudan.se.recorder;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Dawnwords on 2014/9/20.
 */
public class RecordService extends Service {

    private RecorderThread recorderThread;

    @Override
    public IBinder onBind(Intent intent) {
        recorderThread = new RecorderThread(getBaseContext());
        recorderThread.start();
        return new RecordBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        recorderThread.interrupt();
        return super.onUnbind(intent);
    }


    public class RecordBinder extends Binder implements Recorder {
        @Override
        public void record(Tuple tuple) {
            recorderThread.record(tuple);
        }
    }
}
