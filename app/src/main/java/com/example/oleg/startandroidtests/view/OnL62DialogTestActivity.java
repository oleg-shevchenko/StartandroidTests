package com.example.oleg.startandroidtests.view;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.sometests.TestParcel;
import com.example.oleg.startandroidtests.view.dialogs.CustomDatePickerDialog;
import com.example.oleg.startandroidtests.view.dialogs.L62AlertDialog;
import com.example.oleg.startandroidtests.view.dialogs.L62CustomDialog;
import com.example.oleg.startandroidtests.view.dialogs.L62DialogTime;
import com.example.oleg.startandroidtests.view.dialogs.L67ProgressDialogCircle;
import com.example.oleg.startandroidtests.view.dialogs.L67ProgressDialogHorizontal;

//https://developer.android.com/guide/topics/ui/dialogs.html
public class OnL62DialogTestActivity extends AppCompatActivity
        implements View.OnClickListener, L62CustomDialog.OnL62CustomDialogListener, CustomDatePickerDialog.OnDateSetListener {

    DialogFragment timeDialog, alertDialog1, customDialog, customDateDialog,  progressDialogCircle, progressDialogHorizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l62_dialog_test);

        timeDialog = new L62DialogTime();
        alertDialog1 = new L62AlertDialog();

        customDialog = new L62CustomDialog();
        customDialog.setCancelable(false);

        customDateDialog = new CustomDatePickerDialog();
        progressDialogCircle = new L67ProgressDialogCircle();
        progressDialogHorizontal = new L67ProgressDialogHorizontal();

        findViewById(R.id.tvTime).setOnClickListener(this);
        findViewById(R.id.tvAlert1).setOnClickListener(this);
        findViewById(R.id.tvCustom).setOnClickListener(this);
        findViewById(R.id.tvCustomDate).setOnClickListener(this);
        findViewById(R.id.tvProgressCircle).setOnClickListener(this);
        findViewById(R.id.tvProgressHorizontal).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTime:
                timeDialog.show(getSupportFragmentManager(), "dlg1");
                break;
            case R.id.tvAlert1:
                alertDialog1.show(getSupportFragmentManager(), "dlg2");
                break;
            case R.id.tvCustom:
                customDialog.show(getSupportFragmentManager(), "dlg3");
                break;
            case R.id.tvCustomDate:
                customDateDialog.show(getSupportFragmentManager(), "dlg4");
                break;
            case R.id.tvProgressCircle:
                progressDialogCircle.show(getSupportFragmentManager(), "dlg5");
                break;
            case R.id.tvProgressHorizontal:
                progressDialogHorizontal.show(getSupportFragmentManager(), "dlg6");
                break;
        }
    }

    //Реализация обратного вызова для нашего диалога CustomDialog
    @Override
    public void onDialogL62Finished(int btnIndex) {
        Toast.makeText(this, "Button index: " + btnIndex, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        Toast.makeText(this, "Date: " + year + "." + month + "." + dayOfMonth, Toast.LENGTH_LONG).show();
    }
}
