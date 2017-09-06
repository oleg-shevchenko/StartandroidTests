package com.example.oleg.startandroidtests.sometests;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Oleg on 31.07.2017.
 */

public class TestClassObservable extends Observable {

    private static TestClassObservable instance;

    //Хранилище данных
    private List<Integer> data;

    private TestClassObservable() {
        super();
        data = new ArrayList<>();
    }

    //синглтон
    public static TestClassObservable getInstance() {
        if(instance == null) {
            instance = new TestClassObservable();
        }
        return instance;
    }

    //Добавить данные
    public void addData(Integer value) {
        data.add(value);
        //Устанавливаем метку, что данные изменились, иначе notifyObservers(data) при проверке этого флага, прервет выполнение
        setChanged();
        //Сообщаем наблюдателям, что данные изменились
        notifyObservers(data);
        Log.d("TestClassObservable", "add value " + value + " List size: " + getDataSize());
    }

    public boolean removeData(Integer value) {
        if(data.remove(value)) {
            setChanged();
            notifyObservers(data);
            return true;
        }
        return false;
    }

    public int getDataSize() {
        return data.size();
    }
}
