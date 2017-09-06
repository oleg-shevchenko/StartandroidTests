package com.example.oleg.startandroidtests.view;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

//Используем
//compile 'com.android.support:design:25.3.1'
//CoordinatorLayout

public class SnackbarTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackbar_test);
        findViewById(R.id.fab).setOnClickListener(new SnackListener());
    }

    Snackbar snackbar;

    public void showSnack(View v) {
        snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Snackbar 5 seconds", Snackbar.LENGTH_SHORT)
            .setAction("HIDE", new SnackListener()) //Показать кнопку действия и задать ее слушатель
            .setDuration(5000)                      //Установить свою продолжительность
            .setActionTextColor(Color.DKGRAY);  //Цвет текста кнопки действия

        //Установить цвет или ресурс бекграунда можно, получив View
        View vSnack = snackbar.getView();
        vSnack.setBackgroundColor(Color.GREEN);

        //Установить цвет текста
        TextView snackTextView = (TextView) vSnack.findViewById(android.support.design.R.id.snackbar_text);
        snackTextView.setTextColor(Color.RED);


        snackbar.show();
    }

    public void showSnack2(View v) {
        //Создаем snackbar с неопределенным временем показа, чтобы его убрать нужно или сделать свайп или использовать метод dismiss()
        snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Snackbar indefinite time, press againe to hide this message", Snackbar.LENGTH_INDEFINITE);

        //Отслеживать события появления и исчезновения Snackbar можно с помощью методов обратного вызова через setCallback().
        //Параметр event у метода onDismissed() позволяет узнать конкретное событие, повлекшее исчезновение,
        //при помощи констант DISMISS_EVENT_SWIPE, DISMISS_EVENT_ACTION, DISMISS_EVENT_TIMEOUT, DISMISS_EVENT_MANUAL, DISMISS_EVENT_CONSECUTIVE.
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                // do some action on dismiss
                Toast.makeText(getApplicationContext(), "Dismiss. Reason: " + event, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onShown(Snackbar snackbar) {
                // do some action when snackbar is showed
                Toast.makeText(getApplicationContext(), "Snackbar is showed", Toast.LENGTH_SHORT).show();
            }
        });

        snackbar.show();
    }

    public class SnackListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();     //Метод для скрытия сообщения
            } else {
                showSnack2(v);
            }
        }
    }


}
