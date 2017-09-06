package com.example.oleg.startandroidtests.sometests;

import android.util.Log;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Oleg on 31.07.2017.
 */

public class TestClassObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        Log.d("Observer update()", ((List<Integer>) arg).toString());
    }
}
