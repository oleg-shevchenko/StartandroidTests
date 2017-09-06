package com.example.oleg.startandroidtests.view.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oleg.startandroidtests.R;

public class L104Fragment1 extends Fragment {

    final String LOG_TAG = "myLogs";

    public L104Fragment1() {
    }

    //следующие три метода вызываются, когда в активити вызываем super.onCreate()
    //onAttach – фрагмент прикреплен к Activity и получает ссылку на него. В дальнейшем мы всегда можем получить ссылку на Activity, вызвав метод getActivity().
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "Fragment1 onAttach");
    }

    //onCreate  - это аналог метода onCreate у Activity, но здесь мы пока не имеем доступа к UI-элементам
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //сообщаем, что фрагмент намеревается добавить свои пункты меню
        setHasOptionsMenu(true);
        Log.d(LOG_TAG, "Fragment1 onCreate");
    }

    //onCreateView – здесь вы создаете View, который будет содержимым фрагмента, и отдаете его системе
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Fragment1 onCreateView");
        return inflater.inflate(R.layout.fragment_l104_fragment1, container, false);
    }

    //onActivityCreated – сообщает фрагменту о том, что Activity создано и можно работать с UI-элементами
    //вызывается, когда метод onCreate() в активити завершает работу
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 onActivityCreated");
    }

    //onStart – аналогичен методу Activity, фрагмент виден пользователю (вызов происходит когда в активити в методе onStart() вызываем super.onStart()
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Fragment1 onStart");
    }

    //super.onResume() у активити. onResume - аналогичен методу Activity, фрагмент доступен для взаимодействия.
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Fragment1 onResume");
    }



    //фрагмент недуоступен для взаимодействия с пользователем
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Fragment1 onPause");
    }

    //фрагмент не виден пользователю
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Fragment1 onStop");
    }

    //Затем для фрагментов вызываются три метода по уничтожению:

    //onDestroyView – сообщает нам, что View, которое мы создавали в onCreateView, более недоступно
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "Fragment1 onDestroyView");
    }

    //onDestroy – аналог метода onDestroy у Activity
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Fragment1 onDestroy");
    }

    //onDetach – фрагмент отсоединен от Activity
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "Fragment1 onDetach");
    }

    //И в конце вызывается метод onDestroy для Activity.



    //**************************Меню***********************************************//
    //Фрагменты могут добавлять пункты меню в Меню вариантов операции (и, следовательно, в Строку действий), реализовав onCreateOptionsMenu().
    //Однако, чтобы этот метод мог принимать вызовы, необходимо вызывать setHasOptionsMenu() во время выполнения метода onCreate(),
    //чтобы сообщить, что фрагмент намеревается добавить пункты в Меню вариантов (в противном случае фрагмент не примет вызов метода onCreateOptionsMenu()).

    //Любые пункты, добавляемые фрагментом в Меню вариантов, присоединяются к уже существующим.
    //Кроме того, фрагмент принимает обратные вызовы метода onOptionsItemSelected(), когда пользователь выбирает пункт меню.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Menu fragment1");
    }

    /*
    Разработчик может также зарегистрировать представление в макете своего фрагмента, чтобы предоставить контекстное меню.
    Для этого следует вызвать метод registerForContextMenu(). Когда пользователь открывает контекстное меню, фрагмент принимает вызов метода onCreateContextMenu().
    Когда пользователь выбирает пункт меню, фрагмент принимает вызов метода onContextItemSelected().

    Примечание. Хотя фрагмент принимает обратный вызов по событию «выбран пункт меню» для каждого добавленного им пункта,
    операция первой принимает соответствующий обратный вызов, когда пользователь выбирает пункт меню.
    Если имеющаяся в операции реализация обратного вызова по событию «выбран пункт меню» не обрабатывает выбранный пункт,
    событие передается методу обратного вызова во фрагменте. Это справедливо для Меню вариантов и контекстных меню.*/
}
