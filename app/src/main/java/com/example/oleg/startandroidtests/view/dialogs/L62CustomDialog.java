package com.example.oleg.startandroidtests.view.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.oleg.startandroidtests.R;

/**
 * Created by Oleg on 05.07.2017.
 */

public class L62CustomDialog extends DialogFragment implements View.OnClickListener {

    final int BUTTON_YES = 1;
    final int BUTTON_NO = 2;
    final int BUTTON_MAYBE = 3;

    //Интерфейс обратного вызова
    OnL62CustomDialogListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Title"); //не пашет чего-то
        View v = inflater.inflate(R.layout.dialog_custom_l62, null);
        v.findViewById(R.id.btnYes).setOnClickListener(this);
        v.findViewById(R.id.btnNo).setOnClickListener(this);
        v.findViewById(R.id.btnMaybe).setOnClickListener(this);
        return v;
    }

    //Используем интерфейс обратного вызова для передачи результата в активити
    //Активити должна реализовать этот интерфейс
    @Override
    public void onClick(View v) {
        int button = 0;
        switch (v.getId()) {
            case R.id.btnYes:
                button = BUTTON_YES;
                break;
            case R.id.btnNo:
                button = BUTTON_NO;
                break;
            case R.id.btnMaybe:
                button = BUTTON_MAYBE;
                break;
        }
        if (button > 0) {
            //Вызов
            listener.onDialogL62Finished(button);
            dismiss();
        }
    }

    //Проверка на то, что активити имплементит наш интерфейс
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnL62CustomDialogListener) context;
        } catch (ClassCastException exc) {
            throw new ClassCastException(context.toString() + " must implement OnL62CustomDialogListener");
        }
    }

    //Интерфейс обратного вызова
    public interface OnL62CustomDialogListener {
        public void onDialogL62Finished(int btnIndex);
    }
}
