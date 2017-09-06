package com.example.oleg.startandroidtests;

import android.app.Application;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Oleg on 09.07.2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //загружаем настройки по умолчанию (срабатывает при при первом запуске приложения - переменная false)
        //т.е. считываем поле defValue их xml
        PreferenceManager.setDefaultValues(this, R.xml.l71_pref1, false);
    }
}
