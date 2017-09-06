package com.example.oleg.startandroidtests.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.sometests.TestClassObservable;
import com.example.oleg.startandroidtests.sometests.TestParcel;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TestActivity extends BaseActivity implements Observer {
    TextView tvText;
    TestClassObservable observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TestParcel p = new TestParcel();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvText = (TextView) findViewById(R.id.tvTest);

        observable = TestClassObservable.getInstance();
        observable.addObserver(this);
    }

    public void test(View v) {
        observable.addData(observable.getDataSize() + 1);
    }

    @Override
    public void update(Observable o, Object arg) {
        tvText.setText(((List<Integer>) arg).toString());
        Log.d("Observer update()", ((List<Integer>) arg).toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        observable.deleteObserver(this);
    }
}
