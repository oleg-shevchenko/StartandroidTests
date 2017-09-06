package com.example.oleg.startandroidtests.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.example.oleg.startandroidtests.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//!!!!!!!!!!!В этом уроке используются deprecated методы, рекомендуется использование DialogFragment!!!!!!!!!!!!!
public class L59DialogTestActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout root;
    private final int TIME_DIALOG_ID = 1;
    private final int DATE_DIALOG_ID = 2;
    private final int ALERT_DIALOG_ID = 3;
    private final int ALERT_DIALOG_TIME_ID = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l59_dialog_test);
        root = (LinearLayout) findViewById(R.id.l59_root);

        findViewById(R.id.tvTime).setOnClickListener(this);
        findViewById(R.id.tvDate).setOnClickListener(this);
        findViewById(R.id.tvAlert).setOnClickListener(this);
        findViewById(R.id.tvAlert1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTime:
                showDialog(TIME_DIALOG_ID);
                break;
            case R.id.tvDate:
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.tvAlert:
                showDialog(ALERT_DIALOG_ID);
                break;
            case R.id.tvAlert1:
                showDialog(ALERT_DIALOG_TIME_ID);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == TIME_DIALOG_ID) {
            return createTimeDialog();
        }
        else if(id == DATE_DIALOG_ID) {
            return createDateDialog();
        }
        else if(id == ALERT_DIALOG_ID) {
            return createAlertDialog();
        }
        else if(id == ALERT_DIALOG_TIME_ID) {
            return createAlertDialogTime();
        }
        return super.onCreateDialog(id);
    }

    //**********************************************************************************************
    private Dialog createTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                showSnack("Time: " + hourOfDay + ":" + minute);
            }
        }, 12, 5, true);
        return dialog;
    }

    //**********************************************************************************************
    private Dialog createDateDialog() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                showSnack("Date: " + dayOfMonth + "." + (month + 1) + "." + year);
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        return dialog;
    }

    //*********************************************************************************************
    //Alert Dialog. Добавление кнопок, заголовка и т.д. опциональны
    private Dialog createAlertDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        // заголовок
        adb.setTitle("My Alert Dialog");
        // сообщение
        adb.setMessage("My dialog message");
        // иконка
        adb.setIcon(android.R.drawable.ic_dialog_info);
        // кнопка положительного ответа
        adb.setPositiveButton("Ok", alertClickListener);
        // кнопка отрицательного ответа
        adb.setNegativeButton("No", alertClickListener);
        // кнопка нейтрального ответа
        adb.setNeutralButton("Cancel", alertClickListener);
        // отмена возможности закрытия диалога кнопкой назад
        adb.setCancelable(false);
        // создаем диалог
        return adb.create();
    }

    //Слушатель нажатий кнопок алерт диалога
    DialogInterface.OnClickListener alertClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    showSnack("Alert: Ok");
                    break;
                // негативная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    showSnack("Alert: No");
                    break;
                // нейтральная кнопка
                case Dialog.BUTTON_NEUTRAL:
                    showSnack("Alert: Cancel");
                    break;
            }
        }
    };

    //**********************************************************************************************
    //Когда мы создаем диалог в методе onCreateDialog, Activity складывает его в кучу созданных диалогов.
    //И когда надо отобразить, достает его и показывает. Т.е. метод onCreateDialog выполняется только один раз для диалога.
    //И если вам надо перед отображением что-то изменить, надо использовать метод onPrepareDialog. Этот метод вызывается каждый раз перед показом диалога.
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private Dialog createAlertDialogTime() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Текущее время");
        adb.setMessage(sdf.format(new Date(System.currentTimeMillis())));
        return adb.create();
    }

    //В onPrepareDialog мы на вход получаем ID вызываемого диалога и сам диалог (Dialog). Мы преобразуем его к AlertDialog и пишем в текст диалога текущее время.
    //Если вы при создании в onCreateDialog() не задали, например, Message, то в созданном диалоге будет скрыта View (setVisibility(View.GONE)),
    //которая отвечает за отображение текста Message. И если в методе подготовки диалога вы решите-таки Message указать,
    //то диалог его просто не отобразит, т.к. структура задается при создании.
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        if (id == ALERT_DIALOG_TIME_ID) {
            ((AlertDialog)dialog).setMessage(sdf.format(new Date(System.currentTimeMillis())));
        }
    }



    //**********************************************************************************************
    private void showSnack(String text) {
        Snackbar.make(root, text, Snackbar.LENGTH_LONG).show();
    }
}
