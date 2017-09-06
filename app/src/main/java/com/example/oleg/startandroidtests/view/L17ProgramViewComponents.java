package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

import java.util.ArrayList;
import java.util.List;

public class L17ProgramViewComponents extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llMain;
    RadioGroup rgGravity;
    EditText etName;
    Button btnCreate;
    Button btnClear;

    //Для уменьшения громоздкости кода
    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l16_program_view_components);

        llMain = (LinearLayout) findViewById(R.id.llMain);
        rgGravity = (RadioGroup) findViewById(R.id.rgGravity);
        etName = (EditText) findViewById(R.id.etName);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                // Создание LayoutParams c шириной и высотой по содержимому
                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(wrapContent, wrapContent);
                // переменная для хранения значения выравнивания, по умолчанию пусть будет LEFT
                int btnGravity = Gravity.LEFT;
                // определяем, какой RadioButton "чекнут" и соответственно заполняем btnGravity
                switch (rgGravity.getCheckedRadioButtonId()) {
                    case R.id.rbLeft:
                        btnGravity = Gravity.LEFT;
                        break;
                    case R.id.rbCenter:
                        btnGravity = Gravity.CENTER_HORIZONTAL;
                        break;
                    case R.id.rbRight:
                        btnGravity = Gravity.RIGHT;
                        break;
                }
                // переносим полученное значение выравнивания в LayoutParams
                lParams.gravity = btnGravity;

                // создаем Button, пишем текст и добавляем в LinearLayout
                Button btnNew = new Button(this);
                btnNew.setText(etName.getText().toString());

                registerView(btnNew);

                llMain.addView(btnNew, lParams);
                break;

            case R.id.btnClear:
                llMain.removeAllViews();
                Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //Удаляем при нажатии
    private void registerView(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });
    }
}

/*
* На форуме задают вопрос: как потом получить доступ к этим созданным компонентам. Тут есть пара простых вариантов.
1) При создании вы можете сами присваивать компонентам ID.  Это делается методом setId. И потом по этим ID просто вызываете findViewById.
2) Вы можете сохранять созданные компоненты в свой массив или список. Либо можете воспользоваться методом getChildAt.
 Вызов этого метода для llMain позволит получить его дочерние компоненты по индексу.
 Получить кол-во дочерних элементов позволит метод getChildCount.*/
