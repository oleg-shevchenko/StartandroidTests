package com.example.oleg.startandroidtests.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.oleg.startandroidtests.view.L96ServiceBroadcastReciverActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class L96Service extends Service {

    final String LOG_TAG = "myLogs";
    ExecutorService es;

    public L96Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService onCreate");
        es = Executors.newFixedThreadPool(2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MyService onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService onStartCommand");

        //Получаем из интента параметры запуска
        int time = intent.getIntExtra(L96ServiceBroadcastReciverActivity.PARAM_TIME, 1);
        int task = intent.getIntExtra(L96ServiceBroadcastReciverActivity.PARAM_TASK_CODE, 0);

        MyRun mr = new MyRun(startId, time, task);
        es.execute(mr);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyRun implements Runnable {

        int time;
        int startId;
        int task;

        public MyRun(int startId, int time, int task) {
            this.time = time;
            this.startId = startId;
            this.task = task;
            Log.d(LOG_TAG, "MyRun#" + startId + " create");
        }

        public void run() {
            Intent intent = new Intent(L96ServiceBroadcastReciverActivity.BROADCAST_ACTION);
            Log.d(LOG_TAG, "MyRun#" + startId + " start, time = " + time);
            try {
                // отправляем интент с сообщением о старте задачи
                intent.putExtra(L96ServiceBroadcastReciverActivity.PARAM_TASK_CODE, task);
                intent.putExtra(L96ServiceBroadcastReciverActivity.PARAM_STATUS, L96ServiceBroadcastReciverActivity.STATUS_START);
                sendBroadcast(intent);

                // начинаем выполнение задачи
                TimeUnit.SECONDS.sleep(time);

                // сообщаем об окончании задачи
                intent.putExtra(L96ServiceBroadcastReciverActivity.PARAM_STATUS, L96ServiceBroadcastReciverActivity.STATUS_FINISH);
                intent.putExtra(L96ServiceBroadcastReciverActivity.PARAM_RESULT, time * 100);
                sendBroadcast(intent);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop();
        }

        void stop() {
            Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelfResult(" + startId + ") = " + stopSelfResult(startId));
        }
    }
}
