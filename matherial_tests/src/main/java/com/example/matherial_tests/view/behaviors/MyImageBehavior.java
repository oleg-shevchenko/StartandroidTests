package com.example.matherial_tests.view.behaviors;

import android.content.Context;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Oleg on 23.07.2017.
 */

public class MyImageBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    private final String TAG = "MyImageBehavior";

    Context ctx;

    public MyImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
    }

    //Здесь происходит проверка на зависимость от другого компонента (если возврат true, то обработка продолжается)
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    //Если View dependency изменила свои параметры, то вызывается этот метод
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
//        if(dependency.getY() == 0) {
//            child.animate().scaleX(1.0f).start();
//            child.animate().scaleY(1.0f).start();
//
//        }
//        else if(child.getScaleX() == 1.0f) {
//            child.animate().scaleX(0.0f).start();
//            child.animate().scaleY(0.0f).start();
//        }

        //Картинка полтора раза быстрее убегает
        child.setY((int) (dependency.getY()*1.5));
        return true;
    }

    //Behavior получит вызовы метода onDependentViewRemoved(), когда зависимая View была удалена
    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, CircleImageView child, View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }

    //Касания************************************************************************************************
    //Если вернуть в этом методе true, то ваш Behavior получит все последующие касания с помощью метода onTouchEvent() – всё это происходит в тайне от View.
    //(??? Почему то после этого не работает прокрутка, вощем все стоит на месте)
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, CircleImageView child, MotionEvent ev) {
        return false;
    }

    //Вызывается, если onInterceptTouchEvent() возвращает true
    //(??? Почему-то вызывается два раза)
    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, CircleImageView child, MotionEvent ev) {
        Log.d(TAG, "onTouchEvent");
        return super.onTouchEvent(parent, child, ev);
    }
    //******************************************************************************************************

    //Сохранение состояния при повороте и т.п.
    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, CircleImageView child, Parcelable state) {
        super.onRestoreInstanceState(parent, child, state);
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, CircleImageView child) {
        return super.onSaveInstanceState(parent, child);
    }
}
