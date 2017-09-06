package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

//Раньше мы для View-компонентов использовали OnClickListener и ловили короткие нажатия.
//Теперь попробуем ловить касания и перемещения пальца по компоненту. Они состоят из трех типов событий:
//- нажатие (палец прикоснулся к экрану)
//- движение (палец движется по экрану)
//- отпускание (палец оторвался от экрана)
//Все эти события мы сможем ловить в обработчике OnTouchListener, который присвоим для View-компонента.
//Этот обработчик дает нам объект MotionEvent, из которого мы извлекаем тип события и координаты.

public class L102TouchActivity extends AppCompatActivity implements View.OnTouchListener{

    TextView tv;
    float x;
    float y;
    String sDown;
    String sMove;
    String sUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv = new TextView(this);
        tv.setOnTouchListener(this);
        setContentView(tv);
    }

    //Интерфейс OnTouchListener предполагает, что Activity реализует его метод onTouch.
    //На вход методу идет View для которого было событие касания и объект MotionEvent с информацией о событии.
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX();
        y = event.getY();

        //Методы getX и getY дают нам X и Y координаты касания. Метод getAction дает тип события касания:
        //ACTION_DOWN – нажатие
        //ACTION_MOVE – движение
        //ACTION_UP – отпускание
        //ACTION_CANCEL - вызывается в том случае, если коснуться touchable view и после, не прекращая касание, сдвинуть палец с этого view.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                sDown = "Down: " + x + "," + y;
                sMove = ""; sUp = "";
                break;
            case MotionEvent.ACTION_MOVE: // движение
                sMove = "Move: " + x + "," + y;
                break;
            case MotionEvent.ACTION_UP: // отпускание
                v.performClick();       //вызов метода onClick, для View, если он определен
            case MotionEvent.ACTION_CANCEL:
                sMove = "";
                sUp = "Up: " + x + "," + y;
                break;
        }
        tv.setText(sDown + "\n" + sMove + "\n" + sUp);
        //И возвращаем true – мы сами обработали событие. Если два или более обработчика используют одно и тоже событие,
        //то нужно чтобы обработчики возвращали false, тогда событие будет передано дальше по очереди другим обработчикам.
        return true;
    }
}
