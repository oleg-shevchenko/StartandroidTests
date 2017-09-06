package com.example.oleg.startandroidtests.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.oleg.startandroidtests.view.dialogs.ProgressDialogSimpleCircle;

/**
 * Created by Oleg on 06.07.2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public static BaseActivity baseActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        baseActivity = this;
        super.onCreate(savedInstanceState);
    }

//***************************Показ прогрессбара*****************************************************
//Способ устарел, нашел лучший способ с диалогом ниже
    private ProgressBar progressBar;
    private ViewGroup rootView;

    //Инициализация прогрессбара для любого активити
    private void initProgressBar() {
        rootView = (ViewGroup) findViewById(android.R.id.content).getRootView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout rl = new RelativeLayout(this);
        rl.setGravity(Gravity.CENTER);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);

        rl.addView(progressBar);
        rootView.addView(rl, params);
    }

    //Показать прогрессбар
    public void showProgressOld() {
        if(progressBar == null) initProgressBar();
        progressBar.setVisibility(View.VISIBLE);
        //setViewAndChildrenEnabled(rootView, false);
    }

    //Скрыть прогрессбар
    public void hideProgressOld() {
        if(progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            //setViewAndChildrenEnabled(rootView, true);
        }
    }

    //установка enabled-disabled для всех view в контейнере
    private static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }
//*************************************************************************************************

    public void showProgressDialog() {
        ProgressDialogSimpleCircle.getInstance().show(getSupportFragmentManager(), "spd");
    }

    public void hideProgressDialog() {
        ProgressDialogSimpleCircle.getInstance().dismiss();
    }
}
