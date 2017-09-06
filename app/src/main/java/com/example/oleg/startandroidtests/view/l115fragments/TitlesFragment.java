package com.example.oleg.startandroidtests.view.l115fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.oleg.startandroidtests.R;

/**
 * Created by Oleg on 30.08.2017.
 */

//Этот фрагмент покажет нам заголовки и уведомит Activity о том, какой из них был выбран.

public class TitlesFragment extends ListFragment {

    private OnItemClickListener listener;

    //OnItemClickListener – интерфейс, который будет наследовать Activity. Подробно эту схему мы разбирали в Уроке 106.
    //Интерфейс имеет метод itemClick, который фрагмент будет вызывать при выборе элемента списка.
    public interface OnItemClickListener {
        public void itemClick(int position);
    }

    //В onAttach записываем Activity (к которому присоединен фрагмент) в listener. Разумеется, это Activity должно реализовывать интерфейс OnItemClickListener.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnItemClickListener) context;
    }

    //В onCreate создаем адаптер с заголовками и передаем его списку.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.l115_headers));
        setListAdapter(adapter);
    }

    //В onListItemClick, мы через listener посылаем в Activity данные о выбранном элементе.
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        listener.itemClick(position);
    }
}
