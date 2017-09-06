package com.example.oleg.startandroidtests.view;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

import java.util.ArrayList;

//!!!Если приостановленное Activity уничтожается, например, при нехватке памяти или повороте, то соответственно удаляются и все его объекты.

//Для этих целей Activity предоставляет нам для реализации пару методов: первый позволяет сохранить данные – onSaveInstanceState,
// а второй – восстановить - onRestoreInstanceState.
//Эти методы используются в случаях, когда Activity уничтожается, но есть вероятность, что оно еще будет востребовано в своем текущем состоянии.
// Т.е. при нехватке памяти или при повороте экрана. Если же вы просто нажали кнопку Back (назад) и тем самым явно сами закрыли Activity,
// то эти методы не будут выполнены.
//Но даже если не реализовать эти методы, у них есть реализация по умолчанию, которая сохранит и восстановит данные в экранных компонентах.
// Это выполняется для всех экранных компонентов, у которых есть ID.

//Есть еще один полезный механизм сохранения данных. Android дает нам возможность сохранить ссылку
// на какой-либо объект и вернуть ее в новый созданный Activity. Для этого существуют методы:
//onRetainCustomNonConfigurationInstance – в нем мы сохраняем ссылку, передавая ее на выход (return) метода
//getLastCustomNonConfigurationInstance – этот метод ссылку нам возвращает
public class L70onSaveInstanceStateTest extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    //Будем сохранять ссылку на list
    ArrayList<Integer> list;
    //Будем сохранять значение cnt
    int cnt = 0;

    /** Called when the activity is first created. */
    //Кроме метода onRestoreInstanceState, доступ к сохраненным данным также можно получить в методе onCreate.
    // На вход ему подается тот же самый Bundle. Если восстанавливать ничего не нужно, он будет = null.
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l70on_save_instance_state_test);
        Log.d(LOG_TAG, "onCreate");

        //Восстанавливаем ссылку на наш ArrayList, сохраненную методом onRetainCustomNonConfigurationInstance()
        list = (ArrayList<Integer>) getLastCustomNonConfigurationInstance();
        if(list == null) list = new ArrayList<>();
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    //Вызывается перед onResume()
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Восстанавливаем значение счетчика
        cnt = savedInstanceState.getInt("count");
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume ");

        //Пример определения текущей ориентации из кода (не относится к уроку напрямую)
        String orientation = "";
        int or = getResources().getConfiguration().orientation;
        if(or == Configuration.ORIENTATION_PORTRAIT) orientation = "Портрет";
        else if (or == Configuration.ORIENTATION_LANDSCAPE) orientation = "Альбом";
        //Пример определение размеров
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Toast.makeText(this, "Ориентация: " + orientation + "\n" + width + "x" + height, Toast.LENGTH_SHORT).show();
    }

    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    //Вызывается перед onStop()
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Сохраняем значение счетчика в Bundle
        outState.putInt("count", cnt);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    //Вызывается после onStop(), в нем мы сохраняем ссылку, передавая ее на выход (return) метода
    //для последующего восстановления после перерисовки активити, метод getLastCustomNonConfigurationInstance()
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        Log.d(LOG_TAG, "onRetainCustomNonConfigurationInstance");
        //Сохраняем ссылку на наш ArrayList (или любой другой обьект)
        return list;
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    //Обработчики кнопок
    public void onclick1(View v) {
        Log.d(LOG_TAG, "Click1");
        Toast.makeText(this, "Count = " + ++cnt, Toast.LENGTH_SHORT).show();
    }

    public void onclick2(View v) {
        Log.d(LOG_TAG, "Click2");
        list.add(1);
        Toast.makeText(this, list.toString(), Toast.LENGTH_SHORT).show();
    }
}
