package com.example.oleg.startandroidtests.view.l115fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

public class DetailsFragment extends Fragment {

    public DetailsFragment() {
    }

    //Метод newInstance создает экземпляр фрагмента и записывает в его атрибуты число, которое пришло на вход методу.
    //Это число будет содержать позицию выбранного элемента из списка заголовков.
    public static DetailsFragment newInstance(int position) {
        DetailsFragment details = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        details.setArguments(args);
        return details;
    }

    //onCreateView создает View, находим в нем TextView, и помещает в этот TextView содержимое, соответствующее позиции.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details_l115, container, false);
        TextView tv = (TextView) v.findViewById(R.id.tvText);
        tv.setText(getResources().getStringArray(R.array.l115_content)[getPosition()]);
        return v;
    }

    //Метод getPosition достает из аргументов позицию.

    //Для нас тут новыми являются аргументы фрагмента. Они могут быть заданы строго до того,
    //как фрагмент будет присоединен к какому либо Activity, т.е., обычно, сразу после создания фрагмента.
    //Они хранятся в фрагменте даже после того, как он был пересоздан в результате, например, смены ориентации экрана.
    public int getPosition() {
        return getArguments().getInt("position", 0);
    }

}
