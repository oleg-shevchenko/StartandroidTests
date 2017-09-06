package com.example.oleg.startandroidtests.view;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.view.loader.TimeLoader;

//Неплохое инфо по лоадерам англ. http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html

//Здесь используем обычный класс лоадер, но для нашей задачи и вообще лучше пользоваться AsyncTaskLoader, т.к. в нем уже встроен AsyncTask
//Вместо лоадера в этом примере, в пакете с лоадерами еще написан TimeAsyncLoader

//Напишем приложение, в котором используем лоадер, и посмотрим его поведение на примерах.
//Лоадер будет просто определять текущее время, но делать это он будет асинхронно и с учетом формата.

//Реализовывать интерфейс LoaderManager.LoaderCallbacks будем сразу в активити
//На всякий случай поясню, что можно было и создать отдельный объект для этого колбэка, а не использовать Activity. Кому как удобнее.

/*Интерфейс LoaderCallbacks требует реализации трех методов:
    - onCreateLoader – вызывается, когда требуется создать новый лоадер, например, в тот момент, когда мы выше вызываем метод initLoader.
        На вход получает ID требуемого лоадера и Bundle с данными. Т.е. те самые объекты, что мы передавали в initLoader.
    - onLoadFinished – срабатывает, когда лоадер закончил работу и вернул результат. На вход приходит сам лоадер и результат его работы.
    - onLoaderReset – срабатывает, когда LoaderManager собрался уничтожать лоадер. На вход получает лоадер.*/

public class L135LoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    final String LOG_TAG = "myLogs";

    //Описываем ID наших лоадеров для LoaderManager, они должны быть static final int
    static final int LOADER_TIME_ID = 1;

    TextView tvTime;
    RadioGroup rgTimeFormat;
    static int lastCheckedId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l135_loader);

        tvTime = (TextView) findViewById(R.id.tvTime);
        rgTimeFormat = (RadioGroup) findViewById(R.id.rgTimeFormat);

        //Создаем Bundle, и ложим в него строку с нужным форматом времени
        Bundle bndl = new Bundle();
        bndl.putString(TimeLoader.ARGS_TIME_FORMAT, getTimeFormat());

        //В onCreate мы получаем объект LoaderManager с помощью метода getLoaderManager и вызываем его метод initLoader,
        //который создаст и вернет нам Loader. В качестве параметров метода initLoader указываем:
        //  - ID лоадера, это необходимо, т.к. мы запросто можем использовать сразу несколько разных лоадеров, и LoaderManager да и мы сами должны их как-то различать
        //  - объект Bunlde. В него вы помещаете данные, которые хотите использовать при создании лоадера
        //  - объект, реализующий колбэк-интерфейс LoaderCallbacks. Он будет использоваться для взаимодействия с лоадером, в нашем случае это наша активити
        //Метод initLoader возвращает созданный лоадер, но я его никуда не сохраняю, т.к. мне это здесь не нужно.
        Log.d(LOG_TAG, "getLoaderManager().initLoader(LOADER_TIME_ID, bndl, this)");
        //Если загрузчик не существует, то он будет создан, иначе он будет перезапущен (напр. при повороте)
        getLoaderManager().initLoader(LOADER_TIME_ID, bndl, this);
    }

    //Метод onCreateLoader() это метод-фабрика, который возвращает новый загрузчик. Вызывается при первом вызове LoaderManager.initLoader(), а также при LoaderManager.restartLoader().
    //onCreateLoader – будет вызван LoaderManager-м, если до этого загрузчик не существовал,
    //это значит, что при повороте не будет вызываться, так как предыдущий загрузчик с данным ID уже был создан ранее.
    //На вход получает указаный нами ID требуемого лоадера и опциональный Bundle с данными необходимыми для работы. Т.е. те самые объекты, что мы передавали в initLoader.
    //При попытке доступа к загрузчику (например, посредством метода initLoader()), он проверяет, существует ли загрузчик, указанный с помощью идентификатора.
    //Если он не существует, он вызывает метод onCreateLoader(). Именно здесь и создается новый загрузчик.
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader: ");
        Loader<String> loader = null;
        //Проверяем по ID какой лоадер нужно создать, создаем и возвращаем его
        if (id == LOADER_TIME_ID) {
            loader = new TimeLoader(this, args);
            Log.d(LOG_TAG, "onCreateLoader: created " + loader.hashCode());
        }
        return loader;
    }

    //onLoadFinished – вызывается автоматически, когда Loader завершает загрузку данных и вернул результат. Вызывается в основном потоке. Т.е. здесь мы обновляем наш UI
    //Может вызываться несколько раз при изменении данных, например поворотах.
    //При повороте модет вызваться два раза: если еще происходит загрузка, то сначала возвращает предыдущий результат (если был), потом возвращает новый, когда закончит.
    //Если предыдущего не было, то вызывается только в конце

    //Загрузчик следит за поступающими данными, а менеджер получает уведомление о завершении загрузки и передаёт результат данному методу.
    //Этот метод гарантировано вызывается до высвобождения последних данных, которые были предоставлены этому загрузчику.
    //К этому моменту необходимо полностью перестать использовать старые данные (поскольку они скоро будут заменены).
    //Однако этого не следует делать самостоятельно, поскольку данными владеет загрузчик и он позаботится об этом.
    //Загрузчик высвободит данные, как только узнает, что приложение их больше не использует.
    //Например, если данными является курсор из CursorLoader, не следует вызывать close() самостоятельно.
    //Если курсор размещается в CursorAdapter, следует использовать метод swapCursor() с тем, чтобы старый Cursor не закрылся.

    //На вход приходит сам лоадер и результат его работы.
    @Override
    public void onLoadFinished(Loader<String> loader, String result) {
        // Если используется несколько загрузчиков, то удобнее через оператор switch-case
        Log.d(LOG_TAG, "onLoadFinished for loader " + loader.hashCode() + ", result = " + result);
        //Записываем полученую строку в tvTime
        tvTime.setText(result);
    }

    //onLoaderReset – срабатывает, когда LoaderManager собрался уничтожать лоадер. Напр. при уничтожении активити. На вход получает лоадер.
    //!!!Он вызывается только в случае, когда хоть раз были получены данные, т.е. хоть раз отработал
    //Метод onLoadReset() перезагружает данные в загрузчике.
    //Этот метод вызывается, когда состояние созданного ранее загрузчика сбрасывается, в результате чего его данные теряются.
    //Этот обратный вызов позволяет узнать, когда данные вот-вот будут высвобождены, с тем чтобы можно было удалить свою ссылку на них.
    //Т.е. здесь можно освобождать ресурсы, напр. ссылку на курсор сделать null. !!!Но, если используем курсор, его нельзя закрывать (close()), лоадер делает это сам.
    @Override
    public void onLoaderReset(Loader<String> loader) {
        Log.d(LOG_TAG, "onLoaderReset for loader " + loader.hashCode());
    }

    //Метод getTimeClick – обработчик кнопки Get time.
    public void getTimeClick(View v) {
        Loader<String> loader;
        //Определяем: в каком формате требуется получить время.
        int id = rgTimeFormat.getCheckedRadioButtonId();
        //Далее проверяем, если последний созданный лоадер был создан с учетом этого же формата, то просто получаем лоадер методом getLoader по ID.
        if (id == lastCheckedId) {
            Log.d(LOG_TAG, "getLoaderManager().getLoader(LOADER_TIME_ID)");
            loader = getLoaderManager().getLoader(LOADER_TIME_ID);
        //Если же формат другой, то нам нужен новый лоадер. Для этого используется метод restartLoader.
        //Он принимает на вход те же параметры, что и initLoader, и создает новый лоадер.
        } else {
            Bundle bndl = new Bundle();
            bndl.putString(TimeLoader.ARGS_TIME_FORMAT, getTimeFormat());
            Log.d(LOG_TAG, "getLoaderManager().restartLoader(LOADER_TIME_ID, bndl,this)");
            loader = getLoaderManager().restartLoader(LOADER_TIME_ID, bndl,this);
            lastCheckedId = id;
        }
        //Далее у полученного лоадера вызываем метод forceLoad, тем самым запуская работу.
        //loader.forceLoad();
        loader.onContentChanged();

        /*Как видим, у LoaderManager есть аж три метода для получения лоадера: getLoader, initLoader и restartLoader. Давайте сразу проговорим их отличия.
        - getLoader – просто получение лоадера с указанным ID. Если лоадер с таким ID еще не был создан, то метод вернет null.
        - initLoader – создание лоадера если он не существовал. Если лоадер существовал, то метод его и вернет,
            только заменит колбэк-объект, который вы передаете в метод. А если лоадер не просто существовал, но и уже успел отработать,
            то в метод onLoadFinished отправится его последний результат.
        - restartLoader – создание нового лоадера в любом случае. Чуть позже мы рассмотрим на примерах, что происходит если при работающем лоадере создать новый.*/
    }

    //Метод возвращает строку формата даты, исходя из выбраного варианта RadioGroup
    //Константы строк мы описали в классе TimeLoader
    private String getTimeFormat() {
        String result = TimeLoader.TIME_FORMAT_SHORT;
        switch (rgTimeFormat.getCheckedRadioButtonId()) {
            case R.id.rdShort:
                result = TimeLoader.TIME_FORMAT_SHORT;
                break;
            case R.id.rdLong:
                result = TimeLoader.TIME_FORMAT_LONG;
                break;
        }
        return result;
    }
}
