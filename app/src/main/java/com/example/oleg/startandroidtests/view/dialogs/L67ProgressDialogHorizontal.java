package com.example.oleg.startandroidtests.view.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Oleg on 06.07.2017.
 */

public class L67ProgressDialogHorizontal extends DialogFragment {

    ProgressDialog pd;
    Handler h;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Title");
        pd.setMessage("Message");
        // меняем стиль на индикатор
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // устанавливаем максимум
        pd.setMax(2148);

        initHandler();
        return pd;
    }

    private void initHandler() {
        //Эмуляция какого-то выполнения
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // выключаем анимацию ожидания
                pd.setIndeterminate(false);
                if (pd.getProgress() < pd.getMax()) {
                    // увеличиваем значения индикаторов
                    pd.incrementProgressBy(50);
                    pd.incrementSecondaryProgressBy(75);
                    h.sendEmptyMessageDelayed(0, 100);
                } else {
                    pd.dismiss();
                }
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        // включаем анимацию ожидания
        pd.setIndeterminate(true);
        //Запуск эмуляции
        h.sendEmptyMessageDelayed(0, 3000);
    }
}
