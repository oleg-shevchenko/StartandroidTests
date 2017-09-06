package com.example.oleg.startandroidtests.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;

import com.example.oleg.startandroidtests.R;

//!!!Используются устаревшие методы, лучше пользоваться Loader
public class L53SimpleCursorTreeAdapterActivity extends AppCompatActivity {
    ExpandableListView elvMain;
    DB_L53 db;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_cursor_tree_adapter);

        // подключаемся к БД
        db = new DB_L53(this);
        db.open();

        // готовим данные по группам для адаптера
        Cursor cursor = db.getCompanyData();
        startManagingCursor(cursor);
        // сопоставление данных и View для групп
        String[] groupFrom = { DB_L53.COMPANY_COLUMN_NAME };
        int[] groupTo = { android.R.id.text1 };
        // сопоставление данных и View для элементов
        String[] childFrom = { DB_L53.PHONE_COLUMN_NAME };
        int[] childTo = { android.R.id.text1 };

        // создаем адаптер и настраиваем список
        SimpleCursorTreeAdapter sctAdapter = new MyAdapter(this, cursor,
                android.R.layout.simple_expandable_list_item_1, groupFrom,
                groupTo, android.R.layout.simple_list_item_1, childFrom, childTo);
        elvMain = (ExpandableListView) findViewById(R.id.elvMain);
        elvMain.setAdapter(sctAdapter);
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    class MyAdapter extends SimpleCursorTreeAdapter {

        public MyAdapter(Context context, Cursor cursor, int groupLayout, String[] groupFrom, int[] groupTo, int childLayout, String[] childFrom, int[] childTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo, childLayout, childFrom, childTo);
        }

        protected Cursor getChildrenCursor(Cursor groupCursor) {
            // получаем курсор по элементам для конкретной группы
            int idColumn = groupCursor.getColumnIndex(DB_L53.COMPANY_COLUMN_ID);
            return db.getPhoneData(groupCursor.getInt(idColumn));
        }
    }
}



//Т.к. нам нужна будет БД, выделим под работу с ней отдельный класс
class DB_L53 {

    private static final String DB_NAME = "mydb_l53";
    private static final int DB_VERSION = 1;

    // имя таблицы компаний, поля и запрос создания
    private static final String COMPANY_TABLE = "company";
    public static final String COMPANY_COLUMN_ID = "_id";
    public static final String COMPANY_COLUMN_NAME = "name";
    private static final String COMPANY_TABLE_CREATE = "create table " + COMPANY_TABLE + "(" + COMPANY_COLUMN_ID + " integer primary key, "
            + COMPANY_COLUMN_NAME + " text" + ");";

    // имя таблицы телефонов, поля и запрос создания
    private static final String PHONE_TABLE = "phone";
    public static final String PHONE_COLUMN_ID = "_id";
    public static final String PHONE_COLUMN_NAME = "name";
    public static final String PHONE_COLUMN_COMPANY = "company";
    private static final String PHONE_TABLE_CREATE = "create table "
            + PHONE_TABLE + "(" + PHONE_COLUMN_ID
            + " integer primary key autoincrement, " + PHONE_COLUMN_NAME
            + " text, " + PHONE_COLUMN_COMPANY + " integer" + ");";

    private final Context mCtx;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB_L53(Context ctx) {
        mCtx = ctx;
    }

    // открываем подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрываем подключение
    public void close() {
        if (mDBHelper != null)
            mDBHelper.close();
    }

    // данные по компаниям
    public Cursor getCompanyData() {
        return mDB.query(COMPANY_TABLE, null, null, null, null, null, null);
    }

    // данные по телефонам конкретной группы
    public Cursor getPhoneData(long companyID) {
        return mDB.query(PHONE_TABLE, null, PHONE_COLUMN_COMPANY + " = " + companyID, null, null, null, null);
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            ContentValues cv = new ContentValues();
            // названия компаний (групп)
            String[] companies = new String[] { "HTC", "Samsung", "LG" };
            // создаем и заполняем таблицу компаний
            db.execSQL(COMPANY_TABLE_CREATE);
            for (int i = 0; i < companies.length; i++) {
                cv.put(COMPANY_COLUMN_ID, i + 1);
                cv.put(COMPANY_COLUMN_NAME, companies[i]);
                db.insert(COMPANY_TABLE, null, cv);
            }

            // названия телефонов (элементов)
            String[] phonesHTC = new String[] { "Sensation", "Desire", "Wildfire", "Hero" };
            String[] phonesSams = new String[] { "Galaxy S II", "Galaxy Nexus", "Wave" };
            String[] phonesLG = new String[] { "Optimus", "Optimus Link", "Optimus Black", "Optimus One" };

            // создаем и заполняем таблицу телефонов
            db.execSQL(PHONE_TABLE_CREATE);
            cv.clear();
            for (int i = 0; i < phonesHTC.length; i++) {
                cv.put(PHONE_COLUMN_COMPANY, 1);
                cv.put(PHONE_COLUMN_NAME, phonesHTC[i]);
                db.insert(PHONE_TABLE, null, cv);
            }
            for (int i = 0; i < phonesSams.length; i++) {
                cv.put(PHONE_COLUMN_COMPANY, 2);
                cv.put(PHONE_COLUMN_NAME, phonesSams[i]);
                db.insert(PHONE_TABLE, null, cv);
            }
            for (int i = 0; i < phonesLG.length; i++) {
                cv.put(PHONE_COLUMN_COMPANY, 3);
                cv.put(PHONE_COLUMN_NAME, phonesLG[i]);
                db.insert(PHONE_TABLE, null, cv);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

}
