package com.example.oleg.startandroidtests.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

//***Урок 94 Подробно про onStartCommand***//
//Метод onStartCommand должен возвращать int. В прошлых примерах мы использовали ответ метода супер-класса.
//В этом уроке разберемся, что мы можем возвращать, и чем нам это грозит.
//Также посмотрим, что за флаги (второй параметр) идут на вход этому методу.

//Для этого сервиса в манифесте прописано android:process=":my_l94_service_process", поэтому сервис будет запускаться в отдельном процессе
//Для того, чтобы вручную можно было убить процесс, открываем Tools->Android->Android Device Monitor

public class L94Service extends Service {

    final String LOG_TAG = "myLogs";

    public L94Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "L94Service onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "L94Service onDestroy");
    }

    //Метод onStartCommand должен возвращать int. В прошлых примерах мы использовали ответ метода супер-класса.
    /*Система может убить сервис, если ей будет не хватать памяти. Но в наших силах сделать так, чтобы наш сервис ожил, когда проблема с памятью будет устранена.
    И более того, не просто ожил, а еще и снова начал выполнять незавершенные вызовы startService.
    Т.е. мы вызываем startService, срабатывает onStartCommand и возвращает одну из следующих констант:
        START_NOT_STICKY – сервис не будет перезапущен после того, как был убит системой.
        START_STICKY – сервис будет перезапущен после того, как был убит системой. Если нет новых интентов, то сервис запустится с интентом null.
            Т.е. данный флаг не сохраняет предыдущие интенты, но сервис перезапустится с null или новым интентом.
        START_REDELIVER_INTENT – сервис будет перезапущен после того, как был убит системой. Кроме этого,
            сервис снова получит все вызовы startService, которые не были завершены методом stopSelf(startId).
            Т.е. все прерваные onStartCommand() перезапустятся со своими старыми интентами*/

    //Второй параметр flags метода onStartCommand дает нам понять, что это повторная попытка вызова onStartCommand.
    //В хелпе написано, что flags может принимать значения 0, START_FLAG_RETRY или START_FLAG_REDELIVERY.
    //0 - приходит, по умолчанию и когда START_STICKY? START_FLAG_RETRY - непонятно когда, непонятно почему не приходит когда START_STICKY? (API25 & API17)
    //А вот на флаг START_FLAG_REDELIVERY можно положиться. Если он пришел вам в методе onStartCommand, значит прошлый вызов
    //этого метода вернул START_REDELIVER_INTENT, но не был завершен успешно методом stopSelf(startId).
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "L94Service onStartCommand. Intent: " + (intent == null ? "null" : intent.getStringExtra("name")));
        readFlags(flags);
        new Thread(new MyRun(startId)).start();
        return START_REDELIVER_INTENT;
    }

    private void readFlags(int flags) {
        if (flags == START_FLAG_REDELIVERY)
            Log.d(LOG_TAG, "START_FLAG_REDELIVERY");
        if (flags == START_FLAG_RETRY)
            Log.d(LOG_TAG, "START_FLAG_RETRY");
        if (flags == 0)
            Log.d(LOG_TAG, "START_FLAG_0");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //MyRun – Runnable класс, который эмулирует долгую работу. Ставим в нем паузу 15 секунд. После этого вызываем stopSelfResult.
    class MyRun implements Runnable {

        int startId;

        public MyRun(int startId) {
            this.startId = startId;
            Log.d(LOG_TAG, "MyRun#" + startId + " create");
        }

        public void run() {
            Log.d(LOG_TAG, "MyRun#" + startId + " start");
            try {
                TimeUnit.SECONDS.sleep(15);
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
