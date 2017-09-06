package com.example.oleg.startandroidtests.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.services.L96Service;

/*В этом уроке:
- получаем из сервиса результат с помощью BroadcastReceiver

В прошлом уроке мы использовали PendingIntent для получения обратной связи от сервиса. В этом уроке для этой же цели используем BroadcastReceiver.
Схема такая:
- в Activity создаем BroadcastReceiver, а также создаем IntentFilter, настроенный на определенный Action,
    и регистрируем (включаем) эту пару. Теперь BroadcastReceiver будет получать Intent-ы подходящие под условия IntentFilter
- в сервисе, когда нам понадобится передать данные в Activity, мы создаем Intent (с Action из предыдущего пункта),
    кладем в него данные, которые хотим передать, и посылаем его на поиски BroadcastReceiver
- BroadcastReceiver в Activity ловит этот Intent и извлекает из него данные

Т.е. тут все аналогично вызовам Activity с использованием Action и IntentFilter.
Если Action в Intent (отправленном из сервиса) и в IntentFilter (у BroadcastReceiver в Activity) совпадут,
то BroadcastReceiver получит этот Intent и сможет извлечь данные для Activity.
Пример сделаем полностью аналогичный прошлому уроку. У нас будет приложение, которое будет отправлять в сервис на выполнение три задачи.
А сервис будет информировать, когда он начал каждую задачу выполнять, когда закончил и с каким результатом. Все это будем выводить на экран Activity.*/

public class L96ServiceBroadcastReciverActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    //Коды задач, для того, чтобы определять от какой задачи пришел ответ
    final int TASK1_REQUEST_CODE = 1;
    final int TASK2_REQUEST_CODE = 2;
    final int TASK3_REQUEST_CODE = 3;

    //Статусы выполнения, которые будем присылать из сервиса
    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    //Название extras, которые будем ложить в интент
    public final static String PARAM_TIME = "time";
    public final static String PARAM_TASK_CODE = "task";
    public final static String PARAM_RESULT = "result";
    public final static String PARAM_STATUS = "status";

    TextView tvTask1;
    TextView tvTask2;
    TextView tvTask3;
    BroadcastReceiver br;

    //Action для интент-фильтра
    public final static String BROADCAST_ACTION = "com.example.oleg.startanstoidtests.l96servicebackbroadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l96_service_broadcast_reciver);
        tvTask1 = (TextView) findViewById(R.id.tvTask1);
        tvTask1.setText("Task1");
        tvTask2 = (TextView) findViewById(R.id.tvTask2);
        tvTask2.setText("Task2");
        tvTask3 = (TextView) findViewById(R.id.tvTask3);
        tvTask3.setText("Task3");

        initBroadcastReceiver();
    }

    private void initBroadcastReceiver() {
        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            @Override
            public void onReceive(Context context, Intent intent) {
                //Получаем данные из Intent
                int task = intent.getIntExtra(PARAM_TASK_CODE, 0);
                int status = intent.getIntExtra(PARAM_STATUS, 0);
                Log.d(LOG_TAG, "onReceive: task = " + task + ", status = " + status);

                // Ловим сообщения о старте задач
                if (status  == STATUS_START) {
                    switch (task) {
                        case TASK1_REQUEST_CODE:
                            tvTask1.setText("Task1 start");
                            break;
                        case TASK2_REQUEST_CODE:
                            tvTask2.setText("Task2 start");
                            break;
                        case TASK3_REQUEST_CODE:
                            tvTask3.setText("Task3 start");
                            break;
                    }
                }

                // Ловим сообщения об окончании задач
                if (status == STATUS_FINISH) {
                    int result = intent.getIntExtra(PARAM_RESULT, 0);
                    switch (task) {
                        case TASK1_REQUEST_CODE:
                            tvTask1.setText("Task1 finish, result = " + result);
                            break;
                        case TASK2_REQUEST_CODE:
                            tvTask2.setText("Task2 finish, result = " + result);
                            break;
                        case TASK3_REQUEST_CODE:
                            tvTask3.setText("Task3 finish, result = " + result);
                            break;
                    }
                }
            }
        };

        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver, теперь BroadcastReceiver включен и ждет подходящих Intent.
        registerReceiver(br, intFilt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Обязательно дерегистрируем (выключаем) BroadcastReceiver при уничтожении активити
        unregisterReceiver(br);
    }

    //Обработка кнопки СТАРТ
    public void onClickStart(View v) {
        Intent intent;

        // Создаем Intent для вызова сервиса, кладем туда параметр времени и код задачи
        intent = new Intent(this, L96Service.class).putExtra(PARAM_TIME, 7).putExtra(PARAM_TASK_CODE, TASK1_REQUEST_CODE);
        // стартуем сервис
        startService(intent);

        intent = new Intent(this, L96Service.class).putExtra(PARAM_TIME, 4).putExtra(PARAM_TASK_CODE, TASK2_REQUEST_CODE);
        startService(intent);

        intent = new Intent(this, L96Service.class).putExtra(PARAM_TIME, 6).putExtra(PARAM_TASK_CODE, TASK3_REQUEST_CODE);
        startService(intent);
    }
}
