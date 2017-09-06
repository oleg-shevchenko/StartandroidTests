package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.oleg.startandroidtests.R;

//Т.к. ActionMode позиционируется как замена контекстному меню, то вполне можно повесить его вызов на долгое нажатие на какой-либо элемент экрана.
//Чтобы обработать долгое нажатие надо вызывать для элемента метод setOnLongClickListener, передать туда объект,
// реализующий интерфейс OnLongClickListener, и в методе onLongClick этого объекта накодить вызов ActionMode.

//Для некоторых элементов вызов ActionMode по долгому нажатию уже реализован.
//Это наследники класса AbsListView, например GridView и ListView. Попробуем на ListView, как это работает.

public class L113ActionModeListActivity extends AppCompatActivity {

    ListView lvActionMode;
    final String LOG_TAG = "myLogs";

    String[] data = { "one", "two", "three", "four", "five" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l113_action_mode_list_acticity);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, data);
        lvActionMode = (ListView) findViewById(R.id.lvActionMode);
        lvActionMode.setAdapter(adapter);
        //* The list allows multiple choices in a modal selection mode
        lvActionMode.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);

        //устанавливаем объект обработчик, реализующий AbsListView.MultiChoiceModeListener.
        //Методы здесь все те же, что и ранее нами рассмотренные в ActionMode.Callback,
        //плюс добавляется один – onItemCheckedStateChanged, в котором мы получаем инфу о выделенных пунктах списка.
        //Т.е. этот обработчик и выделение пунктов списка отслеживает и ActionMode контролирует.
        lvActionMode.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            // onItemCheckedStateChanged, в котором мы получаем инфу о выделенных пунктах списка.
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Log.d(LOG_TAG, "position = " + position + ", checked = " + checked);

                mode.setTitle("Selected: " + lvActionMode.getCheckedItemCount());
                if(checked) mode.setSubtitle("Last checked: " + lvActionMode.getItemAtPosition(position));
            }

            //В onCreateActionMode мы указываем, из какого файла брать пункты меню
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.l113_context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            //в onActionItemClicked закрываем ActionMode независимо от того, какой пункт меню был выбран
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                mode.finish();
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }
}
