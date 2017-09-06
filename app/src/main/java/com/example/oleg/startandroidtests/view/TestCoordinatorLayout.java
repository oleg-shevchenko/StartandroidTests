package com.example.oleg.startandroidtests.view;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.example.oleg.startandroidtests.R;

public class TestCoordinatorLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_coordinator_layout);

        initFloatingActionButton();

        //определяем наш тулбар как ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFloatingActionButton() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.bottom_sheet);

        //настройка поведения нижнего экрана (у нас готовое поведение BottomSheetBehavior)
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(ll);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_bottom);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Развернуть BottomSheet
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                //Свернуть BottomSheet
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            //также есть полное скрытие
            //т.е. STATE_COLLAPSED сделает свернутым, но если например app:behavior_peekHeight="50dp", то 50dp будут видны
            //а STATE_HIDDEN полностью скроет, как свайпом вниз (!если стоит app:behavior_hideable="true" или setHideable(true))
        });

        // настройка колбэков при изменениях
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // этот код скрывает кнопку сразу же и отображает после того как нижний экран полностью свернется
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    //когда тянем
                    fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                 //когда свернулся
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                }
            }
            //при движении, slideOffset - 0.0-1.0 процент раскрытия
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //fab.animate().scaleX(1 + 0.3f*slideOffset).scaleY(1 + 0.3f*slideOffset).setDuration(0).start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.l14_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
