package com.example.oleg.startandroidtests.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.oleg.startandroidtests.R;

/**
 * Created by Oleg on 30.07.2017.
 */

public class L136DB {
    //Название БД
    private static final String DB_NAME = "mydb_l136";
    //Версия (при увеличении будет вызван метод onUpgrade()
    private static final int DB_VERSION = 1;
    //Таблица в БД
    private static final String DB_TABLE = "mytab";

    //Колонки в таблице (для корректной работы с курсором для ID следует использовать название _id)
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IMG = "img";
    public static final String COLUMN_TXT = "txt";

    private final Context mCtx;

    //Наш внутренний класс, наследник SQLiteOpenHelper, для создания и апгрейда БД и получения обьекта SQLiteDatabase
    private DBHelper mDBHelper;

    //Обьект, с помощью которого происходят операции с БД, получаем с помощью DBHelper
    private SQLiteDatabase mDB;

    //Конструктор, передаем контекст
    public L136DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    //получить все данные из таблици
    public Cursor getAllData() {
        if(mDB != null) {
            return mDB.query(DB_TABLE, null, null, null, null, null, null);
        } else {
            Log.d("L136DB", "No opened DB. Return null");
            return null;
        }
    }

    // добавить запись в DB_TABLE
    public void addRec(String txt, int img) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TXT, txt);
        cv.put(COLUMN_IMG, img);
        mDB.insert(DB_TABLE, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        //SQL запрос для создания нашей таблици, который будем использовать в DBHelper extends SQLiteOpenHelper
        private static final String DB_CREATE = "create table " + DB_TABLE + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_IMG + " integer, " + COLUMN_TXT + " text);";

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            //Создаем таблицу
            db.execSQL(DB_CREATE);
            //Заполняем какими-то данными для примера
            ContentValues cv = new ContentValues();
            for (int i = 0; i < 5; i++) {
                cv.put(COLUMN_TXT, "sometext " + i);
                cv.put(COLUMN_IMG, R.drawable.arrow_left);
                db.insert(DB_TABLE, null, cv);
            }
        }

        //Выполняется при увеличении номера версии БД
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
