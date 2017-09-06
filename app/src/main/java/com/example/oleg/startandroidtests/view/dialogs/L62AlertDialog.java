package com.example.oleg.startandroidtests.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg on 05.07.2017.
 */

public class L62AlertDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private final String LOG_TAG = "L62AlertDialog";
    List<Integer> selectedItems;
    String[] items = {"one", "two", "three"};

    //Обычно для заполнения фрагмента содержимым мы использовали метод onCreateView. Для создания диалога с помощью билдера используется onCreateDialog.
    //Создаем диалог с заголовком, сообщением и тремя кнопками. Обработчиком для кнопок назначаем текущий фрагмент.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedItems = new ArrayList<>();
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("MyDialogTitle")
                .setIcon(R.drawable.google_drive_icon)
                .setPositiveButton("OK", this)
                .setNegativeButton("NO", this)
                .setNeutralButton("Cancel", this)
                //.setItems(new String[]{"one", "two", "three"}, null)
                //.setSingleChoiceItems(new String[]{"one", "two", "three"}, 1, null);
                //.setCancelable(false)
                //.setMessage("MyDialogMessage");
                .setMultiChoiceItems(items, null, new MyMultiChoiceListener());
        return adb.create();
    }

    class MyMultiChoiceListener implements DialogInterface.OnMultiChoiceClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            if(isChecked) {
                selectedItems.add(which);
            }
            else if(selectedItems.contains(which)) {
                selectedItems.remove(Integer.valueOf(which));
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String msg = "";
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                msg = "OK";
                break;
            case Dialog.BUTTON_NEGATIVE:
                msg = "NO";
                break;
            case Dialog.BUTTON_NEUTRAL:
                msg = "Cancel";
                break;
        }

        StringBuilder sb = new StringBuilder("Selected:");
        for(Integer i : selectedItems) {
            sb.append(" " + items[i]);
        }
        Toast.makeText(getActivity(), msg + "\n" + sb.toString(), Toast.LENGTH_LONG).show();
    }

    //onDismiss – диалог закрылся.
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "onDismiss");
    }

    //onCancel - отмена клавишей назад
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "onCancel");
    }
}
