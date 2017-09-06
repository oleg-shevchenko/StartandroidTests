package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

public class L14MenuActivityCode extends AppCompatActivity {

    // Элементы экрана
    TextView tv;
    CheckBox chb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l14_menu_code);

        // находим элементы
        tv = (TextView) findViewById(R.id.tvL14);
        chb = (CheckBox) findViewById(R.id.chbExtMenu);
    }

    //вызывается только при первом показе меню. Создает меню и более не используется. Здесь мы добавляем к меню пункты.
//    Первый параметр – ID группы. В первых трех пунктах он равен нулю, в оставшихся трех – 1. Т.е. пункты меню copy, paste и exit объединены в группу с ID = 1. Визуально это никак не проявляется - они не отличаются цветом или еще чем-либо. ID группы мы будем использовать в реализации onPrepareOptionsMenu.
//    Второй параметр – ID пункта меню. В обработчике используется для определения какой пункт меню был нажат. Будем использовать его в onOptionsItemSelected.
//    Третий параметр – определяет позицию пункта меню. Этот параметр используется для определения порядка пунктов при отображении меню. Используется сортировка по возрастанию, т.е. от меньшего order к большему.
//    Четвертый параметр – текст, который будет отображаться на пункте меню. Тут все понятно.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // добавляем пункты меню
        menu.add(0, 1, 0, "add");
        menu.add(0, 2, 5, "edit");
        menu.add(0, 3, 3, "delete");
        menu.add(1, 4, 1, "copy");
        menu.add(1, 5, 2, "paste");
        menu.add(1, 6, 4, "exit");
        return super.onCreateOptionsMenu(menu);
    }

    //вызывается каждый раз перед отображением меню. Здесь мы вносим изменения в уже созданное меню, если это необходимо
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // пункты меню с ID группы = 1 видны, если в CheckBox стоит галка
        menu.setGroupVisible(1, chb.isChecked());
        return super.onPrepareOptionsMenu(menu);
    }

    //вызывается при нажатии пункта меню. Здесь мы определяем какой пункт меню был нажат.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();

        // Выведем в TextView информацию о нажатом пункте меню
        sb.append("Item Menu");
        sb.append("\r\n groupId: " + String.valueOf(item.getGroupId()));
        sb.append("\r\n itemId: " + String.valueOf(item.getItemId()));
        sb.append("\r\n order: " + String.valueOf(item.getOrder()));
        sb.append("\r\n title: " + item.getTitle());
        tv.setText(sb.toString());

        return super.onOptionsItemSelected(item);
    }
}
