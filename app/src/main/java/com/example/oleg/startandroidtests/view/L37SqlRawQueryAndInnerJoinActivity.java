package com.example.oleg.startandroidtests.view;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import com.example.oleg.startandroidtests.R;

public class L37SqlRawQueryAndInnerJoinActivity extends Activity {

    final String LOG_TAG = "myLogs";

    // данные для таблицы должностей
    int[] position_id = { 1, 2, 3, 4 };
    String[] position_name = { "Директор", "Программер", "Бухгалтер", "Охранник" };
    int[] position_salary = { 15000, 13000, 10000, 8000 };

    // данные для таблицы людей
    String[] people_name = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис", "Костя", "Игорь" };
    int[] people_posid = { 2, 3, 2, 2, 3, 1, 2, 4 };

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l37_sql_raw_query_and_inner_join);

        // Подключаемся к БД
        DBHelper dbh = new DBHelper(this);
        //В чем разница между getWritableDatabase и getReadableDatabase? Судя по хелпу, в обычной ситуации оба метода возвращают одно и то же.
        // И оба позволят читать и менять БД. В случае же, например, проблемы отсутствия свободного места на устройстве,
        // метод getReadableDatabase вернет БД только для чтения, а getWritableDatabase выдаст ошибку.
        SQLiteDatabase db = dbh.getWritableDatabase();

        // Описание курсора
        Cursor c;

        // выводим в лог данные по должностям
        Log.d(LOG_TAG, "--- Table position ---");
        c = db.query("position", null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим в лог данные по людям
        Log.d(LOG_TAG, "--- Table people ---");
        c = db.query("people", null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим результат объединения
        // используем rawQuery
        Log.d(LOG_TAG, "--- INNER JOIN with rawQuery---");
        String sqlQuery = "select PL.name as Name, PS.name as Position, salary as Salary "
                + "from people as PL "
                + "inner join position as PS "
                + "on PL.posid = PS.id "
                + "where salary > ?";
        c = db.rawQuery(sqlQuery, new String[] {"12000"});
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим результат объединения
        // используем query
        Log.d(LOG_TAG, "--- INNER JOIN with query---");
        String table = "people as PL inner join position as PS on PL.posid = PS.id";
        String columns[] = { "PL.name as Name", "PS.name as Position", "salary as Salary" };
        String selection = "salary < ?";
        String[] selectionArgs = {"12000"};
        c = db.query(table, columns, selection, selectionArgs, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // закрываем БД
        dbh.close();
    }

    // вывод в лог данных из курсора
    void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, "Cursor is null");
    }

    //Пример SQL транзакции
    //Она используется при работе с данными по принципу «все или ничего». Транзакция при открытии ставит блокировку на базу.
    void myActions() {
        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getWritableDatabase();
        db.beginTransaction();
        try {
            //Сейчас при попытке создать новое подключение к БД  получим SQLiteException error: database is locked, т.к. выполняется транзакция
            //В SDK Android версии старше 2.3.3 появился метод beginTransactionNonExclusive, который ставит блокировку в режиме IMMEDIATE.
            //Я подозреваю, что это позволит читать данные другим подключениям.

            //db.query(.............)
            /*Some code*/

            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            //Если не выполнится метод setTransactionSuccessful(), тогда изменения не будут сохранены
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        } finally {
            //Метод close() класса SQLiteOpenHelper также закрывает и SQLiteDatabase, т.е. отдельно не нужно
            dbh.close();
        }
    }

    // класс для работы с БД
    class DBHelper extends SQLiteOpenHelper {

        //Для избежания ошибок доступа к БД из разных потоков, можно сделать синглтон, как здесь,
        //а еще лучше сделать контент провайдер
//        private static DBHelper hInstance;
//        public static synchronized DBHelper getInstance(Context context) {
//            // Use the application context, which will ensure that you don't accidentally leak an Activity's context.
//            // See this article for more information: http://bit.ly/6LRzfx
//            if (hInstance == null) {
//                hInstance = new DBHelper(context.getApplicationContext());
//            }
//            return hInstance;
//        }

        public DBHelper(Context context) {
            super(context, "l37_sql_test_db", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");

            ContentValues cv = new ContentValues();

            // создаем таблицу должностей
            db.execSQL("create table position ("
                    + "id integer primary key,"
                    + "name text," + "salary integer"
                    + ");");

            // заполняем ее
            for (int i = 0; i < position_id.length; i++) {
                cv.clear();
                cv.put("id", position_id[i]);
                cv.put("name", position_name[i]);
                cv.put("salary", position_salary[i]);
                db.insert("position", null, cv);
            }

            // создаем таблицу людей
            db.execSQL("create table people ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "posid integer"
                    + ");");

            // заполняем ее
            for (int i = 0; i < people_name.length; i++) {
                cv.clear();
                cv.put("name", people_name[i]);
                cv.put("posid", people_posid[i]);
                db.insert("people", null, cv);
            }
        }

        /* Для примера (не из этого урока!)
        * Попробуем воспользоваться методом onUpgrade и посмотреть, как происходит переход на новую версию БД.
        Для этого напишем небольшое приложение, аналогичное одному из приложений с прошлых уроков – про сотрудников и должности.
        Первая версия БД будет содержать только таблицу people с именем сотрудника и его должностью. Но такая таблица будет не совсем корректна.
        Если вдруг у нас изменится название должности, придется обновлять все соответствующие записи в people.
        Поэтому мы решаем изменить БД и организовать данные немного по-другому.

        Во второй версии добавим таблицу position с названием должности и зарплатой.
        И в таблице people вместо названия должности пропишем соответствующий ID из position.

        - создаем и заполняем данными таблицу position
        - добавляем в таблицу people столбец – posid для хранения id из position
        - заполняем people.posid данными из position в зависимости от значения people.position
        - удаляем столбец people.position*/
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            Log.d(LOG_TAG, " --- onUpgrade database from " + oldVersion + " to " + newVersion + " version --- ");
//            if (oldVersion == 1 && newVersion == 2) {   //if (db.needUpgrade(newVersion))
//
//                ContentValues cv = new ContentValues();
//                // данные для таблицы должностей
//                int[] position_id = {1, 2, 3, 4};
//                String[] position_name = {"Директор", "Программер", "Бухгалтер", "Охранник"};
//                int[] position_salary = {15000, 13000, 10000, 8000};
//                db.beginTransaction();
//                try {
//                    // создаем таблицу должностей
//                    db.execSQL("create table position (id integer primary key,name text,salary integer);");
//                    // заполняем ее
//                    for (int i = 0; i < position_id.length; i++) {
//                        cv.clear();
//                        cv.put("id", position_id[i]);
//                        cv.put("name", position_name[i]);
//                        cv.put("salary", position_salary[i]);
//                        db.insert("position", null, cv);
//                    }
//
//                    db.execSQL("alter table people add column posid integer;");
//
//                    for (int i = 0; i < position_id.length; i++) {
//                        cv.clear();
//                        cv.put("posid", position_id[i]);
//                        db.update("people", cv, "position = ?", new String[]{position_name[i]});
//                    }
//
//                    db.execSQL("create temporary table people_tmp (id integer, name text, position text, posid integer);");
//                    db.execSQL("insert into people_tmp select id, name, position, posid from people;");
//                    db.execSQL("drop table people;");
//                    db.execSQL("create table people (id integer primary key autoincrement,name text, posid integer);");
//                    db.execSQL("insert into people select id, name, posid from people_tmp;");
//                    db.execSQL("drop table people_tmp;");
//
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//            }
        }
    }
}
