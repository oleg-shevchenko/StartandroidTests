package com.example.oleg.startandroidtests.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.oleg.startandroidtests.R;

public class IntentTestActivityForResult extends AppCompatActivity implements View.OnClickListener{

    EditText etName;
    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_test_for_result);

        etName = (EditText) findViewById(R.id.etName);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("name", etName.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
