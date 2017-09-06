package com.example.oleg.startandroidtests.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.services.L100IntentService;
import com.example.oleg.startandroidtests.services.L92Service;
import com.example.oleg.startandroidtests.services.L93Service;
import com.example.oleg.startandroidtests.services.L94Service;

//https://developer.android.com/guide/components/services.html?hl=ru

public class L92ServiceActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    //Система вызывает этот метод при первом создании службы для выполнения однократных процедур настройки
    //(перед вызовом onStartCommand() или onBind()). Если служба уже запущена, этот метод не вызывается.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l92_service);
    }

    //**Урок 92 **//
    public void onClickStart(View v) {
        //Запуск сервиса
        //Если компонент запускает службу посредством вызова startService() (что приводит к вызову onStartCommand()),
        //то служба продолжает работу, пока она не остановится самостоятельно с помощью stopSelf() или другой компонент не остановит ее посредством вызова stopService().
        startService(new Intent(this, L92Service.class));
    }

    public void onClickStop(View v) {
        //Остановка сервиса
        stopService(new Intent(this, L92Service.class));
    }

    //***Урок 93 Передача данных в сервис. Методы остановки сервиса***//
    public void onClickStart1(View v) {
        //По нажатию на кнопку мы отправляем вызов в сервис три раза. И в intent помещаем параметр time.
        //Соответственно в сервисе будет три раза выполнен метод onStartCommand()
        startService(new Intent(this, L93Service.class).putExtra("time", 7));
        startService(new Intent(this, L93Service.class).putExtra("time", 2));
        startService(new Intent(this, L93Service.class).putExtra("time", 4));
    }

    //***Урок 94 Подробно про onStartCommand***//
    public void onClickStart2(View v) {
        //Просто вызов сервиса. Вспоминаем способы вызова Activity, которые мы знаем, и понимаем, что здесь идет аналогичный вызов сервиса через Action.
        //А значит, когда будем создавать сервис, надо будет настроить Intent Filter с этим Action.
        //На прошлых уроках мы сервис вызывали через класс, т.к. там приложение и сервис в одном Application у нас были.
        //!!!Для того, чтобы интент нашел сервис, action не должен совпадать с путем файла сервиса,
        //т.е. если сейчас указать com.example.oleg.startandroidtests.services.L94Service вместо action_L94Service , то будут проблемы
        Intent intent = new Intent("action_L94Service");
        intent.putExtra("name", "value");
        //Если не указать пакет, то будет java.lang.IllegalArgumentException: Service Intent must be explicit: Intent { act=com.example.oleg.startandroidtests.services.L94Service (has extras)
        intent.setPackage("com.example.oleg.startandroidtests");
        startService(intent);       //такой вызов сервиса не рекомендуется, если он часть нашей программы
    }

    //***Урок 100 IntentService. Отправляем на обработку несколько заданий***//
    public void onClickStart3(View v) {
        Intent intent = new Intent(L92ServiceActivity.this, L100IntentService.class);
        //3*4=12
        intent.setAction(L100IntentService.ACTION_MULTIPLY);
        intent.putExtra(L100IntentService.EXTRA_PARAM1, 3);
        intent.putExtra(L100IntentService.EXTRA_PARAM2, 4);
        startService(intent);
    }

    public void onClickStart4(View v) {
        Intent intent = new Intent(L92ServiceActivity.this, L100IntentService.class);
        //3+4=7
        intent.setAction(L100IntentService.ACTION_ADD);
        intent.putExtra(L100IntentService.EXTRA_PARAM1, 3);
        intent.putExtra(L100IntentService.EXTRA_PARAM2, 4);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Если покидаем активити, то останавливаем сервис
        //Здесь идет проверка isFinishing(), т.е. покидаем активити или просто перерисовываем (напр. при повороте вернет false)
        if(isFinishing()) {
            stopService(new Intent(this, L92Service.class));
            stopService(new Intent(this, L93Service.class));
            stopService(new Intent(this, L94Service.class));
        }
    }
}
