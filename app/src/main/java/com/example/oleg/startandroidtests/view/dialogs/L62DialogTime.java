package com.example.oleg.startandroidtests.view.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by Oleg on 05.07.2017.
 */

public class L62DialogTime extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, 12, 0, true);
        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        StringBuilder sb = new StringBuilder("Time: ");
        sb.append(hourOfDay > 9 ? hourOfDay : "0" + hourOfDay);
        sb.append(":");
        sb.append(minute > 9 ? minute : "0" + minute);
        Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();
    }
}
