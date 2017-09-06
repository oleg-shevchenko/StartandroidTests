package com.example.oleg.startandroidtests.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.services.L95Service;

/*
В прошлых уроках мы стартовали сервисы, передавали им данные, но ничего не получали обратно в вызывающее Activity.
Но это возможно, и существует несколько способов. В этом уроке рассмотрим PendingIntent.
Я особо не буду вдаваться в объяснение, что такое PendingIntent и что он умеет - урок не о нем.
Я покажу, как с его помощью можно в Activity получать результаты работы сервиса.

Схема такая:
- в Activity создаем PendingIntent с помощью метода createPendingResult
- кладем этот PendingIntent в обычный Intent, который используем для старта сервиса и вызываем startService
- в сервисе извлекаем PendingIntent из полученного в методе onStartCommand объекта Intent
- когда нам необходимо передать результаты работы из сервиса в Activity, вызываем метод send для объекта PendingIntent
- эти результаты из сервиса ловим в Activity в методе onActivityResult

Т.е. фишка PendingIntent здесь в том, что он содержит некую связь с Activity (в котором он был создан) и,
когда вызывается метод send, он идет в это Activity и несет данные, если необходимо.
В общем, этакий почтовый голубь, который точно знает, как ему вернуться домой.

У нас будет приложение, которое будет отправлять в сервис на выполнение три задачи.
А сервис будет информировать, когда он начал каждую задачу выполнять, когда закончил и с каким результатом. Все это будем выводить на экран Activity.
Кстати, чтобы легче было воспринимать это все, замечу, что алгоритм очень похож на работу startActivityForResult.
Только там мы взаимодействуем не с сервисом, а с Activity.
*/

public class L95ServicePendingIntentActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    //Коды задач, для того, чтобы определять от какой задачи пришел ответ
    final int TASK1_REQUEST_CODE = 1;
    final int TASK2_REQUEST_CODE = 2;
    final int TASK3_REQUEST_CODE = 3;

    //Статусы выполнения, которые будем присылать из сервиса
    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    //Название extras, которые будем ложить в интент для отправки в сервис
    public final static String PARAM_TIME = "time";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";

    TextView tvTask1;
    TextView tvTask2;
    TextView tvTask3;

    //В onCreate мы находим TextView и присваиваем им первоначальный текст. Для каждой задачи свой TextView.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l95_service_pending_intent);

        tvTask1 = (TextView) findViewById(R.id.tvTask1);
        tvTask1.setText("Task1");
        tvTask2 = (TextView) findViewById(R.id.tvTask2);
        tvTask2.setText("Task2");
        tvTask3 = (TextView) findViewById(R.id.tvTask3);
        tvTask3.setText("Task3");
    }

    //Обработка нажатия кнопки Start
    public void onClickStart(View v) {
        PendingIntent pi;
        Intent intent;

        //создаем PendingIntent методом createPendingResult для Task1. На вход методу передаем только код запроса – можно считать это идентификатором.
        //Выяснилось, что в 4-й версии Андроид не прокатывает использование null вместо Intent. Поэтому можно создать пустой Intent и использовать его.)
        //requestCode - код, который вернется в onActivityResult
        //data - Intent для возврата по умолчанию в onActivityResult
        //flags - опциональный флаг, далее описания насколько я понял их :) Если не нужен передаем 0.
        //Написано также, что для использования с getActivity, getBroadcast и getService. Этими методами, напр. можно вызвать активити, которое создало PI из сервиса и т.д.
        //  FLAG_ONE_SHOT - флаг указывает, что PendingIntent отрабатывает send() один раз, все следующие попытки будут отменены.
        //  FLAG_NO_CREATE - если такой интент не существует, то вернется null.
        //  FLAG_CANCEL_CURRENT - если такой интент существует (requestCode?), то он будет отменен перед созданием нового. Т.е. нужен для передачи другой extra data.
        //  Только сушьности, которые имеют новую версию смогут ее использовать.
        //  FLAG_UPDATE_CURRENT - если такой интент существует, то он сохраняется, но заменяется Extra. Сущьности имеющие старую версию могут ее использовать.
        //  FLAG_IMMUTABLE - не совсем понятно, дополнительные аргументы в методе send будут проигнорированы.
        pi = createPendingResult(TASK1_REQUEST_CODE, new Intent(), 0);
        // Создаем Intent для вызова сервиса, кладем туда параметр времени и созданный PendingIntent
        intent = new Intent(this, L95Service.class).putExtra(PARAM_TIME, 7).putExtra(PARAM_PINTENT, pi);
        // стартуем сервис
        startService(intent);

        pi = createPendingResult(TASK2_REQUEST_CODE, new Intent(), 0);
        intent = new Intent(this, L95Service.class).putExtra(PARAM_TIME, 4).putExtra(PARAM_PINTENT, pi);
        startService(intent);

        pi = createPendingResult(TASK3_REQUEST_CODE, new Intent(), 0);
        intent = new Intent(this, L95Service.class).putExtra(PARAM_TIME, 6).putExtra(PARAM_PINTENT, pi);
        startService(intent);
    }



    //Здесь получаем ответы с сервиса
    //Ответ PendingIntent, в отличии от BroadcastReceiver, всегда приходит только в ту активити, в которой создавался.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode = " + requestCode + ", resultCode = " + resultCode);

        // Ловим сообщения о старте задач
        if (resultCode == STATUS_START) {
            switch (requestCode) {
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
        if (resultCode == STATUS_FINISH) {
            int result = data.getIntExtra(PARAM_RESULT, 0);
            switch (requestCode) {
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
}
