package com.example.oleg.startandroidtests.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

//Нужно прописать класс провайдера в манифесте. Делается это аналогично, как мы прописываем Activity или сервис.
//Только из списка выбираем Provider. В Name выбираем наш класс. И заполняем поле Authorities,
//сюда необходимо прописать значение из константы AUTHORITY - com.example.oleg.startandroidtests.contentprovider.AdressBook.

public class MyContactsProvider extends ContentProvider {

    final String LOG_TAG = "myLogs";

    //Константы для БД
    static final String DB_NAME = "mydb_l101";
    static final int DB_VERSION = 1;

    // Таблица
    static final String CONTACT_TABLE = "contacts";

    // Поля
    static final String CONTACT_ID = "_id";
    static final String CONTACT_NAME = "name";
    static final String CONTACT_EMAIL = "email";

    // // Uri
    //константы AUTHORITY и CONTACT_PATH – это составные части Uri. Мы это уже обсуждали в начале урока.
    //Из этих двух констант и префикса content:// мы формируем общий Uri - CONTACT_CONTENT_URI.
    //Т.к. здесь не указан никакой ID, этот Uri дает доступ ко всем контактам.
    static final String AUTHORITY = "com.example.oleg.startandroidtests.contentprovider.AdressBook";
    static final String CONTACT_PATH = "contacts";
    public static final Uri CONTACT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTACT_PATH);

    //Далее описываем MIME-типы данных, предоставляемых провайдером. Один для набора данных, другой для конкретной записи.
    //У меня пока что мало опыта работы с провайдерами, и я не очень понимаю, где и как можно эти типы данных использовать.
    //Но реализовать их надо, поэтому делаем это. Мы будем возвращать их в методе getType нашего провайдера.
    // набор строк
    static final String CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTACT_PATH;
    // одна строка
    static final String CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CONTACT_PATH;

    //Далее создаем и описываем UriMatcher и константы для него. UriMatcher – это что-то типа парсера.
    //В методе addURI мы даем ему комбинацию: authority, path и константа.
    //Причем, мы можем использовать спецсимволы: * - строка любых символов любой длины, # - строка цифр любой длины.
    //На вход провайдеру будут поступать Uri, и мы будем сдавать их в UriMatcher на проверку.
    //Если Uri будет подходить под комбинацию authority и path, ранее добавленных в addURI,
    //то UriMatcher вернет константу из того же набора: authority, path, константа.
    //      общий Uri
    static final int URI_CONTACTS_CODE = 1;
    //      Uri с указанным ID
    static final int URI_CONTACTS_ID_CODE = 2;
    //описание и создание UriMatcher
    //С помощью UriMatcher мы получаем код Uri, который передан, и потом делаем соответствующие действия
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_CONTACTS_CODE);
        //# - это маска для строки из цифр. Т.е. если у нас к path добавляется число, это значит - нам дают ID и мы будем работать с конкретной записью.
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH + "/#", URI_CONTACTS_ID_CODE);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "MyContactsProvider onCreate()");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    //В методе query мы получаем на вход Uri и набор параметров для выборки из БД:
    //projection - столбцы, selection - условие, selectionArgs – аргументы для условия, sortOrder – сортировка. Опять же, эти параметры уже знакомы нам по работе с БД.
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        //Далее мы отдаем uri в метод match объекта uriMatcher. Он его разбирает, сверяет с теми комбинациями authority/path,
        //которые мы давали ему в методах addURI и выдает константу из соответствующей комбинации.
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS_CODE: // общий Uri
                Log.d(LOG_TAG, "URI_CONTACTS_CODE");
                // если сортировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CONTACT_NAME + " ASC";
                }
                break;
            case URI_CONTACTS_ID_CODE: // Uri с ID
                //извлекаем ID из Uri методом getLastPathSegment и добавляем его в условие selection.
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID_CODE, " + id);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = CONTACT_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CONTACT_ID + " = " + id;
                }
                break;
            default:
                //Если uriMatcher не смог опознать Uri, то будем выдавать IllegalArgumentException. Вы, разумеется, можете тут прописать свое решение этой проблемы.
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        //Далее мы получаем БД и выполняем для нее метод query, получаем cursor.
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CONTACT_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        //просим ContentResolver уведомлять этот курсор об изменениях данных в CONTACT_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(), CONTACT_CONTENT_URI);
        return cursor;
    }

    //В методе getType возвращаем типы соответственно типу Uri – общий или с ID.
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS_CODE:
                return CONTACT_CONTENT_TYPE;
            case URI_CONTACTS_ID_CODE:
                return CONTACT_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    //В insert мы проверяем, что нам пришел наш общий Uri. Если все ок, то вставляем данные в таблицу, получаем ID.
    //Этот ID мы добавляем к общему Uri и получаем Uri с ID. По идее, это можно сделать и обычным сложением строк,
    //но рекомендуется использовать метод withAppendedId объекта. Далее мы уведомляем систему, что поменяли данные, соответствующие resultUri.
    //Система посмотрит, не зарегистрировано ли слушателей на этот Uri. Увидит, что мы регистрировали курсор, и даст ему знать, что данные обновились.
    //В конце мы возвращаем resultUri, соответствующий новой добавленной записи.
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_CONTACTS_CODE)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(CONTACT_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    //В delete мы проверяем, какой Uri нам пришел. Если с ID, то фиксим selection – добавляем туда условие по ID.
    //Выполняем удаление в БД, получаем кол-во удаленных записей. Уведомляем, что данные изменились. Возвращаем кол-во удаленных записей.
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS_CODE:
                Log.d(LOG_TAG, "URI_CONTACTS");
                break;
            case URI_CONTACTS_ID_CODE:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = CONTACT_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CONTACT_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(CONTACT_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    //В update мы проверяем, какой Uri нам пришел. Если с ID, то фиксим selection – добавляем туда условие по ID.
    //Выполняем обновление в БД, получаем кол-во обновленных записей. Уведомляем, что данные изменились. Возвращаем кол-во обновленных записей.
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS_CODE:
                Log.d(LOG_TAG, "URI_CONTACTS");

                break;
            case URI_CONTACTS_ID_CODE:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = CONTACT_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CONTACT_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(CONTACT_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    private class DBHelper extends SQLiteOpenHelper {

        // Скрипт создания таблицы
        static final String DB_CREATE = "create table " + CONTACT_TABLE + "(" + CONTACT_ID + " integer primary key autoincrement, "
                + CONTACT_NAME + " text, " + CONTACT_EMAIL + " text" + ");";

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            ContentValues cv = new ContentValues();
            for (int i = 1; i <= 3; i++) {
                cv.put(CONTACT_NAME, "name " + i);
                cv.put(CONTACT_EMAIL, "email " + i);
                db.insert(CONTACT_TABLE, null, cv);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
