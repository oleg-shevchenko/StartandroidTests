package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.oleg.startandroidtests.R;

public class L18SeekbarAndLayoutParamsActivity extends AppCompatActivity {

    SeekBar sbWeight;
    Button btn1, btn2;
    LinearLayout.LayoutParams lParams1, lParams2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l18_seekbar_and_layout_params);

        sbWeight = (SeekBar) findViewById(R.id.l18_sbWeight);
        btn1 = (Button) findViewById(R.id.l18_btn1);
        btn2 = (Button) findViewById(R.id.l18_btn2);

        //Мы используем метод getLayoutParams для получения LayoutParams компонента.
        // Но этот метод возвращает базовый ViewGroup.LayoutParams, а нам нужен LinearLayout.LayoutParams, поэтому делаем преобразование.
        lParams1 = (LinearLayout.LayoutParams) btn1.getLayoutParams();
        lParams2 = (LinearLayout.LayoutParams) btn2.getLayoutParams();

        initSeekListener(sbWeight);
    }

    //Слушатель ползунка, можно использовать и активити
    private void initSeekListener(SeekBar sb) {
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //onProgressChanged срабатывает все время, пока значение меняется
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int leftValue = progress;
                int rightValue = seekBar.getMax() - progress;
                // настраиваем вес
                lParams1.weight = leftValue;
                lParams2.weight = rightValue;
                //Есть небольшой нюанс, если просто написать код lParams1.weight = 1, то компонент не изменится.
                //Необходимо дописать код: btn1.requestLayout(). Тогда кнопка прочтет Layout и перерисуется.
                //Этот метод уже вызывается в дальше setText, поэтому мы его здесь явно не вызываем.

                // в текст кнопок пишем значения переменных
                btn1.setText(String.valueOf(leftValue));
                btn2.setText(String.valueOf(rightValue));
            }
            //onStartTrackingTouch срабатывает, когда начинаем тащить ползунок
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            //onStopTrackingTouch срабатывает, когда отпускаем ползунок
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
