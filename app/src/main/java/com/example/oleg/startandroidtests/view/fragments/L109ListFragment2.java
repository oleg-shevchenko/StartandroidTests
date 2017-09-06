package com.example.oleg.startandroidtests.view.fragments;


import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

public class L109ListFragment2 extends ListFragment {

    String data[];

    public L109ListFragment2() {
    }

    //Если же вас чем-то не устраивает дефолтный список, как в предыдущем фрагменте, можно использовать свой layout-файл для фрагмента.
    //!!!Внимание! В layout нужно присвоить элементам конкретные ID
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_l109_list_fragment2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Получаем массив строк, который ложили при создании фрагмента в активити
        if(getArguments() != null) {
            data = getArguments().getStringArray("items");
        } else data = new String[0];

        //Установить режим выбора
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

//        //Слушатель нажатий на элементы списка (но лучше использовать встроеный onListItemClick ниже)
//        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String str = (String) getListAdapter().getItem(i);
//                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
//            }
//        });

        //Добавим задержку для демонстрации отображения, пока не присвоен адаптер
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, data);
                //Мы создаем адаптер и используем метод setListAdapter, чтобы передать его списку.
                //Обратите внимание - мы даже не создаем или не находим (findViewById) список (ListView),
                //он уже есть где-то внутри фрагмента и метод setListAdapter сам знает, как до него добраться.
                //В принципе, это и есть основная фишка ListFragment - нам не надо работать с ListView.
                setListAdapter(adapter);
            }
        }, 1000);
    }

    //Ловить нажатия можно в методе onListItemClick. Он очень похож на метод onItemClick из Урока 44.
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String str = (String) l.getAdapter().getItem(position);
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }
}
