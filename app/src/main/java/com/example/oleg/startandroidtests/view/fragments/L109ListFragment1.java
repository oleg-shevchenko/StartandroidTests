package com.example.oleg.startandroidtests.view.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

/**
 * Created by Oleg on 11.08.2017.
 */

//Пока что не будем создавать Layout-файл для этого фрагмента. Дело в том, что ListFragment по умолчанию уже содержит ListView и мы вполне можем обойтись им.
public class L109ListFragment1 extends ListFragment {
    String data[];

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Получаем массив строк, который ложили при создании фрагмента в активити
        if(getArguments() != null) {
            data = getArguments().getStringArray("items");
        } else data = new String[0];

        //Добавим задержку для демонстрации отображения, пока не присвоен адаптер
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
                //Мы создаем адаптер и используем метод setListAdapter, чтобы передать его списку.
                //Обратите внимание - мы даже не создаем или не находим (findViewById) список (ListView),
                //он уже есть где-то внутри фрагмента и метод setListAdapter сам знает, как до него добраться.
                //В принципе, это и есть основная фишка ListFragment - нам не надо работать с ListView.
                setListAdapter(adapter);
            }
        }, 1000);
    }
}
