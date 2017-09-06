package com.example.matherial_tests.view;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matherial_tests.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestCoordLayBehavior extends Fragment {


    public TestCoordLayBehavior() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_test_coord_lay_behavior, container, false);
        initToolbar(v);
        initFab(v);
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

    private void initFab(View v) {
        v.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "layoutDependsOn():\nreturn dependency instanceof Snackbar.SnackbarLayout;", Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
