package com.example.matherial_tests.view.behaviors;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.matherial_tests.R;

/**
 * Created by Oleg on 25.07.2017.
 */

public class FabScrollBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private final String TAG = "FabScrollBehavior";
    private long animTime = 0L;

    public FabScrollBehavior(Context context, AttributeSet attrs) {
        //Получаем наши атрибуты с XML, при этом в res/values/attrs.xml их прописываем
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FabScrollBehavior);
            animTime = (long) a.getInt(R.styleable.FabScrollBehavior_animTime, 0);
            a.recycle();
        }
    }

    //События вложенной прокрутки начинаются с метода onStartNestedScroll(). Вы получите оси прокрутки
    //(горизонтальная или вертикальная – можем легко игнорировать прокрутку в определённом направлении)
    //и должны вернуть true, чтобы получить дальнейшие события прокрутки.
    //После того как вы вернёте true в методе onStartNestedScroll(), вложенная прокрутка работает в два этапа: onNestedPreScroll() и onNestedScroll()
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG, "onStartNestedScroll");
        child.animate().scaleX(0.0f).setDuration(animTime).start();
        child.animate().scaleY(0.0f).setDuration(animTime).start();
        child.setScaleX(0.0f);
        child.setScaleY(0.0f);
        return true;
    }

    //onNestedPreScroll() вызывается перед тем как прокручиваемая View получила событие прокрутки
    //и позволяет вашему Behavior обработать часть или всю прокрутку (последний int[] параметр – это “исходящий” параметр,
    //где вы можете указать какую часть прокрутки обработал Behavior).
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dx, int dy, int[] consumed) {
        Log.d(TAG, "onNestedPreScroll");
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    //onNestedScroll() вызывается как только прокручиваемая View была прокручена.
    //Вы получите значение насколько View была прокручена и необработанные значения.
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG, "onNestedScroll");
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }


    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG, "onNestedScrollAccepted");
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    //Когда вложенная (либо быстрая) прокрутка заканчиваются, вы получите вызов метода onStopNestedScroll()
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
        Log.d(TAG, "onStopNestedScroll");
        child.animate().scaleX(1.0f).setDuration(animTime).start();
        child.animate().scaleY(1.0f).setDuration(animTime).start();
        child.setScaleX(1.0f);
        child.setScaleY(1.0f);
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    //****************************************************************************************************************
    //Так как у нас кастомный Behavior, то стандартный уже не работает, поэтому описываем поведение со Snackbar (или другими View)
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if(dependency instanceof Snackbar.SnackbarLayout) {return true;}
        else {return false;}
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}
