package com.example.oleg.startandroidtests.view;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

public class L180ConstraintLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l180_constraint_layout);
    }

    public void submit(View v) {
        Snackbar.make(v, "Submit", Snackbar.LENGTH_SHORT).show();
    }
}
