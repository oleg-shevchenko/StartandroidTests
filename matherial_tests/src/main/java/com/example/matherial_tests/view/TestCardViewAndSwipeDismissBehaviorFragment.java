package com.example.matherial_tests.view;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.matherial_tests.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestCardViewAndSwipeDismissBehaviorFragment extends Fragment {


    public TestCardViewAndSwipeDismissBehaviorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_card_view_and_swipe_dismiss_behavior, container, false);

        initCardview(view);

        return view;
    }

    private void initCardview(View view) {
        CardView cardView = (CardView) view.findViewById(R.id.card_view);

        //Создаем поведение
        final SwipeDismissBehavior<CardView> swipe = new SwipeDismissBehavior<>();
        //Указываем приемлемое направление
        swipe.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        //Присваиваем слушатель
        swipe.setListener(new SwipeDismissBehavior.OnDismissListener() {
            //Обработка dismiss
            @Override
            public void onDismiss(View view) {
                Toast.makeText(getActivity(), "Card swiped !!", Toast.LENGTH_SHORT).show();
            }

            //Состояния карточки
            @Override
            public void onDragStateChanged(int state) {
                //SwipeDismissBehavior.STATE_IDLE = 0 - карточка стоит на месте, не тянется
                //SwipeDismissBehavior.STATE_DRAGGING = 1 - карточка в процессе смахивания
                //SwipeDismissBehavior.STATE_SETTLING = 2 - карточку смахнули
                Log.d("CardViewAndSwipe", "State: " + state);
            }
        });

        //Добавляем поведение в LayoutParams
        CoordinatorLayout.LayoutParams coordinatorParams = (CoordinatorLayout.LayoutParams) cardView.getLayoutParams();
        coordinatorParams.setBehavior(swipe);
    }

}
