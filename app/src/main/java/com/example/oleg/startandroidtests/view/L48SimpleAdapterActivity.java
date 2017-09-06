package com.example.oleg.startandroidtests.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class L48SimpleAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l48_simple_adapter);
        initListView1();
        initListView2();
        initListView3();
    }

    /*А как адаптер понимает, какие методы и для каких View-компонентов надо вызывать, чтобы передать им значения из Map?
 Он смотрит на тип View-компонента и в зависимости от него уже определяет как работать с ним. Для него существует три типа:

1) View, наследующие интерфейс Checkable. Примеры - CheckBox, CheckedTextView, CompoundButton, RadioButton, Switch, ToggleButton.
В этом случае адаптер проверяет, является ли соответствующее значение из Map типа boolean. Если да, то все ок и вызывается метод Checkable.setChecked.
Иначе получим ошибку.

С версии 2.2 алгоритм чуть поменялся – добавили еще одну ветку логики. Теперь, если значение из Map не boolean, то проверяется,
что View является наследником TextView. Если да, то вставляется текстовое значение из Map.

2) View, являющиеся TextView или его наследниками.  Тут просто вызывается метод SimpleAdapter.setViewText,
а он уже вызывает TextView.setText и передает туда значение из Map.

3) View, являющиеся ImageView или его наследниками. Адаптер проверяет тип данных из Map:
- если их можно привести (instanceof) к типу Integer, то вызывается метод SimpleAdapter.setViewImage(ImageView v, int value),
а он уже вызывает ImageView.setImageResource.
- иначе вызывается метод SimpleAdapter.setViewImage (ImageView v, String value), который снова пытается привести значение к int
и вызвать метод ImageView.setImageResource, и, если не получается, то преобразует строку к Uri (Uri.parse) и вызывает метод ImageView.setImageURI(Uri).

Если View не подходит ни под один из трех вышеперечисленных типов, то получим ошибку.*/
//Пример 1
//**************************************************************************************************
    //Используем чистый SimpleAdapter
    ArrayList<Map<String, Object>> data1;
    SimpleAdapter adapter1;
    ListView lvSimple1;
    private void initListView1() {

        // имена атрибутов для Map
        final String ATTRIBUTE_NAME_TEXT = "text";
        final String ATTRIBUTE_NAME_CHECKED = "checked";
        final String ATTRIBUTE_NAME_IMAGE = "image";

        // массивы данных
        String[] texts = {"sometext 1", "sometext 2", "sometext 3", "sometext 4", "sometext 5"};
        boolean[] checked = {true, false, false, true, false};
        int img = R.mipmap.ic_launcher_round;

        // упаковываем данные в понятную для адаптера структуру
        data1 = new ArrayList<Map<String, Object>>(texts.length);
        Map<String, Object> m;
        for (int i = 0; i < texts.length; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT, texts[i]);
            m.put(ATTRIBUTE_NAME_CHECKED, checked[i]);
            m.put(ATTRIBUTE_NAME_IMAGE, img);
            data1.add(m);
        }
        // массив имен атрибутов, из которых будут читаться данные
        String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_CHECKED, ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = {R.id.tvText, R.id.cbChecked, R.id.cbChecked, R.id.ivImg};

        // создаем адаптер
        adapter1 = new SimpleAdapter(this, data1, R.layout.layout_item_l48, from, to);
        // определяем список и присваиваем ему адаптер
        lvSimple1 = (ListView) findViewById(R.id.lvSimple1);
        lvSimple1.setAdapter(adapter1);

        //Урок 51, удаление из списка (также реализуется и вставка)
        //добавляем контекстное меню
        registerForContextMenu(lvSimple1);
    }

    //Описываем контекстное меню
    //Если есть чекбоксы или кнопки, как здесь, то контекстное меню не вызовется
    //Нужно добавить android:descendantFocusability="blocksDescendants" в корневой layout пункта списка
    //1) Если его нет то onItemClick() вообще не вызовется если на item есть кнопки!
    //2) Если он есть то onItemClick() будет вызываться при клике на пустом месте в item (или элементах у которых clicable=false) , при клике не кнопку onItemClick() не вызовется
    private static final int CM_DELETE_ID = 1;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //if(v == lvSimple)
        menu.add(0, CM_DELETE_ID, 0, "Удалить запись");
    }

    //Удаление записи при вызове конт. меню
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем инфу о пункте списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            // удаляем Map из коллекции, используя позицию пункта в списке
            data1.remove(acmi.position);
            // уведомляем, что данные изменились
            adapter1.notifyDataSetChanged();
            return true;
        }
        return super.onContextItemSelected(item);
    }

//Пример 2
//*************************************************************************************************
    //В качестве примера, сделаем список, отражающий динамику некоего показателя в разрезе дней.
    // Если динамика положительная – будем разукрашивать элементы в зеленый цвет, если отрицательная – в красный.
    private void initListView2() {
        // имена атрибутов для Map
        final String ATTRIBUTE_NAME_TEXT = "text";
        final String ATTRIBUTE_NAME_VALUE = "value";
        final String ATTRIBUTE_NAME_IMAGE = "image";

        // картинки для отображения динамики
        final int positive = android.R.drawable.arrow_up_float;
        final int negative = android.R.drawable.arrow_down_float;

        ListView lvSimple;

        // массив данных
        int[] values = { 8, 4, -3, 2, -5, 0, 3, -6, 1, -1 };

        // упаковываем данные в понятную для адаптера структуру
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(values.length);
        Map<String, Object> m;
        int img = 0;
        for (int i = 0; i < values.length; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT, "Day " + (i + 1));
            m.put(ATTRIBUTE_NAME_VALUE, values[i]);
            if (values[i] == 0) img = 0;
            else img = (values[i] > 0) ? positive : negative;
            m.put(ATTRIBUTE_NAME_IMAGE, img);
            data.add(m);
        }

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_VALUE, ATTRIBUTE_NAME_IMAGE };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.tvText, R.id.tvValue, R.id.ivImg };

        // создаем адаптер
        MySimpleAdapter sAdapter = new MySimpleAdapter(this, data, R.layout.layout_item_l49, from, to);
        // определяем список и присваиваем ему адаптер
        lvSimple = (ListView) findViewById(R.id.lvSimple2);
        lvSimple.setAdapter(sAdapter);
    }

    //Наследуемся для переопределения методов
    class MySimpleAdapter extends SimpleAdapter {
        //конструктор суперкласса
        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        //Переопределяем метод вставки текста
        @Override
        public void setViewText(TextView v, String text) {
            // метод супер-класса, который вставляет текст
            super.setViewText(v, text);
            // если нужный нам TextView, то разрисовываем
            if (v.getId() == R.id.tvValue) {
                int i = Integer.parseInt(text);
                if (i < 0) v.setTextColor(Color.RED);
                else if (i > 0) v.setTextColor(Color.GREEN);
            }
        }

        // картинки для отображения динамики
        final int positive = android.R.drawable.arrow_up_float;
        final int negative = android.R.drawable.arrow_down_float;
        @Override
        public void setViewImage(ImageView v, int value) {
            // метод супер-класса
            super.setViewImage(v, value);
            // разрисовываем ImageView
            if (value == negative) v.setBackgroundColor(Color.RED);
            else if (value == positive) v.setBackgroundColor(Color.GREEN);
        }
    }

//Пример 3 - используем свой SimpleAdapter.ViewBinder (урок 50)
//**************************************************************************************************
/*Адаптер SimpleAdapter при своей работе сопоставляет View-компоненты и значения из Map-объектов.
Как он это делает по умолчанию и с каким View-компонентами умеет работать, мы рассмотрели в предыдущих уроках.
Но если нам не хватает этих возможностей, мы всегда можем создать свой обработчик и присвоить его адаптеру.
Для этого используется метод setViewBinder (SimpleAdapter.ViewBinder viewBinder), который на вход требует объект SimpleAdapter.ViewBinder.
Мы создаем свой вариант этого биндера и реализуем в нем метод setViewValue(View view, Object data, String textRepresentation),
в котором прописываем всю логику сопоставления данных и компонентов (биндинга). Метод возвращает значение boolean.

Алгоритм работы адаптера таков: он сначала проверяет, давали ли ему сторонний биндер.
Если находит, то выполняет его метод setViewValue. Если метод возвращает true, то адаптер считает, что обработка успешно завершена,
если же false – то он выполняет биндинг в своем стандартном алгоритме, который мы рассмотрели на предыдущих уроках.
Если адаптер не находит сторонний биндер, он также выполняет стандартный биндинг.

Т.е. наша задача – заполнить метод SimpleAdapter.ViewBinder.setViewValue. И здесь уже нет ограничений на TextView или ImageView,
может быть обработан и заполнен любой компонент. Создадим пример, в котором будем заполнять значение ProgressBar и менять цвет LinearLayout.
*/
    private void initListView3() {
        // имена атрибутов для Map
        final String ATTRIBUTE_NAME_TEXT = "text";  //Text
        final String ATTRIBUTE_NAME_PB = "pb";      //ProgressBar
        final String ATTRIBUTE_NAME_LL = "ll";      //LinearLayout
        ListView lvSimple = (ListView) findViewById(R.id.lvSimple3);

        // массив данных
        int load[] = { 41, 48, 22, 35, 30, 67, 51, 88 };

        // упаковываем данные в понятную для адаптера структуру
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(load.length);
        Map<String, Object> m;
        for (int i = 0; i < load.length; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT, "Day " + (i+1) + ". Load: " + load[i] + "%");
            m.put(ATTRIBUTE_NAME_PB, load[i]);
            m.put(ATTRIBUTE_NAME_LL, load[i]);
            data.add(m);
        }
        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_PB, ATTRIBUTE_NAME_LL };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.tvLoad, R.id.pbLoad, R.id.llLoad };
        // создаем адаптер
        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.layout_item_l50, from, to);
        // Указываем адаптеру свой биндер
        sAdapter.setViewBinder(new MyViewBinder());

        lvSimple.setAdapter(sAdapter);

    }

    class MyViewBinder implements SimpleAdapter.ViewBinder {
        int red = getResources().getColor(R.color.Red);
        int orange = getResources().getColor(R.color.Orange);
        int green = getResources().getColor(R.color.Green);

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            //view - View-компонент, data - данные для него
            //textRepresentation - текстовое представление данных (data.toString() или пустой String, но никогда не null)
            int i = 0;
            switch (view.getId()) {
                // LinearLayout
                case R.id.llLoad:
                    //Здесь получаем сопоставленое значение из from-to
                    i = ((Integer) data).intValue();
                    if (i < 40) view.setBackgroundColor(green);
                    else if (i < 70) view.setBackgroundColor(orange);
                    else view.setBackgroundColor(red);
                    //Возвращаем true. Это важно! Тем самым мы говорим адаптеру,
                    //что мы успешно выполнили биндинг для данного компонента и выполнять для него стандартную обработку не надо.
                    return true;
                // ProgressBar
                case R.id.pbLoad:
                    i = ((Integer) data).intValue();
                    ((ProgressBar)view).setProgress(i);
                    return true;
            }
            //Если нам view не нужно обрабатывать, то возвращаем стандартному обработчику
            return false;
        }
    }

}
