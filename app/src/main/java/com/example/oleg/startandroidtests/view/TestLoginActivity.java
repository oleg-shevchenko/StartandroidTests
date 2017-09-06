package com.example.oleg.startandroidtests.view;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestLoginActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout usernameWrapper;
    TextInputLayout passwordWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);

        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper  = (TextInputLayout) findViewById(R.id.passwordWrapper);
//        usernameWrapper.setHint("Username");
//        passwordWrapper.setHint("Password");
        findViewById(R.id.btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        String username = usernameWrapper.getEditText().getText().toString();
        String password = passwordWrapper.getEditText().getText().toString();
        if(checkFields(username, password))
            doLogin(username, password);
    }

    //проверка допустимых значений полей и установка ошибки, если нужно
    private boolean checkFields(String username, String password) {
        boolean error = false;

        if (!validateEmail(username)) {
            usernameWrapper.setError("Not a valid email address!");
            error = true;
        } else {
            usernameWrapper.setErrorEnabled(false);
        }
        if (!validatePassword(password)) {
            passwordWrapper.setError("Not a valid password!");
            error = true;
        } else {
            passwordWrapper.setErrorEnabled(false);
        }
        return !error;
    }

    public void doLogin(String username, String password) {
        String text = "OK! I'm performing login.\nUsername: " + username + "\nPassword: " + password;
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        // TODO: login procedure; not within the scope of this tutorial.
    }

    //проверка мыла
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //проверка длинны пароля
    public boolean validatePassword(String password) {
        return password.length() > 5;
    }

    //скрытие клавиатуры
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
