package com.example.oleg.startandroidtests.view.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Oleg on 06.07.2017.
 */

public class L67ProgressDialogCircle extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Title...");
        pd.setMessage("Message...");
        pd.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return pd;
    }
}
