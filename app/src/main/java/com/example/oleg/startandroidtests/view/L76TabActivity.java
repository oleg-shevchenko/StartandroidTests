package com.example.oleg.startandroidtests.view;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

//!!!Наследоваться от TabActivity нужно лишь если используется случай, как в четвертой вкладке, т.е. активити как вкладка
//иначе просто Activity, к тому же TabActivity deprecated, т.е. лучше использовать TabLayout c PagerAdapter (смотреть в matherial_tests)
public class L76TabActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l76_tab);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        //вызываем обязательный метод setup. Это первичная инициализация.
        tabHost.setup();

        //Первая вкладка
        TabHost.TabSpec tabSpec = null;
        // создаем вкладку и указываем тег – это просто некий строковый идентификатор вкладки
        tabSpec = tabHost.newTabSpec("tag1");
        // название вкладки
        tabSpec.setIndicator("Вкладка 1");
        // указываем id компонента из FrameLayout, он и станет содержимым
        tabSpec.setContent(R.id.tvTab1);
        // добавляем в корневой элемент
        tabHost.addTab(tabSpec);

        //Вторая вкладка
        tabSpec = tabHost.newTabSpec("tag2");
        // указываем название и картинку, в нашем случае вместо картинки идет xml-файл, который определяет картинку по состоянию вкладки
        //!!! Почему-то не работает картинка
        tabSpec.setIndicator("Вкладка 2", getResources().getDrawable(R.drawable.tab_icon_selector));
        tabSpec.setContent(R.id.tvTab2);
        tabHost.addTab(tabSpec);

        //Третья вкладка (кастомный хедер)
        tabSpec = tabHost.newTabSpec("tag3");
        //создаем View из layout-файла и устанавливаем его, как заголовок
        tabSpec.setIndicator(getLayoutInflater().inflate(R.layout.l76_tab_header, null));
        tabSpec.setContent(R.id.tvTab3);
        tabHost.addTab(tabSpec);

        //Четвертая вкладка (TabActivity), т.е. отдельная активити отображается во вкладке (TabActivity is deprecated)
        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator("Вкладка 4");
        //Здесь указываем интент с нужной активити
        tabSpec.setContent(new Intent(this, L77ActivityAsTab.class));
        //Для инфо: А как отрабатывают события Activity LifeCycle? При первом показе первой вкладки срабатывают три метода OneActivity: onCreate, onStart, onResume.
        //Переключаемся на вторую вкладку – срабатывает onPause в OneActivity, а потом три метода TwoActivity: onCreate, onStart, onResume.
        //И далее при переключениях между вкладками одна уходит в onPause, другая возвращается в onResume.
        tabHost.addTab(tabSpec);

        //Устанавливаем (setCurrentTabByTag) вторую в качестве выбранной по умолчанию.
        tabHost.setCurrentTabByTag("tag2");

        // обработчик переключения вкладок
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                Toast.makeText(getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Также можно использовать TabHost.TabContentFactory, который по тегу будет делать View для вкладки
    //В этом случае используем tabSpec.setContent(tabFactory) для каждой вкладки

//    TabHost.TabContentFactory tabFactory = new TabHost.TabContentFactory() {
//        @Override
//        public View createTabContent(String tag) {
//            if (tag.equals("tag1")) {
//                return getLayoutInflater().inflate(R.layout.test_layout, null);
//            } else if (tag.equals("tag2")) {
//                TextView tv = new TextView(L76TabActivity.this);
//                tv.setText("Это создано вручную");
//                return tv;
//            }
//            return null;
//        }
//    };
}
