package com.example.matherial_tests.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.matherial_tests.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestBottomSheetFragment extends Fragment {


    public TestBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_test_bottom_sheet, container, false);
        initFloatingActionButton(v);
        return v;
    }

    private void initFloatingActionButton(View v) {
        LinearLayout ll = (LinearLayout) v.findViewById(R.id.bottom_sheet);

        //настройка поведения нижнего экрана (у нас готовое поведение BottomSheetBehavior)
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(ll);

        final FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Развернуть BottomSheet
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
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

}
