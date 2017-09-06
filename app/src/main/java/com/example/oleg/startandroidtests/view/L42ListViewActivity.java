package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

public class L42ListViewActivity extends AppCompatActivity implements View.OnClickListener {

    String[] names;
    ListView lv1, lv2, lv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l42_list_view);
        names = getResources().getStringArray(R.array.names);  //Массив строк в strings.xml
        initList1();
        initList2();
        initList3();
    }

    //Простой список
    private void initList1() {
        // находим список
        lv1 = (ListView) findViewById(R.id.lvMain1);
        //Создаем слушатель нажатий на элемент списка
        lv1.setOnItemClickListener(new Lv1OnItemClickListener());
        //Создаем слушатель выбора (срабатывает нажатии на клавиатуре кнопок вверх-вниз)
        lv1.setOnItemSelectedListener(new Lv1OnItemSelectedListener());
        //Слушаем скролл
        lv1.setOnScrollListener(new Lv1OnScrollChangeListener());



        // создаем адаптер (android.R.layout.simple_list_item_1 – это системный layout-файл, который представляет собой TextView)
        //names – массив данных, которые мы хотим вывести в список
        //В layout-ресурсе для пункта списка вместо TextView вы можете использовать какой-нибудь его производный класс – например Button.
        //Главное, чтобы объект прошел преобразование к TextView. Адаптер присвоит ему текст методом setText и отдаст списку.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        // присваиваем адаптер списку
        lv1.setAdapter(adapter);
    }

    //Список с одиночным выбором
    private void initList2() {
        ((Button) findViewById(R.id.btnChecked2)).setOnClickListener(this);
        lv2 = (ListView) findViewById(R.id.lvMain2);
        //Мы устанавливаем для списка режим выбора - CHOICE_MODE_SINGLE.
        //Это значит, что список будет хранить позицию последнего нажатого пункта и мы всегда можем запросить у него эту информацию.
        lv2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Создаем адаптер, используя массив из файла ресурсов
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.names, android.R.layout.simple_list_item_single_choice);
        lv2.setAdapter(adapter);
    }

    //Простой список
    private void initList3() {
        ((Button) findViewById(R.id.btnChecked3)).setOnClickListener(this);
        lv3 = (ListView) findViewById(R.id.lvMain3);
        //Мы устанавливаем для списка режим выбора - CHOICE_MODE_SINGLE.
        //Это значит, что список будет хранить позицию последнего нажатого пункта и мы всегда можем запросить у него эту информацию.
        lv3.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Создаем адаптер, используя массив из файла ресурсов
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.names, android.R.layout.simple_list_item_single_choice);
        lv3.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        String text = "checked: ";
        switch (v.getId()) {
            case R.id.btnChecked2:
                //Получаем имя по индексу выбраного элемента
                String name = lv2.getCheckedItemPosition() >= 0 ? names[lv2.getCheckedItemPosition()] : "none";
                text = text + name;
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                break;
            case R.id.btnChecked3:
                //SparseBooleanArray представляет собой Map(int, boolean). Ключ (int) – это позиция элемента, а значение (boolean) – это выделен пункт списка или нет.
                // Причем SparseBooleanArray хранит инфу не о всех пунктах, а только о тех, с которыми проводили действие (выделяли и снимали выделение).
                SparseBooleanArray sbArray = lv3.getCheckedItemPositions();
                for (int i = 0; i < sbArray.size(); i++) {
                    int key = sbArray.keyAt(i);
                    if(sbArray.get(key)) {
                        text = text + names[key] + "\n";
                    }
                }
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                break;
        }
    }

    //Слушаем клики на элементы списка
    class Lv1OnItemClickListener implements AdapterView.OnItemClickListener {
        /*parent – View-родитель для нажатого пункта, в нашем случае - ListView
        view – это нажатый пункт, в нашем случае – TextView из android.R.layout.simple_list_item_1
        position – порядковый номер пункта в списке
        id – идентификатор элемента*/
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(), "List1: itemClick: position = " + position + ", id = " + id, Toast.LENGTH_LONG).show();
        }
    }

    //Слушаем выбор кнопками элеминтов списка
    class Lv1OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(), "List1: itemSelection: position = " + position + ", id = " + id, Toast.LENGTH_SHORT).show();
        }

        //onNothingSelected – когда список теряет выделение пункта и ни один пункт не выделен, напр. при потере фокуса ListView
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(getApplicationContext(), "List1: itemSelection: nothing", Toast.LENGTH_SHORT).show();
        }
    }

    //Слушаем прокрутку списка
    class Lv1OnScrollChangeListener implements AbsListView.OnScrollListener {

        //обработка состояний прокрутки
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //view – это прокручиваемый элемент, т.е. ListView
            //scrollState – состояние списка. Может принимать три значения:
            //SCROLL_STATE_IDLE = 0, список закончил прокрутку
            //SCROLL_STATE_TOUCH_SCROLL = 1, список начал прокрутку
            //SCROLL_STATE_FLING = 2, список «катнули», т.е. при прокрутке отпустили палец и прокрутка дальше идет «по инерции»
            Log.d("myLog", "scrollState = " + scrollState);
        }

        //обработка прокрутки
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //view – прокручиваемый элемент
            //firstVisibleItem – первый видимый на экране пункт списка (даже частично)
            //visibleItemCount – сколько пунктов видно на экране (даже частично)
            //totalItemCount – сколько всего пунктов в списке
            //Log.d("myLog", "scroll: firstVisibleItem = " + firstVisibleItem + ", visibleItemCount " + visibleItemCount + ", totalItemCount " + totalItemCount);
        }
    }


}
