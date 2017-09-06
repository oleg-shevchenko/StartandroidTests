package com.example.oleg.startandroidtests.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

/*LayoutInflater – это класс, который умеет из содержимого layout-файла создать View-элемент.
Метод который это делает называется inflate. Есть несколько реализаций этого метода с различными параметрами.
Но все они используют друг друга и результат их выполнения один – View.

Мы рассмотрим эту реализацию – public View inflate (int resource, ViewGroup root, boolean attachToRoot)
Как видим, на вход метод принимает три параметра:
resource - ID layout-файла, который будет использован для создания View. Например - R.layout.main
root – родительский ViewGroup-элемент для создаваемого View. LayoutParams от этого ViewGroup присваиваются создаваемому View.
attachToRoot – присоединять ли создаваемый View к root. Если true, то root становится родителем создаваемого View.
Т.е. это равносильно команде root.addView(View).  Если false – то создаваемый View просто получает LayoutParams от root, но его дочерним элементом не становится.*/

public class L40LayoutInflaterActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l40_layout_inflater);

/*Убрать для работы первой части


        //Получаем LayoutInflater
        LayoutInflater ltInflater = getLayoutInflater();
        //Получили View елемент из layout-файла
        View view = ltInflater.inflate(R.layout.layout_l40_text, null, false);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        //Мы видим класс созданного элемента - TextView. Все верно - этот элемент и был в файле text.xml. Далее видим null вместо LayoutParams.
        // Это произошло потому, что родителя в методе inflate мы указали null. А именно от родителя view и должен был получить LayoutParams.
        // Третья строка лога показывает текст TextView. Он тот же, что и в layout-файле text.xml – все верно.
        Log.d(LOG_TAG, "Class of view: " + view.getClass().toString());
        Log.d(LOG_TAG, "LayoutParams of view is null: " + (lp == null));
        Log.d(LOG_TAG, "Text of view: " + ((TextView) view).getText());

        //Давайте немного изменим программу. Будем добавлять наш созданный элемент в linLayout из main.xml. Делается это просто – командой addView.
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
        linLayout.addView(view);

        //Теперь давайте попробуем указать родителя (root) при вызове метода inflate. Перепишем метод onCreate:
        //Мы находим элементы linLayout и relLayout с экрана и с помощью LayoutInflater создаем два View-элемента из layout-файла layout_l40_text.xml.
        //Для первого указываем root – linLayout, для второго – relLayout. Но третий параметр attachToRoot оставляем false.
        //Это значит, что созданный View-элемент получит LayoutParams от root-элемента, но не добавится к нему, т.е. ничего не отобразится.
        linLayout.removeView(view);         //Удаляем для следующего теста
        linLayout = (LinearLayout) findViewById(R.id.linLayout);
        View view1 = ltInflater.inflate(R.layout.layout_l40_text, linLayout, false);
        ViewGroup.LayoutParams lp1 = view1.getLayoutParams();
        Log.d(LOG_TAG, "Class of view1: " + view1.getClass().toString());
        Log.d(LOG_TAG, "Class of layoutParams of view1: " + lp1.getClass().toString());
        Log.d(LOG_TAG, "Text of view1: " + ((TextView) view1).getText());

        RelativeLayout relLayout = (RelativeLayout) findViewById(R.id.relLayout);
        View view2 = ltInflater.inflate(R.layout.layout_l40_text, relLayout, false);
        ViewGroup.LayoutParams lp2 = view2.getLayoutParams();
        Log.d(LOG_TAG, "Class of view2: " + view2.getClass().toString());
        Log.d(LOG_TAG, "Class of layoutParams of view2: " + lp2.getClass().toString());
        Log.d(LOG_TAG, "Text of view2: " + ((TextView) view2).getText());

        //Теперь у нас два варианта, как добавить созданные view1 и view2 на экран.
        //1) Снова использовать методы addView
        //2) Передавать true в качестве третьего параметра метода inflate. Тогда созданный View-элемент будет добавлен к root.
        //Выберем второй вариант:
        linLayout.removeView(view1);         //Удаляем для следующего теста
        view1 = ltInflater.inflate(R.layout.layout_l40_text, linLayout, true);
        lp1 = view1.getLayoutParams();
        Log.d(LOG_TAG, "Class of view1: " + view1.getClass().toString());
        Log.d(LOG_TAG, "Class of layoutParams of view1: " + lp1.getClass().toString());

        relLayout.removeView(view2);         //Удаляем для следующего теста
        view2 = ltInflater.inflate(R.layout.layout_l40_text, relLayout, true);
        lp2 = view2.getLayoutParams();
        Log.d(LOG_TAG, "Class of view2: " + view2.getClass().toString());
        Log.d(LOG_TAG, "Class of layoutParams of view2: " + lp2.getClass().toString());

        //Обратите внимание на класс элементов. В первом случае - это LinearLayout, а во втором - RelativeLayout.
        // Т.е. метод inflate вернул нам не созданные из layout-файла View-элементы, а те, что мы указывали как root!!!
        // А созданные из layout-файла View элементы он добавил в root как дочерние аналогично команде addView.
        // Это произошло потому, что мы указали true в третьем параметре (attachToRoot) метода inflate.

        //Основные методы для работы с индексами:
        //int getChildCount() - возвращает общее количество добавленных элементов в группе.
        //int indexOfChild(View child) - Возвращает позицию дочернего элемента в группе.
        //View getChildAt(int index) - Возвращает дочерний элемент по позиции в группе.
        //removeViewAt(index);
        //removeView(view1);
        //Так же часто приходится использовать метод View getParent() для "навигации" по вложенным элементам.

        //Получение из сервиса
        //LayoutInflater inflater = (LayoutInflater)getSystemService (Context.LAYOUT_INFLATER_SERVICE);

*/
        lesson41createList();
    }

    //Приложение будет параллельно перебирать три массива данных, создавать для каждой тройки View-элемент из layout-файла item.xml,
    // заполнять его данными и добавлять в вертикальный LinearLayout в main.xml.
    //Для реализации нам понадобятся два layout-файла:
    private void lesson41createList() {
        String[] name = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис", "Костя", "Игорь" };
        String[] position = { "Программер", "Бухгалтер", "Программер", "Программер", "Бухгалтер", "Директор", "Программер", "Охранник" };
        int salary[] = { 13000, 10000, 13000, 13000, 10000, 15000, 13000, 8000 };
        int[] colors = new int[] {Color.parseColor("#559966CC"), Color.parseColor("#55336699")}; //int[] colors = { 0x559966CC, 0x55336699 };
        LinearLayout linLayout1 = (LinearLayout) findViewById(R.id.linLayout123);
        LayoutInflater ltInflater = getLayoutInflater();
        for (int i = 0; i < name.length; i++) {
            Log.d("myLogs", "i = " + i);
            View item = ltInflater.inflate(R.layout.layout_item_l41, linLayout1, false);
            TextView tvName = (TextView) item.findViewById(R.id.tvName);
            tvName.setText(name[i]);
            TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
            tvPosition.setText("Должность: " + position[i]);
            TextView tvSalary = (TextView) item.findViewById(R.id.tvSalary);
            tvSalary.setText("Оклад: " + String.valueOf(salary[i]));
            item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            item.setBackgroundColor(colors[i % 2]);
            linLayout1.addView(item);
        }
    }
}
