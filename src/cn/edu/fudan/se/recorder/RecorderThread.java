package cn.edu.fudan.se.recorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Dawnwords on 2014/9/21.
 */
public class RecorderThread extends Thread {
    private static final String DB_PATH = "sensor.db";

    private BlockingQueue<Tuple> tupleQueue;
    private ConcurrentSkipListSet<String> tableCreated;
    private SQLiteDatabase db;

    public RecorderThread(Context context) {
        tupleQueue = new LinkedBlockingQueue<Tuple>();
        tableCreated = new ConcurrentSkipListSet<String>();
        db = context.openOrCreateDatabase(DB_PATH, Context.MODE_PRIVATE, null);
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                saveToDB(tupleQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        db.close();
    }

    public void put(Tuple tuple) {
        try {
            tupleQueue.put(tuple);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveToDB(Tuple tuple) {
        if (!tableCreated.contains(tuple.name)) {
            tableCreated.add(tuple.name);
            createTable(tuple.name, tuple.values.length);
        }
        insertTable(tuple);
    }

    private void insertTable(Tuple tuple) {
        String sql = "insert into " + tuple.name + " values (datetime('now'),";
        for (float value : tuple.values) {
            sql += value + ",";
        }
        executeSQL(sql);
    }

    private void createTable(String tableName, int valueNum) {
        String sql = "create table if not exists " + tableName + "(time TIMESTAMP,";
        for (int i = 0; i < valueNum; i++) {
            sql += "value" + i + " float,";
        }
        executeSQL(sql);
    }

    private void executeSQL(String sql) {
        sql = sql.substring(0, sql.length() - 1) + ")";
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}