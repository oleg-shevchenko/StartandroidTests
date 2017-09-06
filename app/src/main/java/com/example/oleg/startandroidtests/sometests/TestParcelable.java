package com.example.oleg.startandroidtests.sometests;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Oleg on 06.07.2017.
 */

//Для передачи в интентах, например
public class TestParcelable implements Parcelable {

    //Поля обьекта
    String str;
    long k;

    //Обычный конструктор
    public TestParcelable(String str, long k) {
        this.str = str;
        this.k = k;
    }

    //конструктор, считывающий данные из Parcel
    public TestParcelable(Parcel in) {
        str = in.readString();
        k = in.readLong();
    }

    //непонятно зачем, битовое описание каких-то вложеных обьектов, что-ли
    @Override
    public int describeContents() {
        return 0;
    }

    // упаковываем объект в Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str);
        dest.writeLong(k);
    }

    public static final Creator<TestParcelable> CREATOR = new Creator<TestParcelable>() {
        @Override
        // распаковываем объект из Parcel (с помощью конструктора)
        public TestParcelable createFromParcel(Parcel in) {
            return new TestParcelable(in);
        }

        @Override
        public TestParcelable[] newArray(int size) {
            return new TestParcelable[size];
        }
    };


}
