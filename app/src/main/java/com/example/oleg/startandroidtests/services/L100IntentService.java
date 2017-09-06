package com.example.oleg.startandroidtests.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.oleg.startandroidtests.R;

import java.util.concurrent.TimeUnit;

/*Это подкласс обычного Service. Он используется, если вам в сервисе надо выполнять какие-то тяжелые задачи, и вы не хотите сами возиться с асинхронностью.
Принцип работы этого вида сервиса прост. Он создает новый поток для своей работы.
Затем берет все Intent пришедшие ему в onStartCommand и отправляет их на обработку в этот поток.
Как именно обрабатываются Intent – зависит от нас, т.к. мы сами кодим это в методе onHandleIntent.

Т.е. приложение сыпет в сервис вызовами startService, в которых передает Intent-ы.
IntentService принимает эти вызовы в onStartCommand, берет Intent-ы и отправляет их в очередь на обработку.
И далее они поочередно обрабатываются в отдельном потоке методом onHandleIntent.
Когда последний Intent из очереди обработан, сервис сам завершает свою работу.*/

public class L100IntentService extends IntentService {

    final String LOG_TAG = "myLogs";

    public static final String ACTION_MULTIPLY = "com.example.oleg.startandroidtests.services.action.MULTIPLY";
    public static final String ACTION_ADD = "com.example.oleg.startandroidtests.services.action.ADD";

    public static final String EXTRA_PARAM1 = "com.example.oleg.startandroidtests.services.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.oleg.startandroidtests.services.extra.PARAM2";

    public L100IntentService() {
        super("L100IntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "L100IntentService onCreate()");
    }

    //Этот метод срабатывает всегда при вызове startService(), он ложит входящие интенты в очередь и для каждого по порядку вызывается
    //onHandleIntent(Intent intent), т.е. работает именно очередь
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "L100IntentService onStartCommand()");

        //Вы можете сказать системе, что ваш сервис очень важен для пользователя и его нельзя грохать при нехватке памяти.
        //Это актуально, например, для музыкального плеера. В статус-бар при этом будет помещено уведомление.
        startForeground(1, getForegroundNotification());

        return super.onStartCommand(intent, flags, startId);
    }

    //Метод вызывается для каждого входящего интента по порядку из очереди, когда обработается последний, то сервис остановится (onDestroy)
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "L100IntentService onHandleIntent()");
        if (intent != null) {
            final String action = intent.getAction();
            //Здесь определяем какой action, и исходя из этого делаем нужные действия
            if (ACTION_MULTIPLY.equals(action)) {
                final int param1 = intent.getIntExtra(EXTRA_PARAM1, 0);
                final int param2 = intent.getIntExtra(EXTRA_PARAM2, 0);
                handleActionMultiply(param1, param2);
            } else if (ACTION_ADD.equals(action)) {
                final int param1 = intent.getIntExtra(EXTRA_PARAM1, 0);
                final int param2 = intent.getIntExtra(EXTRA_PARAM2, 0);
                handleActionAdd(param1, param2);
            }
        }
    }

    //Обрабатываем ACTION_MULTIPLY
    private void handleActionMultiply(int param1, int param2) {
        Log.d(LOG_TAG, "L100IntentService handleActionMultiply()");
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "L100IntentService. Multiply result: " + param1 + " * " + param2 + " = " + (param1*param2));
    }

    //Обрабатываем ACTION_ADD
    private void handleActionAdd(int param1, int param2) {
        Log.d(LOG_TAG, "L100IntentService handleActionAdd()");
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "L100IntentService. Add result: " + param1 + " + " + param2 + " = " + (param1+param2));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        Log.d(LOG_TAG, "L100IntentService onDestroy()");
    }

    //Для примера cделаем для сервиса режим FOREGROUND, для которого нужен Notification
    private Notification getForegroundNotification() {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_sync_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.google_drive_icon))
                .setTicker("L100IntentService: выполняется обработка")
                .setContentTitle("L100IntentService")
                .setContentText("Выполняется обработка...")
                .build();
    }
}
