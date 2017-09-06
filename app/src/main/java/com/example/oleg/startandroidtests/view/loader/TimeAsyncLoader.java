package com.example.oleg.startandroidtests.view.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/*AsyncTaskLoader это лоадер, который выполнит свою работу асинхронно и вернет вам результат. Это наследник класса Loader — базовый класс,
который сам по себе не очень полезен. Он определяет API, используемый LoaderManager для взаимодействия со всеми загрузчиками.

Класс TimeLoader на основе Loader, который мы сделали, в принципе является упрощенной версией AsyncTaskLoader,
т.к. он свою работу тоже в AsyncTask выполняет. Но чтобы самим каждый раз не возиться с AsyncTask, существует AsyncTaskLoader.*/

public class TimeAsyncLoader extends AsyncTaskLoader<String> {

    final String LOG_TAG = "myLogs";
    final int PAUSE = 10;

    public final static String ARGS_TIME_FORMAT = "time_format";
    public final static String TIME_FORMAT_SHORT = "h:mm:ss a";
    public final static String TIME_FORMAT_LONG = "yyyy.MM.dd G 'at' HH:mm:ss";

    //Требуемый формат времени, который передаем с помощью Bundle и описаных выше констант в конструктор
    String format;

    //Конструктор, который будем вызывать в методе onCreateLoader()
    public TimeAsyncLoader(Context ctx, Bundle args) {
        super(ctx);
        if(args != null) {
            format = args.getString(ARGS_TIME_FORMAT);
        }
        //если по какой-то причине в Bundle нет нужной строки, то присваиваем значение по умолчанию
        if(TextUtils.isEmpty(format)) {
            format = TIME_FORMAT_SHORT;
        }
    }

    @Override
    public String loadInBackground() {
        Log.d(LOG_TAG, hashCode() + " loadInBackground start");
        try {
            TimeUnit.SECONDS.sleep(PAUSE);
        } catch (InterruptedException e) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }
}

/*У класса AsyncTaskLoader есть метод отмены: cancelLoad. Отмененный лоадер по окончании работы вызовет уже не onLoadFinished, а onCanceled в AsyncTaskLoader.
Есть еще метод setUpdateThrottle, который позволит поставить задержку между двумя последовательными вызовами одного лоадера.
Т.е. вы, например, поставили эту задержку в 10 000 мсек. Далее запускаете лоадер, он отрабатывает.
И вы сразу пытаетесь запустить его еще раз. Но он не запустится. Он отсчитает 10 сек после окончания работы последнего запуска, а потом уже снова начнет работу.
*/
