package com.example.matherial_tests.view.other;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.matherial_tests.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {

    private String text = "";

    public PeopleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_people, container, false);
        ((TextView) v.findViewById(R.id.text)).setText(text);
        return v;
    }

    public PeopleFragment setCenterText(String text) {
        this.text = text;
        return this;
    }
}
