package com.example.oleg.startandroidtests.view;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.oleg.startandroidtests.R;

//Рассмотренный нами в прошлых уроках ActionBar – это альтернатива обычному меню прошлых версий.
//В третьей версии Андроида появилась также альтернатива и контекстному меню - ActionMode. Посмотрим, как его можно использовать.

public class L113ActionModeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionMode actionMode;
    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l113_action_mode);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    //Чтобы вызвать ActionMode, используется метод startActionMode.
    //На вход он берет объект callback, который будет обрабатывать все события, связанные с ActionMode. На выходе получаем объект ActionMode.
    //В методе onClick мы проверяем, если ActionMode еще не был вызван, то вызываем. Иначе – убираем его с помощью его же метода finish.
    public void onClick(View v) {
        if (actionMode == null)
            //Для того, чтобы toolbar перекрывать toolbar, а не добавлять сверху в стиль темы пишем
            //<item name="windowActionModeOverlay">true</item>

            actionMode = startSupportActionMode(callback);
            //actionMode = toolbar.startActionMode(callback);
        else
            actionMode.finish();
    }

    private ActionMode.Callback callback = new ActionMode.Callback() {

        //onCreateActionMode – вызывается при создании ActionMode. Возвращаем true, если ActionMode можно создавать.
        //Здесь мы наполняем ActionMode пунктами меню (через объект Menu).
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.l113_context_menu, menu);
            return true;
        }

        //onPrepareActionMode – вызывается при обновлении ActionMode. Например, в случае вызова метода invalidate.
        // Возвращаем true, если ActionMode можно обновить.
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        //onActionItemClicked – обработка нажатия на какой-либо пункт ActionMode. Будем выводить в лог текст нажатого пункта.
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Log.d(LOG_TAG, "item " + item.getTitle());
            return false;
        }

        //onDestroyActionMode – вызывается при закрытии ActionMode. Пишем лог и обнуляем переменную actionMode,
        // чтобы в onClick (см.выше) у нас работала проверка (actionMode == null).
        public void onDestroyActionMode(ActionMode mode) {
            Log.d(LOG_TAG, "destroy");
            actionMode = null;
        }

    };
}
