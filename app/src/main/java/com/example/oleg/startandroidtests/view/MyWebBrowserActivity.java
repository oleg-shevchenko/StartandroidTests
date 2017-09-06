package com.example.oleg.startandroidtests.view;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

public class MyWebBrowserActivity extends AppCompatActivity implements View.OnClickListener {
    //<uses-permission android:name="android.permission.INTERNET" />

    WebView myWebView;
    ImageButton btnPrev, btnNext, btnGo;
    EditText etUrl;
    LinearLayout llRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_browser);
        initView();
        initWebView();
    }

    private void initView() {
        myWebView = (WebView) findViewById(R.id.webview);
        llRoot = (LinearLayout) findViewById(R.id.ll_webbrowser_root);

        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnGo = (ImageButton) findViewById(R.id.btnGo);

        etUrl = (EditText) findViewById(R.id.etUrl);
        etUrl.setSelectAllOnFocus(true);
        //Слушаем нажатие кнопки enter на поле ввода
        etUrl.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    openPage(etUrl.getText().toString());
                    return true;
                }
                return false;
            }
        });

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnGo.setOnClickListener(this);
    }

    private void initWebView() {
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //Handling Page Navigation (включить возможность переходить по ссылкам, а не запрашивать браузер)
        //myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebViewClient(new MyWebViewClient());
        if(getIntent().getData() != null) {
            openPage(getIntent().getData().toString());
        }
    }

    //Открыть страницу по адресу
    private boolean openPage(String url) {
        if(url != null) {
            url = comleteUrl(url);
            etUrl.setText(url);
            myWebView.loadUrl(url);
            hideSoftKeyboard();
            checkBackForwardButtons();
            return true;
        } else {
            return false;
        }
    }

    //Проверка введенного URL
    private String comleteUrl(String url) {
        Uri uri = Uri.parse(url);
        //Если нет схемы, то это или поисковой запрос или проверяем наличие в конце точки
        if(uri.getScheme() == null) {
            if(url.length() > 3 && url.lastIndexOf('.') > url.length() - 4 ||
                    url.length() > 4 && url.lastIndexOf('.') > url.length() - 5) {
                uri = Uri.parse("http://" + url);
                return uri.toString();
            } else {
                uri = Uri.parse("https://www.google.com/#q=" + url);
                return uri.toString();
            }
        }
        return url;
    }

    private void hideSoftKeyboard() {
        myWebView.requestFocus();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrev: {
                goBackAction();
                break;
            }
            case R.id.btnNext: {
                goForwardAction();
                break;
            }
            case R.id.btnGo: {
                openPage(etUrl.getText().toString());
                break;
            }
        }
    }



    //If you want more control over where a clicked link load, create your own WebViewClient
    // that overrides the shouldOverrideUrlLoading() method. For example:
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            etUrl.setText(url);
            checkBackForwardButtons();
            hideSoftKeyboard();
            return false;
        }
    }

    //Обработка нажатий на кнопки
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBackAction();
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_FORWARD) {
            goForwardAction();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    //проверяем возможность перехода назад или вперед, также получаем список истории и с помощью текщего индекса
    //получаем предыдущий или следующий URL
    boolean backPressedOnceToExit = false;
    private void goBackAction() {
        //Если есть возможность вернуться назад
        if(myWebView.canGoBack()) {
            WebBackForwardList mWebBackForwardList = myWebView.copyBackForwardList();
            if (mWebBackForwardList.getCurrentIndex() > 0) {
                etUrl.setText(mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl());
            }
            myWebView.goBack();
            checkBackForwardButtons();
        //Иначе запрашиваем выход из активити по двойному нажатию назад
        } else {
            //Если назад была нажата, то выходим из активити
            if(backPressedOnceToExit == true) {
                finish();
                return;
            }
            //Если нажато первый раз, то помечаем тригер и ждем две секунды, потом снимаем отметку
            backPressedOnceToExit = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedOnceToExit =false;
                }
            }, 2000);
        }
    }

    private void goForwardAction() {
        if(myWebView.canGoForward()) {
            WebBackForwardList mWebBackForwardList = myWebView.copyBackForwardList();
            if (mWebBackForwardList.getCurrentIndex() < mWebBackForwardList.getSize() - 1) {
                etUrl.setText(mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() + 1).getUrl());
            }
            myWebView.goForward();
        }
        checkBackForwardButtons();
    }

    private void checkBackForwardButtons() {
        if(myWebView.canGoBack()) btnPrev.setEnabled(true);
        else btnPrev.setEnabled(false);
        if(myWebView.canGoForward()) btnNext.setEnabled(true);
        else btnNext.setEnabled(false);
    }
}
