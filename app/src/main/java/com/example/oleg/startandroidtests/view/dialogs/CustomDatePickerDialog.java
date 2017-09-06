package com.example.oleg.startandroidtests.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Oleg on 06.07.2017.
 */

public class CustomDatePickerDialog extends DialogFragment {

    CalendarView cv;
    TextView title;
    GregorianCalendar cal;
    OnDateSetListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Строим через билдер
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Получаем View нашего диалога с помощью inflate()
        View view = inflater.inflate(R.layout.dialog_date_custom, null);
        //Находим элементы нашего View и устанавливаем им обработчики
        initView(view);
        //Устанавливаем наш view для диалога, также определяем кнопки и слушатели их нажатий
        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Обратный вызов
                        listener.onDateSet(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        //Создать наш диалог
        return builder.create();
    }

    //Проверка на то, что активити имплементит наш интерфейс
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //Инстантиируем наш интерфейс
            listener = (OnDateSetListener) context;
        } catch (ClassCastException exc) {
            throw new ClassCastException(context.toString() + " must implement OnDateSetListener");
        }
        cal = (GregorianCalendar) Calendar.getInstance();
    }

    //Обработчик выбора дат
    private void initView(View v) {
        cv = (CalendarView) v.findViewById(R.id.cvCalendar);
        title = (TextView) v.findViewById(R.id.tvTitle);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                cal.set(year, month, dayOfMonth);
                String strMonth = month > 8 ? String.valueOf(month + 1) : "0" + (month + 1);
                String strDay = dayOfMonth > 9 ? String.valueOf(dayOfMonth) : "0" + dayOfMonth;
                title.setText(strDay + "." + strMonth + "." + year);
            }
        });
    }

    //Интерфейс обратного вызова для активити
    public interface OnDateSetListener {
        void onDateSet(int year, int month, int dayOfMonth);
    }
}
