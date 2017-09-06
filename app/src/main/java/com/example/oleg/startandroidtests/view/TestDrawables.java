package com.example.oleg.startandroidtests.view;

import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.oleg.startandroidtests.R;

public class TestDrawables extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drawables);

        testTransitionDrawable();

    }

    ImageButton button;
    boolean stateDrawable = true;
    TransitionDrawable drawable;
    private void testTransitionDrawable() {
        button = (ImageButton) findViewById(R.id.imageButton);
        drawable = (TransitionDrawable) button.getDrawable();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stateDrawable) drawable.startTransition(500);
                else drawable.reverseTransition(500);
                stateDrawable = !stateDrawable;
            }
        });
    }
}
