package com.example.oleg.startandroidtests.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

public class L34SqlTetsActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    final String sqlTableName = "mytable";

    Button btnAdd, btnRead, btnClear, btnUpd, btnDel;
    EditText etName, etEmail, etID;
    TextView tvLog;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l34_sql_tets);

        tvLog = (TextView) findViewById(R.id.tvLog);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnUpd = (Button) findViewById(R.id.btnUpd);
        btnUpd.setOnClickListener(this);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etID = (EditText) findViewById(R.id.etID);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        //подключаемся к БД и получаем объект SQLiteDatabase, который содержит методы для работы с данными – т.е. вставка, обновление, удаление и чтение.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Класс ContentValues используется для указания полей таблицы и значений, которые мы в эти поля будем вставлять (типа Map)
        ContentValues cv = new ContentValues();
        // получаем данные из полей ввода
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String id = etID.getText().toString();

        switch (v.getId()) {
            case R.id.btnAdd:
                // подготовим данные для вставки в виде пар: наименование столбца - значение
                cv.put("name", name);
                cv.put("email", email);
                // вставляем запись и получаем ее ID, (второй аргумент метода используется, при вставке в таблицу пустой строки, пока не нужно)
                long rowID = db.insert(sqlTableName, null, cv);
                tvLog.setText("--- Insert in mytable: ---\nrow inserted, ID = " + rowID);
                break;
            case R.id.btnRead:
                String logText = "--- Rows in mytable: ---\n";
                //делаем запрос всех данных из таблицы mytable, получаем Cursor, его можно рассматривать как таблицу с данными
                //Для чтения используется метод query. На вход ему подается имя таблицы, список запрашиваемых полей, условия выборки, группировка, сортировка.
                Cursor c = db.query(sqlTableName, null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки, если в выборке нет строк, вернется false
                if (c.moveToFirst()) {
                    //получаем порядковые номера столбцов в Cursor по их именам
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int emailColIndex = c.getColumnIndex("email");
                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        logText = logText + "ID = " + c.getInt(idColIndex) +
                                        ", name = " + c.getString(nameColIndex) +
                                        ", email = " + c.getString(emailColIndex) + "\n";
                    // переход на следующую строку, а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    logText = logText +  "0 rows";
                //В конце закрываем курсор (освобождаем занимаемые им ресурсы) методом close, т.к. далее мы его нигде не используем.
                tvLog.setText(logText);
                c.close();
                break;
            case R.id.btnClear:
                //удаляем все записи, на вход передаем имя таблицы и null в качестве условий для удаления,
                //а значит удалится все. Метод возвращает кол-во удаленных записей.
                int clearCount = db.delete(sqlTableName, null, null);
                tvLog.setText("--- Clear mytable: ---\ndeleted rows count = " + clearCount);
                break;
            case R.id.btnUpd:
                if (TextUtils.isEmpty(id)) break;
                // подготовим значения для обновления
                cv.put("name", name);
                cv.put("email", email);
                // обновляем по id: указываем таблицу, values, строка условия, массив аргументов для строки условия
                //Если знаков ? в строке условия несколько, то им будут сопоставлены значения из массива по порядку
                //пример myDatabase.update(DATABASE_TABLE, murzikValues, "NAME = ? OR DESCRIPTION = ?", new String[] {"Murzik", "Nice"});
                int updCount = db.update("mytable", cv, "id = ?", new String[] { id });
                tvLog.setText("--- Update mytable: ---\nupdated rows count = " + updCount);
                break;
            case R.id.btnDel:
                if (TextUtils.isEmpty(id)) break;
                // удаляем по id, для delete вставил значение аргумента сразу в строку условия, как вариант
                // этот способ не защищает от SQL injection
                int delCount = db.delete("mytable", "id = " + id, null);
                tvLog.setText("--- Delete from mytable: ---\ndeleted rows count = " + delCount);
                break;
        }
        //После этого закрываем соединение с БД методом close.
        dbHelper.close();
    }

    //класс должен наследовать класс SQLiteOpenHelper и переопределять методы onCreate и onUpgrade
    //нужен для получения обьекта SQLiteDatabase
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            //конструктор суперкласса
            //context - контекст, mydb - название базы данных, null – объект для работы с курсорами, нам пока не нужен, поэтому null, 1 – версия базы данных
            super(context, "l34_sql_test_db", null, 1);
        }

        //этот метод вызывается, если БД не существует и ее надо создавать
        @Override
        public void onCreate(SQLiteDatabase db) {
            tvLog.setText("--- onCreate database ---");
            //выполнения SQL-запроса, создаем таблицу mytable с полями id, name и email.
            db.execSQL("create table " + sqlTableName + " ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text" + ");");
        }

        //onUpgrade пока не заполняем, т.к. используем одну версию БД и менять ее не планируем.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
