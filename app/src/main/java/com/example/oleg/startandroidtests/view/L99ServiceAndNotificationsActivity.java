package com.example.oleg.startandroidtests.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.services.L99Service;

/*
Разберем уведомление на логические составляющие, чтобы проще было понять как его создавать и отправлять.
Первая часть – то, что видно в статус-баре, когда уведомление только приходит – иконка и текст. Текст потом исчезает и остается только иконка.
Вторая часть – то, что мы видим, когда открываем статус бар (тянем вниз). Там уже полноценный View с иконкой и двумя текстами, т.е. более подробная информация о событии.
Третья часть – то, что произойдет, если мы нажмем на View из второй части. Тут обычно идет вызов Activity, где мы можем просмотреть полную информацию и обработать событие.

Кроме этого есть еще несколько возможностей, по которым совсем кратко пробежимся в конце урока.
Создадим приложение и сервис. Сервис, как будто загружает файл и посылает уведомление, по нажатию на которое будет открываться приложение и отображать имя файла.*/

public class L99ServiceAndNotificationsActivity extends AppCompatActivity {

    public final static String FILE_NAME = "filename";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l99_service_and_notifications);

        tv = (TextView) findViewById(R.id.tv);

        Intent intent = getIntent();
        String fileName = intent.getStringExtra(FILE_NAME);
        if (!TextUtils.isEmpty(fileName))
            tv.setText(fileName);
    }

    //если в манифесте прописать для активити android:launchMode="singleInstance"
    //то при попытке вызвать новую копию активити, при уже открытой, напр. при нажатии на уведомление, будет вызвана текущая с вызовом onNewIntent вместо onCreate
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String fileName = intent.getStringExtra(FILE_NAME);
        if (!TextUtils.isEmpty(fileName))
            tv.setText(fileName);
    }

    public void onClickStart(View v) {
        startService(new Intent(this, L99Service.class));
    }

    public void onClickStop(View v) {
        stopService(new Intent(this, L99Service.class));
    }
}
