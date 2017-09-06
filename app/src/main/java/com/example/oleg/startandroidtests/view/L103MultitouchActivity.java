package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/*
Рассмотрим систему событий для мультитача. К событиям ACTION_DOWN, ACTION_MOVE и ACTION_UP добавляются ACTION_POINTER_DOWN и ACTION_POINTER_UP.
ACTION_DOWN – срабатывает при касании первого пальца
ACTION_POINTER_DOWN – срабатывает при касании каждого последующего пальца
ACTION_MOVE - срабатывает при любом движении
ACTION_ POINTER_UP – срабатывает при отпускании каждого пальца кроме последнего
ACTION_ UP – срабатывает при отпускании последнего пальца

Теперь надо понять, как отличить - для какого именно пальца сработали события ACTION_POINTER_DOWN и ACTION_ POINTER_UP.
Для этого используются две системы нумерации – индекс и ID.
Индекс – порядковый номер пальца. Не привязан к пальцу – один палец может иметь разные индексы в течение одного касания.
ID -  привязан к пальцу от начала до конца касания.
*/

public class L103MultitouchActivity extends AppCompatActivity implements View.OnTouchListener {

    StringBuilder sb = new StringBuilder();
    TextView tv;
    int upPI = 0;
    int downPI = 0;
    boolean inTouch = false;
    String result = "";

    //В onCreate мы создаем TextView, присваиваем обработчик – текущее Activity, и помещаем в Activity.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        tv.setTextSize(20);
        tv.setOnTouchListener(this);
        setContentView(tv);
    }

    //Если для одного касания мы использовали метод getAction, чтобы понять какое событие произошло,
    //то с мультитачем надо использовать getActionMasked. Индекс касания определяется методом getActionIndex. Кол-во текущих касаний – getPointerCount.
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // событие
        int actionMask = event.getActionMasked();
        // индекс касания
        int pointerIndex = event.getActionIndex();
        // число касаний
        int pointerCount = event.getPointerCount();

        switch (actionMask) {
            //Если событие - ACTION_DOWN, значит мы получили первое касание. Ставим метку inTouch = true.
            //Она для нас будет означать, что есть касания. Обратите внимание,
            //что в этой ветке case мы не ставим break – следующая case-ветка (ACTION_POINTER_DOWN) также выполнится при ACTION_DOWN.
            case MotionEvent.ACTION_DOWN: // первое касание
                inTouch = true;
            //Если событие ACTION_POINTER_DOWN (или ACTION_DOWN), то в переменную downPI помещаем индекс касания. Это будет индекс последнего прикоснувшегося пальца.
            case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
                downPI = pointerIndex;
                break;
            //Если событие - ACTION_UP, значит последнее касание прервано и экрана больше ничего не касается.
            //Ставим inTouch = false, т.е. отсутствие касаний. Очищаем StringBuilder, который содержит информацию о движениях.
            case MotionEvent.ACTION_UP: // прерывание последнего касания
                inTouch = false;
                sb.setLength(0);
            //Если событие - ACTION_POINTER_UP (или ACTION_UP), то в переменную upPI помещаем индекс касания.
            //Это будет индекс последнего прерванного касания. Т.е. когда мы одно за другим прерываем касания,
            //эта переменная будет содержать один за другим индексы последнего из прерванных.
            case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                upPI = pointerIndex;
                break;
            //Если событие ACTION_MOVE – мы перебираем все существующие индексы. С помощью pointerCount определяем,
            //какие из них сейчас задействованы и содержат информацию о касаниях.
            //Для них мы пишем номер индекса, ID (метод getPointerId) и координаты (getX и getY).
            //Для незадействованных пишем только номер индекса. Пишем мы это все в StringBuilder.
            case MotionEvent.ACTION_MOVE: // движение
                sb.setLength(0);

                for (int i = 0; i < 10; i++) {
                    sb.append("Index = " + i);
                    if (i < pointerCount) {
                        sb.append(", ID = " + event.getPointerId(i));
                        sb.append(", X = " + event.getX(i));
                        sb.append(", Y = " + event.getY(i));
                    } else {
                        sb.append(", ID = ");
                        sb.append(", X = ");
                        sb.append(", Y = ");
                    }
                    sb.append("\r\n");
                }
                break;
        }

        //Далее при любом событии формируем result, пишем туда индекс последнего касания и последнего завершенного касания.
        //Если в данный момент есть касание (inTouch), то добавляем в результат содержимое StringBuilder с подробной инфой о всех касаниях. И выводим result в TextView.
        result = "down: " + downPI + "\n" + "up: " + upPI + "\n";
        if (inTouch) {
            result += "pointerCount = " + pointerCount + "\n" + sb.toString();
        }
        tv.setText(result);
        return true;
    }
}
