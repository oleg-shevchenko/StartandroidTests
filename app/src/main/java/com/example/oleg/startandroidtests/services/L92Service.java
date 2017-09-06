package com.example.oleg.startandroidtests.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

//Итак, сервис – это некая задача, которая работает в фоне и не использует UI.
//Запускать и останавливать сервис можно из приложений и других сервисов. Также можно подключиться к уже работающему сервису и взаимодействовать с ним.

/* https://developer.android.com/guide/topics/manifest/service-element.html
<service android:description="string resource"
         android:directBootAware=["true" | "false"]
         android:enabled=["true" | "false"]
         android:exported=["true" | "false"]
         android:icon="drawable resource"
         android:isolatedProcess=["true" | "false"]
         android:label="string resource"
         android:name="string"
         android:permission="string"
         android:process="string" >
    . . .
</service>
*/

//https://developer.android.com/guide/components/services.html?hl=ru

public class L92Service extends Service {

    final String LOG_TAG = "myLogs";

    public L92Service() {
    }

    //Срабатывает при создании сервиса (напр. когда первый раз вызываем startService())
    //Система вызывает этот метод, когда другой компонент, например, операция, запрашивает запуск этой службы, вызывая startService()
    //После выполнения этого метода служба запускается и может в течение неограниченного времени работать в фоновом режиме.
    //Если вы реализуете такой метод, вы обязаны остановить службу посредством вызова stopSelf() или stopService().
    //(Если требуется только обеспечить привязку, реализовывать этот метод не обязательно).
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    //срабатывает, когда сервис запущен методом startService
    //Параметры:
    // Первый – это Intent.  Тот самый, который отправляется в путь, когда мы стартуем сервис с помощью метода startService.
    //      Соответственно вы можете использовать его для передачи данных в ваш сервис.
    // Второй параметр – флаги запуска.
    // Третий параметр – startId. Простыми словами – это счетчик вызовов startService пока сервис запущен.
    //      Т.е. вы запустили сервис методом startService, сработал метод onStartCommand и получил на вход startId = 1.
    //      Вызываем еще раз метод startService, сработал метод onStartCommand и получил на вход startId = 2. И так далее.
    //      Счетчик сбрасывается, когда сервис будет остановлен методами stopService, stopSelf и пр. После этого вызовы снова идут с единицы.

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        someTask(startId);
        return super.onStartCommand(intent, flags, startId);
    }

    //Срабатывает при уничтожении сервиса (напр. при остановке методом stopService())
    //Система вызывает этот метод, когда служба более не используется и выполняется ее уничтожение.
    //Ваша служба должна реализовать это для очистки ресурсов, таких как потоки, зарегистрированные приемники, ресиверы и т. д.
    //Это последний вызов, который получает служба.
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    void someTask(final int startId) {
        //Если в ДАННОМ случае не вынести в отдельный поток, то UI подвиснет, т.к. сейчас сервис выполняется в основном потоке
        //Можно также вынести в отдельный процесс в манифесте android:process=":my_l92_service_process"
        new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    Log.d(LOG_TAG, "i = " + i);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //Сервис останавливает сам себя
                stopSelf();

                //Этот метод дает системе понять, что конкретный вызов сервиса был успешно обработан (startId поступает на вход метода onStartCommand())
                //Сервис останавливается, когда последний полученный (а не последний обработанный) вызов выполняет метод  stopSelf(startId).
                //А при этом могут продолжать работать ранее полученные вызовы. Почему так сделано – я не знаю.
                //stopSelf(startId);

                //Метод stopSelfResult(startId) аналогичен методу stopSelf(startId), но при этом еще возвращает boolean значение – остановил он сервис или нет.
                //boolean stopped = stopSelfResult(startId);
            }
        }).start();
    }
}
