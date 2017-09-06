package com.example.oleg.startandroidtests.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.oleg.startandroidtests.R;

public class L36SqlTestActivity extends AppCompatActivity implements View.OnClickListener {
    final String LOG_TAG = "myLogs";

    String name[] = { "Китай", "США", "Бразилия", "Россия", "Япония", "Германия", "Египет", "Италия", "Франция", "Канада" };
    int people[] = { 1400, 311, 195, 142, 128, 82, 80, 60, 66, 35 };
    String region[] = { "Азия", "Америка", "Америка", "Европа", "Азия", "Европа", "Африка", "Европа", "Европа", "Америка" };

    Button btnAll, btnFunc, btnPeople, btnSort, btnGroup, btnHaving;
    EditText etFunc, etPeople, etRegionPeople;
    RadioGroup rgSort;

    DBHelper dbHelper;
    SQLiteDatabase db;

    /** Called when the activity is first created. */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l36_sql_test);

        btnAll = (Button) findViewById(R.id.btnAll);
        btnAll.setOnClickListener(this);

        btnFunc = (Button) findViewById(R.id.btnFunc);
        btnFunc.setOnClickListener(this);

        btnPeople = (Button) findViewById(R.id.btnPeople);
        btnPeople.setOnClickListener(this);

        btnSort = (Button) findViewById(R.id.btnSort);
        btnSort.setOnClickListener(this);

        btnGroup = (Button) findViewById(R.id.btnGroup);
        btnGroup.setOnClickListener(this);

        btnHaving = (Button) findViewById(R.id.btnHaving);
        btnHaving.setOnClickListener(this);

        etFunc = (EditText) findViewById(R.id.etFunc);
        etPeople = (EditText) findViewById(R.id.etPeople);
        etRegionPeople = (EditText) findViewById(R.id.etRegionPeople);

        rgSort = (RadioGroup) findViewById(R.id.rgSort);

        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();

        // проверка существования записей
        Cursor c = db.query("l36_table", null, null, null, null, null, null);
        if (c.getCount() == 0) {
            ContentValues cv = new ContentValues();
            // заполним таблицу
            for (int i = 0; i < 10; i++) {
                cv.put("name", name[i]);
                cv.put("people", people[i]);
                cv.put("region", region[i]);
                Log.d(LOG_TAG, "id = " + db.insert("l36_table", null, cv));
            }
        }
        c.close();
        dbHelper.close();
        // эмулируем нажатие кнопки btnAll
        onClick(btnAll);

    }

    public void onClick(View v) {

        // подключаемся к базе
        db = dbHelper.getWritableDatabase();

        // данные с экрана
        String sFunc = etFunc.getText().toString();
        String sPeople = etPeople.getText().toString();
        String sRegionPeople = etRegionPeople.getText().toString();

        // переменные для query
        String[] columns = null;
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        // курсор
        Cursor c = null;


/*В метод query() передают семь параметров. Если какой-то параметр для запроса вас не интересует, то оставляете null:
table — имя таблицы, к которой передается запрос;
String[] columnNames — список имен возвращаемых полей (массив). При передаче null возвращаются все столбцы;
String whereClause — параметр, формирующий выражение WHERE (исключая сам оператор WHERE). Значение null возвращает все строки. Например: _id = 19 and summary = ?
String[] selectionArgs — значения аргументов фильтра. Вы можете включить ? в "whereClause"". Подставляется в запрос из заданного массива по порядку;
String groupBy - фильтр для группировки, формирующий выражение GROUP BY (исключая сам оператор GROUP BY). Если GROUP BY не нужен, передается null;
String having — фильтр для группировки, формирующий выражение HAVING (исключая сам оператор HAVING). Если не нужен, передается null;
String orderBy — параметр, формирующий выражение ORDER BY (исключая сам оператор ORDER BY). При сортировке по умолчанию передается null.*/

/*Cursor cursor = db.query("CAT", new String[] {"NAME", "DESCRIPTION"}, "NAME = ? OR DESCRIPTION = ?", new String[] {"Murzik", "Nice"}, null, null, null);*/
//http://developer.alexanderklimov.ru/android/sqlite/android-sqlite.php

        // определяем нажатую кнопку
        switch (v.getId()) {
            // Все записи
            case R.id.btnAll:
                Log.d(LOG_TAG, "--- Все записи ---");
                c = db.query("l36_table", null, null, null, null, null, null);
                break;
            // Функция
            case R.id.btnFunc:
                Log.d(LOG_TAG, "--- Функция " + sFunc + " ---");
                columns = new String[] { sFunc };
                //Вы можете использовать функции SQL для составления запросов: AVG(), COUNT(), SUM(), MAX(), MIN() и др.
                //пример: COUNT(id) as mycounter (получим курсор со столбцом mycounter и колличеством заполненых? строк, в данном случае id всегда есть)
                //пример: SUM(people) as allpeople (колличество всех людей)
                //пример: AVG(people) as avgpeople (среднее колличество людей)
                c = db.query("l36_table", columns, null, null, null, null, null);
                break;
            // Население больше, чем
            case R.id.btnPeople:
                Log.d(LOG_TAG, "--- Население больше " + sPeople + " ---");
                selection = "people > ?";
                selectionArgs = new String[] { sPeople };
                c = db.query("l36_table", null, selection, selectionArgs, null, null, null);
                break;
            // Население по региону
            case R.id.btnGroup:
                Log.d(LOG_TAG, "--- Население по региону ---");
                columns = new String[] { "region", "sum(people) as people" };
                groupBy = "region";
                c = db.query("l36_table", columns, null, null, groupBy, null, null);
                break;
            // Население по региону больше чем
            case R.id.btnHaving:
                Log.d(LOG_TAG, "--- Регионы с населением больше " + sRegionPeople + " ---");
                columns = new String[] { "region", "sum(people) as people" };
                groupBy = "region";
                having = "sum(people) > " + sRegionPeople;
                c = db.query("l36_table", columns, null, null, groupBy, having, null);
                break;
            // Сортировка
            case R.id.btnSort:
                // сортировка по
                switch (rgSort.getCheckedRadioButtonId()) {
                    // наименование
                    case R.id.rName:
                        Log.d(LOG_TAG, "--- Сортировка по наименованию ---");
                        orderBy = "name";
                        break;
                    // население
                    case R.id.rPeople:
                        Log.d(LOG_TAG, "--- Сортировка по населению ---");
                        orderBy = "people";
                        break;
                    // регион
                    case R.id.rRegion:
                        Log.d(LOG_TAG, "--- Сортировка по региону ---");
                        orderBy = "region";
                        break;
                }
                //Для сортировки используется последний параметр. Нужно указать нужный столбец,
                //по которому будет проводиться сортировка. По алфавиту с буквы A - ASC, наоборот - DESC
                //пример Cursor cursor = db.query("CAT", new String[] {"_id", "NAME", "AGE"}, null, null, null, null,"NAME ASC");
                //по нескольким столбцам Cursor cursor = db.query("CAT",new String[] {"_id", "NAME", "DESCRIPTION"}, null, null, null, null, "DESCRIPTION DESC, NAME");
                c = db.query("l36_table", null, null, null, null, null, orderBy);
                break;
        }

        /* У метода query есть еще два параметра
        limit – строковый параметр, указывается в формате [offset], rows.
        Т.е. если в query в качестве limit передать строку "5" - то запрос выдаст только пять первых записей.
        Если же передать "3,5", то запрос выдаст пять записей, начиная с четвертой (НЕ с третьей).
        distinct – это boolean-параметр, удаление дубликатов. Может быть true или false.*/

        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    //Получаем массив имен колонок
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
            c.close();
        } else Log.d(LOG_TAG, "Cursor is null");
        dbHelper.close();
    }

    class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "l34_sql_test_db", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table l36_table ("
                    + "id integer primary key autoincrement," + "name text,"
                    + "people integer," + "region text" + ");");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
