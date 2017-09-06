package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.oleg.startandroidtests.R;

public class L104FragmentLifecycleActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "L104FragmentLifecycleActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l104_fragment_lifecycle);
    }

    protected void onStart() {
        Log.d(LOG_TAG, "L104FragmentLifecycleActivity onStart");
        super.onStart();
    }

    protected void onResume() {
        Log.d(LOG_TAG, "L104FragmentLifecycleActivity onResume");
        super.onResume();
    }

    protected void onPause() {
        Log.d(LOG_TAG, "L104FragmentLifecycleActivity onPause");
        super.onPause();
    }

    protected void onStop() {
        Log.d(LOG_TAG, "L104FragmentLifecycleActivity onStop");
        super.onStop();
    }

    protected void onDestroy() {
        Log.d(LOG_TAG, "L104FragmentLifecycleActivity onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Menu activity");
        return super.onCreateOptionsMenu(menu);
    }
}
