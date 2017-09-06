package com.example.oleg.startandroidtests.view.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.oleg.startandroidtests.R;

public class L106Fragment extends Fragment {

    final String LOG_TAG = "myLogs";

    public L106Fragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Получим view для фрагмента
        View v = inflater.inflate(R.layout.fragment_l106, container, false);
        initBtn1(v);
        initBtn2(v);
        //Возврат view
        return v;
    }

    private void initBtn1(View v) {
        //Назначим обработчик для кнопки. Атрибут onClick, который мы привыкли использовать для кнопки, здесь не прокатит.
        //Указанный в этом атрибуте метод, будет вызван в Activity, а не в фрагменте.
        v.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Получаем доступ к активити из фрагмента
                ((Button) getActivity().findViewById(R.id.btnFind)).setText("Access from Fragment1");
            }
        });
    }

    //**********************************Event*************************************************//

    //Инстанс нашего интерфейса для CallBack
    private OnSomeEventListener eventListener;

    //Описываем интерфейс onSomeEventListener. В нем метод someEvent, который на вход получает строку. Этот интерфейс будет реализовывать Activity.
    public interface OnSomeEventListener {
        public void someEvent(String s);
    }

    //В методе onAttach мы на вход получаем Activity, к которому присоединен фрагмент.
    //Мы пытаемся привести это Activity к типу интерфейса onSomeEventListener, чтобы можно было вызывать метод someEvent и передать туда строку.
    //Теперь someEventListener ссылается на Activity.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            eventListener = (OnSomeEventListener) context;
        //Если мы пытаемся присоеденить фрагмент к активити, которая не имплементит наш интерфейс, то бросится исключение
        } catch (ClassCastException exc) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    private void initBtn2(View v) {
        v.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Вызываем метод интерфейса, передаем строку, в активити при этом сработаем метод someEvent
                eventListener.someEvent("Event from fragment");
            }
        });
    }

}
