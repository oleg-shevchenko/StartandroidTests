package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.view.l115fragments.DetailsFragment;
import com.example.oleg.startandroidtests.view.l115fragments.TitlesFragment;

/*
Соответственно урок состоит из трех частей.
1. Приложение, отображающее слева заголовки, а справа – содержимое
2. Добавляем учет ориентации. При вертикальной будем отображать заголовки на первом экране, а содержимое на втором.
3. Добавляем учет размера экрана. Для небольших экранов в любой ориентации будем отображать заголовки на первом экране, а содержимое на втором.
*/

public class L115DifferentScreenActivity extends AppCompatActivity implements TitlesFragment.OnItemClickListener {

    int position = 0;

    //В onCreate вызываем метод, который покажет последнюю выбранную запись.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l115_different_screen);

        if (savedInstanceState != null)
            position = savedInstanceState.getInt("position");
        showDetails(position);
    }

    //Метод showDetails получает на вход позицию, ищет DetailsFragment.
    //Если не находит или находит но, отображающий данные по другой позиции, то создает фрагмент заново, передает ему нужную позицию и размещает в контейнер.
    void showDetails(int pos) {
        DetailsFragment details = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.cont);
        if (details == null || details.getPosition() != pos) {
            System.out.println("NULL");
            details = DetailsFragment.newInstance(pos);
            getSupportFragmentManager().beginTransaction().replace(R.id.cont, details).commit();
        }
    }

    @Override
    public void itemClick(int position) {
        this.position = position;
        showDetails(position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
    }
}
