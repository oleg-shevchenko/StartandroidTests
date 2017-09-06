package com.example.oleg.startandroidtests.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.services.L97Service;

/*
В прошлых уроках мы общались с сервисом асинхронно. Т.е. мы отправляли запрос через startService,
а ответ нам приходил когда-нибудь потом посредством PendingIntent или BroadcastReceiver.
Но есть и синхронный способ взаимодействия с сервисом. Он достигается с помощью биндинга (binding, я также буду использовать слово «подключение»).
Мы подключаемся к сервису и можем взаимодействовать с ним путем обычного вызова методов с передачей данных и получением результатов.
В этом уроке передавать данные не будем. Пока что разберемся, как подключаться и отключаться.

Как вы помните, для запуска и остановки сервиса мы использовали методы startService и stopService.
Для биндинга используются методы bindService и unbindService.*/

/*
Насколько я понял по хелпу, не рекомендуется оставлять незавершенные подключения к сервисам по окончании работы приложения.
В хелповских примерах подключение к сервису производится в методе onStart, а отключение - в onStop.
Но, разумеется, если вам надо, чтобы подключение оставалось при временном уходе Activity в background, то используйте onCreate и onDestroy.
Также есть четкая рекомендация от гугла - не использовать для этих целей onResume и onPause.
 */

public class L97ServiceBindActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    //Переменную bound мы используем для того, чтобы знать – подключены мы в данный момент к сервису или нет.
    boolean bound = false;

    ServiceConnection sConn;
    Intent intent;

    L97Service l97Service;

    Button btnDoSomething;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l97_service_bind);

        btnDoSomething = (Button) findViewById(R.id.btnDoSomething);

        //создаем Intent, который позволит нам добраться до сервиса.
        intent = new Intent("action_L97Service");
        intent.setPackage("com.example.oleg.startandroidtests");

        //Объект ServiceConnection позволит нам определить, когда мы подключились к сервису и когда связь с сервисом потеряна (если сервис был убит системой при нехватке памяти).
        //При подключении к сервису сработает метод onServiceConnected. На вход он получает имя компонента-сервиса и объект Binder для взаимодействия с сервисом.
        //В этом уроке мы этим Binder пока не пользуемся. При потере связи сработает метод onServiceDisconnected.
        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(LOG_TAG, "L97ServiceBindActivity onServiceConnected");
                //Приводим полученный биндер к нужному и получаем ссылку на сервис
                l97Service = ((L97Service.MyL97Binder) binder).getService();

                btnDoSomething.setEnabled(true);
                bound = true;
            }

            //Метод onServiceDisconnected не сработает при явном отключении. Срабатывает при убийстве сервиса системой и т.д.
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "L97ServiceBindActivity onServiceDisconnected");
                l97Service = null;
                btnDoSomething.setEnabled(false);
                bound = false;
            }
        };
    }

    //Кнопка старт сервиса
    public void onClickStart(View v) {
        startService(intent);
    }

    //Кнопка стоп сервиса
    public void onClickStop(View v) {
        //Сервис остановится только в том случае, если нет подключений, иначе будет ждать пока все отключатся и потом остановится
        stopService(intent);
    }

    //Кнопка bind
    public void onClickBind(View v) {
        //соединяемся с сервисом, используя метод bindService. На вход передаем Intent, ServiceConnection и флаг BIND_AUTO_CREATE,
        //означающий, что, если сервис, к которому мы пытаемся подключиться, не работает, то он будет запущен.
        //Если всесто флага указать 0, то мы оставим заявку на подключение, если сервис не запущен. Т.е. когда он запустится, например запустим startService(intent)
        //то произойдет подключение

        //Если мы биндингом запустили сервис, он будет жить, пока живет соединение.
        //Как только мы отключаемся, сервис останавливается (если к одному сервису много подключений, то живет до последнего)

        //Также, если сервис самоперезапускаемый, то бинд пойдет автоматически

        //При повторном бинд и анбинд к работающему сервису, если метод onUnbind сервиса возвращает false, то вызовется только метод onServiceConnected для ServiceConnection
        //если вернуть true, то будут также каждый раз вызываться onRebind и onUnbind в сервисе.

        //Обычно вызывается в методе onStart() активити
        bindService(intent, sConn, BIND_AUTO_CREATE);
    }

    //Кнопка unbind
    public void onClickUnBind(View v) {
        if (!bound) return;
        //отсоединяемся методом unbindService, на вход передавая ему ServiceConnection.
        //И в bound пишем false, т.к. мы сами разорвали соединение. Метод onServiceDisconnected не сработает при явном отключении.
        //Как только мы отключаемся, сервис останавливается, но только если мы его запустили методом bindService и подключение одно. Т.е. если сервис запущен методом startService
        //то произойдет только отключение.

        //Обычно вызывается в методе onStop() активити
        unbindService(sConn);

        //Поскольку при явном unbind не вызывается onServiceDisconnected, опишем те же действия
        l97Service = null;
        btnDoSomething.setEnabled(false);
        bound = false;
    }

    //Запуск метода в сервисе (если это один процесс!!!)
    public void onClickDoSomething(View v) {
        if(l97Service != null) {
            //Вызываем какой-то метод в сервисе
            l97Service.doSomething();
        }
    }

    //В onDestroy делаем unbind
    protected void onDestroy() {
        super.onDestroy();
        //отключаемся
        onClickUnBind(null);
        //Убираем ссылку на сервис
        l97Service = null;
    }
}
