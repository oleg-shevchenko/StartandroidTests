package com.example.oleg.startandroidtests.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

//При подключении нескольких приложений или сервисов к одному сервису, а такая возможность существует,
//то в этом случае, если сервис запущен биндингом, то он будет жить, пока не отключатся все подключившиеся.

public class L97Service extends Service {

    final String LOG_TAG = "myLogs";

    MyL97Binder binder = new MyL97Binder();

    public L97Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "L97Service onCreate");
    }

    //Срабатывает при подключении к сервису
    //В методе onBind возвращаем биндер, который мы реализовали ниже.
    /*Система вызывает этот метод, когда другой компонент хочет выполнить привязку к службе
    (например, для выполнения удаленного вызова процедуры) путем вызова bindService().
    В вашей реализации этого метода вы должны обеспечить интерфейс, который клиенты используют для взаимодействия со службой, возвращая IBinder.
    Всегда необходимо реализовывать этот метод, но если вы не хотите разрешать привязку, необходимо возвращать значение null.*/
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "L97Service onBind");
        return binder;
    }

    //Срабатывает, козда один обьект ServiceConnection, переподключается к сервису, после отключения. При этом onUnbind должен возвращать true, иначе при переподключении
    //будет срабатывать только onServiceConnected у обьекта ServiceConnection.
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(LOG_TAG, "L97Service onRebind");
    }

    //При отключении, по умолчанию возвращает false. Это возможность ребинда.
    //При повторном бинд и анбинд к работающему сервису, если метод onUnbind возвращает false,
    //то методы сервиса onBind и onUnbind уже не вызываются, только срабатывает метод onServiceConnected для ServiceConnection
    //если вернуть true, то будет срабатывать onRebind и onUnbind
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG, "L97Service onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "L97Service onDestroy");
    }

    public void doSomething() {
        Log.d(LOG_TAG, "L97Service doSomething");
    }

    //В биндере опишем метод, который будет возврвщать ссылку на сервис
    //далее в активити сможем вызывать методы нашего сервиса
    //!!!Такое работает только если сервис и активити работают в одном процессе
    public class MyL97Binder extends Binder {
        //Здесь просто вернем ссылку на службу
        public L97Service getService() {
            return L97Service.this;
        }
    }
}
