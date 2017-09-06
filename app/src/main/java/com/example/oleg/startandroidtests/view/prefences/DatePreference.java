package com.example.oleg.startandroidtests.view.prefences;

import android.content.Context;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Oleg on 09.07.2017.
 */

public class DatePreference extends DialogPreference {

    private GregorianCalendar calendar;

    public DatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Устанавливаем layout диалога
        setDialogLayoutResource(R.layout.dialog_date_custom);

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setIcon(null);
        setDialogTitle(null);

        calendar = new GregorianCalendar();
    }

    public DatePreference(Context context) {
        this(context, null);
    }

    public GregorianCalendar getCalendar() {
        return calendar;
    }

    private CalendarView cv;
    private TextView title;

    //Здесь получаем доступ к элементам диалога
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        cv = (CalendarView) view.findViewById(R.id.cvCalendar);
        title = (TextView) view.findViewById(R.id.tvTitle);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                String strMonth = month > 8 ? String.valueOf(month + 1) : "0" + (month + 1);
                String strDay = dayOfMonth > 9 ? String.valueOf(dayOfMonth) : "0" + dayOfMonth;
                title.setText(strDay + "." + strMonth + "." + year);
            }
        });
    }



    //Срабатывает при закрытии диалога кнопками Ok, cancel или просто закрытии
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult) {
            Log.d("TIME: ", "" + calendar.getTimeInMillis());
            //!!!!!!Сохранение результата!!!!!!
            persistLong(calendar.getTimeInMillis());
            //Callback
            getOnPreferenceChangeListener().onPreferenceChange(this, calendar.getTimeInMillis());
        }
    }

    //Пример переопределения (не совсем понятно когда срабатывает)
    //Когда система добавляет ваше предпочтение Preference на экран, она вызывает метод onSetInitialValue(),
    // чтобы уведомить вас, имеет ли настройка сохраненное значение. Если сохраненного значения нет, этот вызов предоставляет вам значение по умолчанию.
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        //Считываем сохраненное значение или подставляем 0 по умолчанию
        if (restorePersistedValue) {
            // Restore existing state
            System.out.println("222222222222222222222222222222222222");
            calendar.setTimeInMillis(getPersistedLong(0));
        //Если значение не сохранялось, то берем из XML и записываем
        } else {
            System.out.println("111111111111111111111111111111111111111");
            // Set default state from the XML attribute
            calendar.setTimeInMillis((Long) defaultValue);
            persistLong((Long) defaultValue);
        }
    }
}
