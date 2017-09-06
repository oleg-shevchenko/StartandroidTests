package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

public class L16ProgramLayoutActivity extends AppCompatActivity {

    //Программное создание экрана. LayoutParams

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // создание LinearLayout
        LinearLayout linLayout = new LinearLayout(this);
        // установим вертикальную ориентацию
        linLayout.setOrientation(LinearLayout.VERTICAL);
        // создаем LayoutParams, используем базовый класс ViewGroup.LayoutParams, чтобы не указывать все параметры LinearLayout
        ViewGroup.LayoutParams llParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // устанавливаем linLayout как корневой элемент экрана
        setContentView(linLayout, llParams);


        //Мы снова создаем объект LayoutParams с атрибутами width = wrap_content и height = wrap_content.
        // Теперь если мы присвоим этот объект какому-либо View, то это View будет иметь ширину и высоту по содержимому.
        //Далее мы создаем TextView, настраиваем его текст, присваиваем ему выше созданный LayoutParams
        // и добавляем в LinearLayout с помощью метода addView (View child).
        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(this);
        tv.setText("TextView");
        tv.setLayoutParams(lpView);
        linLayout.addView(tv);

        Button btn = new Button(this);
        btn.setText("Button");
        linLayout.addView(btn, lpView);

        //для View в LinearLayout доступны, например, отступ слева или выравнивание по правому краю.
        // И если мы хотим их задействовать, значит надо использовать LinearLayout.LayoutParams:
        LinearLayout.LayoutParams leftMarginParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        leftMarginParams.leftMargin = 50;
        Button btn1 = new Button(this);
        btn1.setText("Button1");
        linLayout.addView(btn1, leftMarginParams);


        LinearLayout.LayoutParams rightGravityParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rightGravityParams.gravity = Gravity.RIGHT;
        Button btn2 = new Button(this);
        btn2.setText("Button2");
        linLayout.addView(btn2, rightGravityParams);
    }
}
