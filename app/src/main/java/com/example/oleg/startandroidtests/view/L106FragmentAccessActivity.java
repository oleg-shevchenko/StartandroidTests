package com.example.oleg.startandroidtests.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.view.fragments.L106Fragment;

public class L106FragmentAccessActivity extends AppCompatActivity implements L106Fragment.OnSomeEventListener{

    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l106_fragment_access);

        //Добавляем отображение фрагмента
        fragment = new L106Fragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    //Пример доступа к элементам фрагмента из активити
    public void onClick(View v) {
        //Находим нужный фрагмент (если нужно) методом findFragmentById, передав id компонента
        Fragment frag1 = getFragmentManager().findFragmentById(R.id.fragment);
        //Вызываем у фрагмента метод getView() и получаем view фрагмента с доступом ко всем элементам
        ((TextView) frag1.getView().findViewById(R.id.textView)).setText("Access to Fragment 1 from Activity");
    }

    //Активити реализует интерфейс, который мы описали в классе фрагмента. При вызове методов интерфейса в фрагменте они
    //будут отрабатывать в активити, которая их реализует
    @Override
    public void someEvent(String s) {
        Snackbar.make(findViewById(R.id.LinearLayout1), s, Snackbar.LENGTH_SHORT).show();
    }
}
