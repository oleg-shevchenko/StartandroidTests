package com.example.oleg.startandroidtests.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.oleg.startandroidtests.view.L95ServicePendingIntentActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class L95Service extends Service {

    final String LOG_TAG = "myLogs";
    ExecutorService es;

    public L95Service() {
    }

    //При создании сервиса
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "L95Service onCreate");
        //Создаем екзекьютор с возможностью одновременного выполнения двух Runnable, в него будем подавать наши Runnable
        es = Executors.newFixedThreadPool(2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "L95Service onDestroy");
    }

    //При запуске выполнения
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "L95Service onStartCommand");
        //Получаем время из интента
        int time = intent.getIntExtra(L95ServicePendingIntentActivity.PARAM_TIME, 1);
        //Получаем PendingIntent, который мы создали и поместили в intent в активити
        PendingIntent pi = intent.getParcelableExtra(L95ServicePendingIntentActivity.PARAM_PINTENT);
        //Запускаем выполнение Runnable с получеными данными
        es.execute(new MyRun(time, startId, pi));

        //Возврат значения флага для перезапуска (по умолчанию START_STICKY)
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyRun implements Runnable {

        int time;
        int startId;
        PendingIntent pi;

        //Конструктор задачи
        public MyRun(int time, int startId, PendingIntent pi) {
            this.time = time;
            this.startId = startId;
            this.pi = pi;
            Log.d(LOG_TAG, "MyRun#" + startId + " create");
        }

        //Запуск выполнения
        public void run() {
            Log.d(LOG_TAG, "MyRun#" + startId + " start, time = " + time);
            try {
                //сообщаем об старте задачи, т.е. у присланого нам PendingIntent вызываем метод send(), в качестве параметра указываем код статуса
                //в активити сработает метод onActivityResult(int requestCode, int resultCode, Intent data), где мы получим
                //requestCode - который мы определили при создании PendingIntent
                //resultCode - код статуса, который мы сейчас отправляем
                pi.send(L95ServicePendingIntentActivity.STATUS_START);

                //имитируем долгое выполнение задачи
                TimeUnit.SECONDS.sleep(time);

                //сообщаем об окончании задачи, в этот раз, кроме кода статуса, создаем и отправляем интент с результатом работы
                //в активити понимаем по статус коду, что выполнение завершено и должен быть прислан интент с результатами, получаем его и извлекаем результаты
                Intent intent = new Intent().putExtra(L95ServicePendingIntentActivity.PARAM_RESULT, time * 100);
                pi.send(L95Service.this, L95ServicePendingIntentActivity.STATUS_FINISH, intent);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            stop();
        }

        //Пытаемся остановить сервис
        void stop() {
            Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelfResult(" + startId + ") = " + stopSelfResult(startId));
        }
    }
}
