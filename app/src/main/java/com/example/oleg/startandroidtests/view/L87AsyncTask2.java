package com.example.oleg.startandroidtests.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class L87AsyncTask2 extends AppCompatActivity {

    private final String LOG_TAG = "myLogs";
    private static MyAsyncTask mat;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l87_async_task2);
        tvInfo = (TextView) findViewById(R.id.tvInfo);

        //При перерисовке экрана если AsyncTask существует, то установим наше текущее активити для вызовов
        if(mat != null) mat.setTargetActivity(this);
    }

    public void onclick(View v) {
        //Останавливаем текущее выполнение, если оно есть
        if(mat != null) mat.cancel(true);
        //Создаем новый MyAsyncTask
        mat = new MyAsyncTask(this);
        //execute – этот метод мы явно вызываем, чтобы начать выполнение задачи. В него мы передаем набор данных  определенного типа.
        //Этот тип указан первым в угловых скобках при описании AsyncTask (в нашем примере это String) и передастся в метод doInBackground
        mat.execute("file_path_1", "file_path_2", "file_path_3", "file_path_4");
    }

    /*При описании класса-наследника AsyncTask мы в угловых скобках указываем три типа данных:
    1) Тип входных данных. Это данные, которые пойдут на вход AsyncTask. (входящий тип для методов doInBackground а также execute)
    2) Тип промежуточных данных. Данные, которые используются для вывода промежуточных результатов (входящий тип для метода onProgressUpdate)
    3) Тип возвращаемых данных. То, что вернет AsyncTask после работы. (тип возвращаемых данных для метода doInBackground и как входящий для метода onPostExecute)
    Если какой-либо параметр не нужен указываем Void*/
    private static class MyAsyncTask extends AsyncTask<String , Integer, Integer> {
        //******************************************************************************************//
        //В данном примере используем статический класс и мягкую ссылку на активити для предотвращения утечек памяти
        //Т.е. неиспользуемая активити сможет очиститься сборщиком мусора, даже если AsyncTask продолжает работать

        //Мягкая ссылка на активити для доступа к полям и методам, поскольку они недоступны из-за статичности класса
        private WeakReference<L87AsyncTask2> wrActivity;

        //В конструктор передаем ссылку на активити и с ее помощью создаем мягкую ссылку
        private MyAsyncTask(L87AsyncTask2 activity) {
            wrActivity = new WeakReference<L87AsyncTask2>(activity);
        }

        //Метод для установки текущего активити (напр. при перерисовке)
        private void setTargetActivity(L87AsyncTask2 activity) {
            wrActivity.clear();
            wrActivity = new WeakReference<L87AsyncTask2>(activity);
        }
        //******************************************************************************************//

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Проверка на доступность активити
            L87AsyncTask2 activity = wrActivity.get();
            if(activity != null) {
                activity.tvInfo.setText("Begin");
            }
        }

        //Здесь используются входные данные. У нас это String, т.е. первый тип из угловых скобок
        @Override
        protected Integer doInBackground(String... urls) {
            int cnt = 0;
            try {
                for (String url : urls) {
                    //Если мы где-то используем метод cancel для остановки таска, значит нужно где-нибудь проверять этот флаг, для завершения задачи
                    if(isCancelled()) break;
                    // загружаем файл
                    downloadFile(url);
                    //Вызываем метод для обработки промежуточных результатов
                    //Когда мы в doInBackground вызываем метод publishProgress и передаем туда данные, срабатывает метод onProgressUpdate и получает эти данные.
                    publishProgress(++cnt);
                }
                // разъединяемся
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //return используется для передачи результата в onPostExecute
            //Тип результата указывается в последних угловых скобках, его можно получить вызвав метод get у обьекта AsyncTask
            return cnt;
        }

        //Имитация загрузки файлов
        private void downloadFile(String url) throws InterruptedException {
            TimeUnit.SECONDS.sleep(2);
        }

        //Когда мы в doInBackground вызываем метод publishProgress и передаем туда данные, срабатывает метод onProgressUpdate и получает эти данные.
        //Тип этих данных равен второму типу из угловых скобок, т.е. Integer. Метод onProgressUpdate используется для вывода промежуточных результатов.
        //Он выполняется в основном потоке и имеет доступ к UI.
        //Из doInBackground мы (с помощью publishProgress) передали в onProgressUpdate кол-во загруженных файлов – это кол-во и выводим в TextView.
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            L87AsyncTask2 activity = wrActivity.get();
            if(activity != null) {
                activity.tvInfo.setText("Downloaded " + values[0] + " files");
            }
        }

        //На вход подается тип данных, указанный в последних угловых скобках, также результат доступен методу get()
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            L87AsyncTask2 activity = wrActivity.get();
            if(activity != null) {
                activity.tvInfo.setText("End. Downloaded files " + result);
            }
        }

        //Выполняется при отмене выполнения AsyncTask методом cancel() и возврате результата из doInBackground
        //в стандартной реализации вызывает onCancelled() без аргументов
        @Override
        protected void onCancelled(Integer result) {
            Log.d("myLogs", "onCanceled() with result " + result);
            L87AsyncTask2 activity = wrActivity.get();
            if(activity != null) {
                activity.tvInfo.setText("Cancel. Downloaded files: " + result);
            }
        }

        //Выполняется при отмене выполнения AsyncTask методом cancel() и возврате результата из doInBackground
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("myLogs", "onCanceled()");
            L87AsyncTask2 activity = wrActivity.get();
            if(activity != null) {
                activity.tvInfo.setText("Cancel");
            }
        }
    }

    //Вывод результата
    //Получить этот объект мы можем двумя способами:
    //1) Он передается на вход метода onPostExecute, который срабатывает по окончании задачи
    //2) Метод get возвращает нам этот объект

    //Мы вызываем метод get, чтобы получить результат работы AsyncTask. Но что будет, если задача еще не завершена, а мы вызвали get?
    //Метод get будет ждать. Т.е. просто блокирует поток, в котором он выполняется, и не отпустит, пока не получит какой-то результат или не выскочит exception.
    public void showResult(View v) {
        //Проверяем существует ли AsyncTask и закончилось ли выполнение, чтобы не заблокировать выполнение UI потока ожиданием метода get()
        //Мы всегда можем определить, в каком состоянии сейчас находится задача. Для этого используются статусы. Их всего три:
        //PENDING – задача еще не запущена
        //RUNNING – задача в работе
        //FINISHED – метод onPostExecute отработал, т.е. задача успешно завершена
        //Кстати, нет статуса CANCELED, статус RUNNING так и продолжает висеть, поэтому ловим CancellationException или проверяем isCanceled()
        if (mat == null || mat.getStatus() != AsyncTask.Status.FINISHED || mat.isCancelled()) {
            Toast.makeText(this, "AsynkTask null or status PENDING/RUNNING or isCanceled", Toast.LENGTH_SHORT).show();
            return;
        }
        int result = -1;
        try {
            Log.d(LOG_TAG, "Try to get result");
            //Пытаемся получить результат
            result = mat.get();
            Toast.makeText(this, "Downloaded files: " + result, Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            Log.d(LOG_TAG, "Interrupted");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (CancellationException e) {
            //Вылетит, если мы отменили задачу методом cancel(), а потом вызвали get()
            Toast.makeText(this, "Cancellation exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        /*Чтобы долго не блокировать поток, у метода get возможно указать таймаут. Если в течении этого времени не удастся
        получить результат, то вылетит TimeoutException, который мы должны ловить.
        try {
            result = mat.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            Log.d(LOG_TAG, "get timeout, result = " + result);
            e.printStackTrace();
        }*/
    }

    //Отмена выполнения AsyncTask по нажатию кнопки
    public void cancelTask(View v) {
        if (mat == null) return;

        //- Иногда возникает необходимость отменить уже выполняющуюся задачу. Для этого в AsyncTask есть метод cancel.
        //- Он на вход принимает boolean-параметр, который указывает, может ли система немедленно прервать выполнение потока
        //      если флаг true, то doInBackground сразу прерывается и возвращает значение
        //      если флаг false, то устанавливается флаг CANCELED, значит нам в таске нужно периодически вызывать метод isCanceled() для проверки флага и самим заканчивать выполнение
        //- Метод cancel возвращает boolean. Мы получим false, если задача уже завершена или отменена, и true если мы остановка произошла
        //- При вызове метода cancel() не вызывается onPostExecute(), а срабатывают методы onCancelled()
        boolean result = mat.cancel(false);
        Toast.makeText(this, "Task canceled: " + result, Toast.LENGTH_SHORT).show();
        //!!!Если после этого вызвать метод get() то получим CancellationException, поэтому в catch нужно это предусмотреть
    }
}
