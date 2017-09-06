package com.example.oleg.startandroidtests.view;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.db.L136DB;

import java.util.concurrent.TimeUnit;

//неплохое описание CursorLoader http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html

public class L136CursorLoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CM_DELETE_ID = 1;
    ListView lvData;
    L136DB db;
    SimpleCursorAdapter scAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l136_cursor_loader);

        // открываем подключение к БД
        db = new L136DB(this);
        db.open();

        // формируем столбцы сопоставления
        String[] from = new String[] { L136DB.COLUMN_IMG, L136DB.COLUMN_TXT };
        int[] to = new int[] { R.id.ivImg, R.id.tvText };

        // создаем адаптер и настраиваем список, вместо курсора передаем null, дальше, когда вызовем initLoader() первый раз, лоадер сразу отработает
        //и в методе onLoadFinished() мы вызовем scAdapter.swapCursor(), который заменит курсор и ListView отобразит данные
        scAdapter = new SimpleCursorAdapter(L136CursorLoaderActivity.this, R.layout.item_l136, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lvData = (ListView) findViewById(R.id.lvData);
        //Присваимваем пока пустой адаптер для ListView
        lvData.setAdapter(scAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);

        // создаем CursorLoader
        // CursorLoader при переходе в состояние «стартован» стартует работу, если еще не было получено никаких результатов (например при первом запуске)
        getLoaderManager().initLoader(0, null, this);
    }

    //обработка нажатия кнопки
    //добавляем запись в БД, получаем лоадер и просим его получить для нас новый курсор с данными и соответственно в конце вызовется метод onLoadFinished()
    public void onButtonClick(View view) {
        // добавляем запись
        db.addRec("sometext " + (scAdapter.getCount() + 1), R.drawable.arrow_left);
        // получаем новый курсор с данными
        //!!!!Здесь описан вариант, что мы в активити сами заботимся о перезагрузке данных, когда они изменились,
        //!!!!т.е. вручную вызываем forceLoad(), после того, как удалили или добавили запись в базу
        //!!!!Есть еще вариант реализовать Observer и чтобы лоадер сам запускал себе forceLoad(), когда меняем БД через L136DB
        getLoaderManager().getLoader(0).forceLoad();
    }

    //onCreateContextMenu – создание контекстного меню.
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Delete record");
    }

    //В onContextItemSelected мы реализуем удаление записи из БД. И после удаления снова просим лоадер дать нам новый курсор с данными.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            db.delRec(acmi.id);
            // получаем новый курсор с данными
            getLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    //В onDestroy отключаемся от БД.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
    }

    //В onCreateLoader создаем Loader и даем ему на вход объект для работы с БД.
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(L136CursorLoaderActivity.this, db);
    }

    //В onLoadFinished мы получаем результат работы лоадера – новый курсор с данными. Этот курсор мы отдаем адаптеру методом swapCursor.
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // В примере этого нет, но в другой статье обнуляют курсор для адаптера
        // For whatever reason, the Loader's data is now unavailable. Remove any references to the old data by replacing it with a null Cursor.
        scAdapter.swapCursor(null);
    }

    //MyCursorLoader – наш лоадер, наследник класса CursorLoader. У него мы переопределяем метод loadInBackground, в котором просто получаем курсор с данными БД.
    //Ну и я 1 секундной паузой сэмулировал долгое чтение БД для наглядности асинхронной работы.
    static class MyCursorLoader extends CursorLoader {
        L136DB db;

        public MyCursorLoader(Context context, L136DB db) {
            super(context);
            this.db = db;
        }

        //В методе суперкласса идет проверка, если mCursor != null вызывается deliverResult(mCursor);
        //иначе (т.е. при первом запуске) сразу начинается forceLoad(), т.е. отдельно его вызывать сразу не нужно
        @Override
        protected void onStartLoading() {
            super.onStartLoading();
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }
    }

    /*Кроме асинхронной загрузки, CursorLoader:
        - закрывает старый курсор при успешном получении нового
        - закрывает курсор при уничтожении лоадера (т.е. и при выходе из приложения)
        - при переходе в состояние «стартован» проверяет метку, которую ставит Observer и запускает работу, если данные изменились
        - при переходе в состояние «стартован» стартует работу, если еще не было получено никаких результатов (например при первом запуске).*/
}
