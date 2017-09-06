package com.example.oleg.startandroidtests.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

import java.util.concurrent.TimeUnit;

/*На прошлых уроках мы рассматривали, как в приложении можно выполнять тяжелые задачи.
Мы вводили их в отдельный поток и использовали Handler для обратной связи и обновления экрана.
Создатели Android решили, что эти механизмы стоит выделить в отдельный класс – AsyncTask.
Т.е. его цель – это выполнение тяжелых задач и передача в UI-поток результатов работы.
Но при этом нам не надо задумываться о создании Handler и нового потока.*/

public class L86AsyncTask1 extends AppCompatActivity {

    MyAsyncTask mat;
    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l86_async_task1);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
    }

    //Выполняется при нажатии на кнопку
    //Обратите внимание, что если мы нажмем на Start, пока работает AsyncTask, то создастся и запустится новый AsyncTask.
    //Но старый при этом никуда не денется, и продолжит работу в своем потоке.
    public void onclick(View v) {
        //Создаем экземпляр MyAsyncTask
        mat = new MyAsyncTask();
        //Запускаем обработку
        mat.execute();
    }

    //Для создания нужно наследоваться от AsyncTask и переопределить нужные методы
    /*Официальный хелп дает 4 правила использования AsyncTask, я также укажу их здесь:
    - объект AsyncTask должен быть создан в UI-потоке
    - метод execute должен быть вызван в UI-потоке
    - не вызывайте напрямую методы onPreExecute, doInBackground, onPostExecute и onProgressUpdate (последний мы пока не проходили)
    - AsyncTask может быть запущен (execute) только один раз, иначе будет exception*/

    //!!!Рекомендуется использовать static class для AsyncTask, а также WeakReference на активити внутри для предотвращения утечек памяти
    //Это описано в L80HandlerActivity, и в следующем примере
    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        //onPreExecute – выполняется перед doInBackground, имеет доступ к UI
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Begin");
        }

        //doInBackground – будет выполнен в новом потоке, здесь решаем все свои тяжелые задачи. Т.к. поток не основной - не имеет доступа к UI.
        //обязателен для имплементации
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        //onPostExecute – выполняется после doInBackground (не срабатывает в случае, если AsyncTask был отменен - об этом в следующих уроках), имеет доступ к UI
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tvInfo.setText("End");
        }
    }
}
