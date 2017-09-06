package com.example.oleg.startandroidtests.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Чтобы построить такой список нам нужно как-то передать адаптеру данные по группам и элементам.
//Каждая группа представляет из себя Map<String, ?>. Этот Map содержит атрибуты, которые вам нужны для каждой группы.
//Потом все эти Map (группы) собираются в List-коллекцию, например ArrayList. В итоге мы получили упакованные в один объект группы.

//Каждый элемент группы также представлен объектом Map<String, ?>. Мы собираем все Map (элементы) для каждой группы в отдельную коллекцию.
// Получается, каждой группе соответствует коллекция с элементами. Далее эти коллекции мы теперь помещаем в общую коллекцию.
//Т.е. получается подобие двумерного массива. И в итоге пункты упакованы в один объект.

public class L45ExpandableListViewActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    ExpandableListView elvMain;
    AdapterHelper ah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l45_expandable_list_view);

        elvMain = (ExpandableListView) findViewById(R.id.elvMain);
        //Устанавливаем адаптер
        ah = new AdapterHelper(this);
        elvMain.setAdapter(ah.getAdapter());

        // нажатие на элемент
        elvMain.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,   int childPosition, long id) {
                String text =  "onChildClick\ngroupPosition = " + groupPosition + "\nchildPosition = " + childPosition +
                        "\nid = " + id + "\nInfo = " + ah.getGroupChildText(groupPosition, childPosition);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        // нажатие на группу
        elvMain.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                String text = "onGroupClick\ngroupPosition = " + groupPosition + "\nid = " + id;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                // блокируем дальнейшую обработку события для группы с позицией 1 (т.е. не будет раскрываться)
                //if (groupPosition == 1) return true;
                return false;
            }
        });

        // сворачивание группы
        elvMain.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            public void onGroupCollapse(int groupPosition) {
                String text = "onGroupCollapse\ngroupPosition = " + groupPosition;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });

        // разворачивание группы
        elvMain.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                String text = "onGroupExpand\ngroupPosition = " + groupPosition;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });

        // разворачиваем группу с позицией 2
        elvMain.expandGroup(2);
    }
}

//Для простоты кода получение адаптера вынесем во внешний класс
//Это урок 45
class AdapterHelper {
    final String ATTR_GROUP_NAME= "groupName";
    final String ATTR_PHONE_NAME= "phoneName";

    // названия компаний (групп)
    String[] groups = new String[] {"HTC", "Samsung", "LG"};

    // названия телефонов (элементов)
    String[] phonesHTC = new String[] {"Sensation", "Desire", "Wildfire", "Hero"};
    String[] phonesSams = new String[] {"Galaxy S II", "Galaxy Nexus", "Wave"};
    String[] phonesLG = new String[] {"Optimus", "Optimus Link", "Optimus Black", "Optimus One"};

    // коллекция для групп
    ArrayList<Map<String, String>> groupData;

    // коллекция для элементов одной группы
    ArrayList<Map<String, String>> childDataItem;

    // общая коллекция для коллекций элементов
    ArrayList<ArrayList<Map<String, String>>> childData;
    // в итоге получится childData = ArrayList<childDataItem>

    // список аттрибутов группы или элемента
    Map<String, String> m;

    Context ctx;

    AdapterHelper(Context _ctx) {
        ctx = _ctx;
    }

    SimpleExpandableListAdapter adapter;

    SimpleExpandableListAdapter getAdapter() {

        // заполняем коллекцию групп из массива с названиями групп
        groupData = new ArrayList<Map<String, String>>();
        for (String group : groups) {
            // заполняем список аттрибутов для каждой группы
            m = new HashMap<String, String>();
            m.put(ATTR_GROUP_NAME, group); // имя компании
            groupData.add(m);
        }

        // список аттрибутов групп для чтения
        String groupFrom[] = new String[] {ATTR_GROUP_NAME};
        // список ID view-элементов, в которые будет помещены аттрибуты групп
        int groupTo[] = new int[] {android.R.id.text1};


        // создаем коллекцию для коллекций элементов
        childData = new ArrayList<ArrayList<Map<String, String>>>();

        // создаем коллекцию элементов для первой группы
        childDataItem = new ArrayList<Map<String, String>>();
        // заполняем список аттрибутов для каждого элемента
        for (String phone : phonesHTC) {
            m = new HashMap<String, String>();
            m.put(ATTR_PHONE_NAME, phone); // название телефона
            childDataItem.add(m);
        }
        // добавляем в коллекцию коллекций
        childData.add(childDataItem);

        // создаем коллекцию элементов для второй группы
        childDataItem = new ArrayList<Map<String, String>>();
        for (String phone : phonesSams) {
            m = new HashMap<String, String>();
            m.put(ATTR_PHONE_NAME, phone);
            childDataItem.add(m);
        }
        childData.add(childDataItem);

        // создаем коллекцию элементов для третьей группы
        childDataItem = new ArrayList<Map<String, String>>();
        for (String phone : phonesLG) {
            m = new HashMap<String, String>();
            m.put(ATTR_PHONE_NAME, phone);
            childDataItem.add(m);
        }
        childData.add(childDataItem);

        // список аттрибутов элементов для чтения
        String childFrom[] = new String[] {ATTR_PHONE_NAME};
        // список ID view-элементов, в которые будет помещены аттрибуты элементов
        int childTo[] = new int[] {android.R.id.text1};

        //На вход при создании адаптера идут элементы:
        //this – контекст
        //groupData – коллекция групп
        //android.R.layout.simple_expandable_list_item_1 – layout-ресурс, который будет использован для отображения группы в списке. Соответственно, запросто можно использовать свой layout-файл.
        //groupFrom – массив имен атрибутов групп
        //groupTo – массив ID TextView из layout для групп
        //childData – коллекция коллекций элементов по группам
        //android.R.layout.simple_list_item_1 - layout-ресурс, который будет использован для отображения элемента в списке. Можно использовать свой layout-файл
        //childFrom – массив имен атрибутов элементов
        //childTo - массив ID TextView из layout для элементов.
        adapter = new SimpleExpandableListAdapter(
                ctx,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                childData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo);

        return adapter;
    }

    String getGroupText(int groupPos) {
        return ((Map<String,String>)(adapter.getGroup(groupPos))).get(ATTR_GROUP_NAME);
    }

    String getChildText(int groupPos, int childPos) {
        return ((Map<String,String>)(adapter.getChild(groupPos, childPos))).get(ATTR_PHONE_NAME);
    }

    String getGroupChildText(int groupPos, int childPos) {
        return getGroupText(groupPos) + " " +  getChildText(groupPos, childPos);
    }
}
