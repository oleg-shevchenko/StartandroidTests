package com.example.oleg.startandroidtests.view.loader;

import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

//Лоадер следует использовать только если нам не обязательно дожидаться окончания загрузки данных,
//потому что, например, при выходе из активити, лоадер прервется. Т.е. если нам нужно асинхронно
//подгрузить какие-то данные для элементов View в активити из БД или т.п. то используем.
//Иначе, если нам обязательно нужен результат, следует пользоваться сервисами или возможно AsyncTask

/*Создадим класс лоадера. Причем, не в MainActivity, а отдельно, чтобы было нагляднее.
Вообще, можно и в MainActivity создавать, но при этом есть ограничение: он должен быть static.
Иначе LoaderManager ругнется: "Object returned from onCreateLoader must not be a non-static inner member class". */

//Лоадеры появились в третьей версии Android. Предназначены для выполнения асинхронных операций и привязаны к некоторым lifecycle-методам Activity или Fragment-ов.

//Итак, имеем два класса:
//LoaderManager – встроен в Activity и Fragment. Как и следует из названия он управляет объектами Loader.
//Он их создает, хранит, уничтожает и стартует/останавливает. Для взаимодействия с ним используется колбэк интерфейс LoaderCallbacks.
//Loader – объект, который должен уметь асинхронно выполнять какую-либо задачу.

//Loader является параметризированным классом, поэтому нам в скобках <> необходимо указать класс-тип, который указывает, что будет возвращать лоадер после своей работы.

//Существуют три встроенных типа загрузчиков: Loader, AsyncTaskLoader и CursorLoader.
//Loader — базовый класс, который сам по себе не очень полезен. Он определяет API, используемый LoaderManager для взаимодействия со всеми загрузчиками.
public class TimeLoader extends Loader<String> {
    final String LOG_TAG = "myLogs";

    public final static String ARGS_TIME_FORMAT = "time_format";
    public final static String TIME_FORMAT_SHORT = "h:mm:ss a";
    public final static String TIME_FORMAT_LONG = "yyyy.MM.dd G 'at' HH:mm:ss";

    GetTimeTask getTimeTask;
    String format;

    //Описываем собственный конструктор, в args будем передавать формат вывода даты
    public TimeLoader(Context context, Bundle args) {
        super(context);
        Log.d(LOG_TAG, hashCode() + " create TimeLoader");
        //Получаем из Bundle формат, или присваиваем формат по умолчанию
        if (args != null)
            format = args.getString(ARGS_TIME_FORMAT);
        if (TextUtils.isEmpty(format))
            format = TIME_FORMAT_SHORT;
    }

    /*Сразу надо определиться с формулировкой состояний. Будем считать что лоадер перешел в состояние «стартован» после метода onStartLoading
    и в состояние «остановлен» после метода onStopLoading. Это необходимо, т.к. поведение лоадера зависит от состояния
    и нам в дальнейшем нужно будет эти состояния как-то словесно идентифицировать.
    Надо понимать, что два этих метода автоматически не означают, что лоадер начал или закончил работу.
    Это просто переход в состояние стартован и остановлен. А будет он в это время работать или нет, определять вам.*/

    //onStartLoading – вызывается при старте (onStart) Activity или фрагмента, к которому будет привязан Loader.
    //в частности если свернуть активити то срабатывает onStopLoading(), если вернуться то onStartLoading()
    //Также вызывается при рестарте
    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, hashCode() + " onStartLoading");
        super.onStartLoading();
    }

    //onStopLoading – вызывается при остановке (onStop) Activity или фрагмента, к которому будет привязан Loader.
    @Override
    protected void onStopLoading() {
        Log.d(LOG_TAG, hashCode() + " onStopLoading");
        super.onStopLoading();
    }

    //onForceLoad - в этом методе кодим работу лоадера. Для примера запускаем здесь GetTimeTask, который будет нам время получать асинхронно.
    @Override
    protected void onForceLoad() {
        Log.d(LOG_TAG, hashCode() + " onForceLoad");
        super.onForceLoad();
        //Проверяем, если AsyncTask уже существует, то останавливаем его, чтобы он зря не выполнялся
        if (getTimeTask != null) getTimeTask.cancel(true);
        //Создаем новый AsyncTask
        getTimeTask = new GetTimeTask();
        //Запускаем эмуляцию запроса, передав как входной аргумент String с нужным форматом
        getTimeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, format);
    }

    //onAbandon – метод означающий, что лоадер становится неактивным (переводится в разряд устаревшего)
    //вызывается сразу после getLoaderManager().restartLoader()
    @Override
    protected void onAbandon() {
        Log.d(LOG_TAG, hashCode() + " onAbandon");
        super.onAbandon();
    }

    //onReset – означает уничтожение лоадера, вызывается при закрытии (onDestroy) Activity или фрагмента, к которому будет привязан Loader.
    //!! также вродебы вызывается, когда делаем рестарт, после успешной отработки нового лоадера
    //!!!Не вызывается, если onDestroy был вызван, например при смене ориентации.
    @Override
    protected void onReset() {
        Log.d(LOG_TAG, hashCode() + " onReset");
        super.onReset();
    }

    //Метод getResultFromTask – это наш метод. GetTimeTask, по окончании своей работы, вызовет этот метод и передаст нам результаты своей работы.
    //А мы уже вызываем в нем стандартный метод лоадера – deliverResult, который оповещает слушателя, подключенного к лоадеру, что работа окончена и передает ему данные.
    void getResultFromTask(String result) {
        //Стандартный метод лоадера, который оповещает слушателя, подключенного к лоадеру, что работа окончена и передает ему данные.
        deliverResult(result);
    }

    //В AsyncTask имитируем долгое выполнение задачи получения времени (он нужен только для имитации)
    class GetTimeTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(LOG_TAG, TimeLoader.this.hashCode() + " doInBackground");
            //Имитация какого-то выполнения 5 секунды
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                return null;
            }

            //Создаем SimpleDateFormat на основе String, который передаем как взодной параметр AsyncTask
            SimpleDateFormat sdf = new SimpleDateFormat(params[0], Locale.getDefault());
            //Возвращаем строку с текущим временем
            return sdf.format(new Date());
        }

        //Выполняется когда doInBackground вернет результат
        @Override
        protected void onPostExecute(String time) {
            super.onPostExecute(time);
            Log.d(LOG_TAG, TimeLoader.this.hashCode() + " onPostExecute " + time);
            getResultFromTask(time);
        }
    }
}
