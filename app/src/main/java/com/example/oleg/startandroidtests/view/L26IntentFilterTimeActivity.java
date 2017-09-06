package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class L26IntentFilterTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l26_intent_filter_time);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String date = sdf.format(new Date(System.currentTimeMillis()));

        TextView tvDate = (TextView) findViewById(R.id.l26_1_tvTime);
        tvDate.setText(date);
    }
}
