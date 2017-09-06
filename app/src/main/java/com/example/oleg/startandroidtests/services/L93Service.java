package com.example.oleg.startandroidtests.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//***Урок 93 Передача данных в сервис. Методы остановки сервиса***//
public class L93Service extends Service {

    final String LOG_TAG = "myLogs";

    //Екзекьютор
    ExecutorService es;

    //Какой-то ресурс, для примера его будем обнулять в onDestroy(). Будет видно, что сущетвует вероятность вызова onDestroy(), когда не все потоки отработали
    Object someRes;

    //Конструктор
    public L93Service() {}

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "L93Service onCreate");

        //эта строка дает нам объект (я буду называть его - экзекьютор), который будет получать от нас задачи (Runnable)
        //и запускать их по очереди в одном потоке (на вход ему мы передаем значение 1 - колл одновременных потоков) или вместе, если передадим 3
        //Он сделает за нас всю работу по управлению потоками.
        es = Executors.newFixedThreadPool(3);
        someRes = new Object();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "L93Service onStartCommand");
        //читаем из intent параметр time, который мы передали из активити при запуске
        int time = intent.getIntExtra("time", 1);
        //Создаем Runnable-объект MyRun, передаем ему time и startId и отдаем этот объект экзекьютору, который его запустит в отдельном потоке.
        MyRun mr = new MyRun(time, startId);
        es.execute(mr);
        return super.onStartCommand(intent, flags, startId);
    }

    //В onDestroy обнуляем someRes
    //Этот пример описывает ситуацию, козда мы три раза вызываем startService(). Если указать Executors.newFixedThreadPool(3), то наши три Runnable
    //будут выполняться одновременно. Поскольку первому мы передаем время выполнения 7 сек, а третьему 4, то когда выполнится последний stopSelf(startId=3),
    //т.е. третий со startId=3 через 4 секунды, то вызовется onDestroy(), при этом первый еще продолжит работу, в то время как мы уже освободили расурс someRes
    //в таком случае получим NullPointerException. Т.е. получилась неприятная ситуация, когда мы в onDestroy освободили объект, а он был еще нужен.
    //Здесь надо быть аккуратным, понимать механизм работы stopSelf(startId) и, когда именно этот метод остановит сервис.
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "L93Service onDestroy");
        someRes = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*MyRun – Runnable-объект. Он и будет обрабатывать входящие вызовы сервиса. В конструкторе он получает time и startId.
    Параметр time будет использован для кол-ва секунд паузы (т.е. эмуляции работы).
    А startId будет использован в методе stopSelf(startId), который даст сервису понять, что вызов под номером strartId обработан.
    В лог выводим инфу о создании, старте и завершении работы. Также здесь используем объект someRes, в лог просто выводим его класс.
    Если же объект = null, то ловим эту ошибку и выводим ее в лог.*/
    class MyRun implements Runnable {

        int time;
        int startId;

        public MyRun(int time, int startId) {
            this.time = time;
            this.startId = startId;
            Log.d(LOG_TAG, "MyRun#" + startId + " create");
        }

        public void run() {
            Log.d(LOG_TAG, "MyRun#" + startId + " start, time = " + time);
            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Log.d(LOG_TAG, "MyRun#" + startId + " someRes = " + someRes.getClass() );
            } catch (NullPointerException e) {
                Log.d(LOG_TAG, "MyRun#" + startId + " error, null pointer");
            }
            stop();
        }

        void stop() {
            //Этот метод дает системе понять, что конкретный вызов сервиса был успешно обработан (startId поступает на вход метода onStartCommand())
            //Сервис останавливается, когда последний ПОЛУЧЕНЫЙ (а не последний обработанный) вызов выполняет метод  stopSelf(startId).
            //А при этом могут продолжать работать ранее полученные вызовы. Почему так сделано – я не знаю.
            //stopSelf(startId);

            //Метод stopSelfResult(startId) аналогичен методу stopSelf(startId), но при этом еще возвращает boolean значение – остановил он сервис или нет.
            boolean stopped = stopSelfResult(startId);
            Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelfResult(" + startId + ") = " + stopped);
        }
    }
}
