package com.example.oleg.startandroidtests.sometests;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Oleg on 28.07.2017.
 */

//Утечка памяти
//Статическая переменная класса Activity содержит в себе ссылку на контекст.
//Пример: https://android-developers.googleblog.com/2009/01/avoiding-memory-leaks.html
//Допустим у вас в onCreate() создается некий Drawable, который достаточно большой и вы хотите чтобы в случае смены ориентации экрана
//(при смене вызывается onDestroy() и onCreate() каждый раз) его не создавать заново, а взять из статической переменной класса.
//Все ок, но если вы связали этот Drawable например с каким-то View, то в статическом поле будет сидеть drawable c уже ссылкой на весь Активити,
//т.к. в каждом View есть ссылка на Контекст. И этот весь старый Активити сборщик мусора не уберет.
//Решение: https://stackoverflow.com/questions/6567647/avoid-memory-leaks-on-android
//Решается это тем, что в методе оnDestroy делается unbind, то есть обнуляется связь у Drawable, чтобы он не тянул за собой весь предыдущий активити.

public class UnbindStaticDrawablesFromView {
    //Статической делают для того, чтобы не пересоздавать при повороте экрана большую drawable
    private static Drawable sBackground;
    TextView label;

    protected void onCreate(Bundle state) {
/*        super.onCreate(state);

        label = new TextView(this);
        label.setText("Leaks are bad");

        if (sBackground == null) {
            sBackground = getDrawable(R.drawable.large_bitmap);
        }
        //Устанавливаем статическую drawable как бекграунд. Данный TextView будет хранить ссылку на нашу активити и не даст сборщику мусора удалить ее.
        label.setBackgroundDrawable(sBackground);

        setContentView(label);*/
    }

    //Решение
    public void onDestroy() {
        unbindDrawables(label);
    }

    public static void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
