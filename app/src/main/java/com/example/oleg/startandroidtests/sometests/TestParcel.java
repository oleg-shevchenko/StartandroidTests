package com.example.oleg.startandroidtests.sometests;

import android.os.Parcel;
import android.util.Log;

/**
 * Created by Oleg on 06.07.2017.
 */

//Parcel – это контейнер для передачи данных. У него есть куча методов для помещения и извлечения данных. В этом уроке рассмотрим самые простейшие из них
//Данные записываются байтами, и считываются с позиций байтов
//Напр изначально позиция 0, для записи long использовались еще 8 байтов, т.е. позиция станет 8 и т.д.

    //Хорошим примером использования Parcel - это многопользовательское онлайн приложение например игра,
// когда в игре обмен данных очень большой множество пакетов данных(разного типа) идет от сервера к пользователю,
// например первым байтом будет ID пакета , а по ID пакета легко можно определить какой обработчик для него следует выполнить,
// а в обработчике будет чтение из пакета нужных данных в нужной последовательности и соответственно реакция приложения на эти данные...
// Конечно можно использовать данные и в формате xml , но Parcel в этом примере позволит экономит трафик,
// и парсер для него будет не универсальный(как для стандарта XML) а строго заданный, что увеличит скорость обработки данных.
public class TestParcel {
    private String LOG_TAG = "TestParcel";
    Parcel p;

    public TestParcel() {
        writeParcel();
        readParcel();
    }

    void writeParcel() {
        p = Parcel.obtain();

        byte b = 1;
        int i = 2;
        long l = 3;
        float f = 4;
        double d = 5;
        String s = "abcdefgh";

        logWriteInfo("before writing");
        p.writeByte(b);
        logWriteInfo("byte");
        p.writeInt(i);
        logWriteInfo("int");
        p.writeLong(l);
        logWriteInfo("long");
        p.writeFloat(f);
        logWriteInfo("float");
        p.writeDouble(d);
        logWriteInfo("double");
        p.writeString(s);
        logWriteInfo("String");
    }

    void logWriteInfo(String txt) {
        Log.d(LOG_TAG, txt + ": " + "dataSize = " + p.dataSize());
    }

    void readParcel() {
        logReadInfo("before reading");
        p.setDataPosition(0);
        logReadInfo("byte = " + p.readByte());
        logReadInfo("int = " + p.readInt());
        logReadInfo("long = " + p.readLong());
        logReadInfo("float = " + p.readFloat());
        logReadInfo("double = " + p.readDouble());
        logReadInfo("string = " + p.readString());
    }

    void logReadInfo(String txt) {
        Log.d(LOG_TAG, txt + ": " + "dataPosition = " + p.dataPosition());
    }
}
