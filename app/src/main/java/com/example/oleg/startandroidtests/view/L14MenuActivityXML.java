package com.example.oleg.startandroidtests.view;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

public class L14MenuActivityXML extends AppCompatActivity {

    TextView tv;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l14_menu_xml);
        tv = (TextView) findViewById(R.id.tvL14);
    }

//    С помощью метода getMenuInflater мы получаем MenuInflater и вызываем его метод inflate.
//    На вход передаем наш файл mymenu.xml из папки res/menu и объект menu. MenuInflater берет объект
//    menu и наполняет его пунктами согласно файлу mymenu.xml.
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.l14_menu, menu);

        //Следующий блок нужен для отображения иконок в меню
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        //--------------------------------------------------

        //Обработка раскрывающегося поиска
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }

    //вызывается при нажатии пункта меню. Здесь мы определяем какой пункт меню был нажат.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
