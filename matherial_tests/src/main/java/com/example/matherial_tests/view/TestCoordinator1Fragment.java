package com.example.matherial_tests.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.matherial_tests.R;

public class TestCoordinator1Fragment extends Fragment {

    public TestCoordinator1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_test_coordinator1, container, false);
        initToolbar(v);
        return v;
    }

    private void initToolbar(View v) {
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.main_toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        MainNavigationDrawerActivity activity = MainNavigationDrawerActivity.instance;
        activity.setSupportActionBar(toolbar);

        //Инициализируем "бутерброд"
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), activity.drawer, toolbar, R.string.str_open_drawer, R.string.str_close_drawer);
        activity.drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

//    @Override
//    public void onDetach() {
//        MainNavigationDrawerActivity.instance.setSupportActionBar(null);
//        super.onDetach();
//    }
}
