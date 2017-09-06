package com.example.oleg.startandroidtests.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

public class IntentTests extends AppCompatActivity implements View.OnClickListener {

    TextView tvIntentTest1, tvIntentTest2, tvIntentTest3, tvIntentTest4, tvIntentTest5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_tests);
        initView();
    }

    private void initView() {
        tvIntentTest1 = (TextView) findViewById(R.id.tvIntentTest1);
        tvIntentTest2 = (TextView) findViewById(R.id.tvIntentTest2);
        tvIntentTest3 = (TextView) findViewById(R.id.tvIntentTest3);
        tvIntentTest4 = (TextView) findViewById(R.id.tvIntentTest4);
        tvIntentTest5 = (TextView) findViewById(R.id.tvIntentTest5);
        tvIntentTest1.setOnClickListener(this);
        tvIntentTest2.setOnClickListener(this);
        tvIntentTest3.setOnClickListener(this);
        tvIntentTest4.setOnClickListener(this);
        tvIntentTest5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tvIntentTest1: {
//                intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","abc@gmail.com", null));
//                //intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"oleg.sh.vi@gmail.com"});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Test subject");
//                intent.putExtra(Intent.EXTRA_TEXT, "Hello!");
//                startActivity(Intent.createChooser(intent, "Send email..."));
                composeEmail(new String[]{"oleg.sh.vi@gmail.com"}, "New Test");
                break;
            }
            case R.id.tvIntentTest2: {
                testStartActivityForResult();
                break;
            }
            //Dial
            case R.id.tvIntentTest3: {
                testDial();
                break;
            }
            //Call
            case R.id.tvIntentTest4: {
                testCall();
                break;
            }
            case R.id.tvIntentTest5: {
                testOpenMap();
                break;
            }

        }
    }

    //отправка email
    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses); //String[]
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, "Hello!");
        //Проверка на наличие активити с допустимым интент-фильтром и запуск
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        //Можно просто вызвать чузер, если доступных активити нет, то он напишет
        //Т.е здесь принудительный вызов блока выбора
        //startActivity(Intent.createChooser(intent, "Send email..."));
    }

    //открыть номеронабиратель с номером
    private void testDial() {
        String number = "+380958560675";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        startActivity(Intent.createChooser(intent, "Call " + number));
    }

    //позвонить по номеру
    //    <uses-permission android:name="android.permission.CALL_PHONE"/>
    //    <uses-feature android:name="android.hardware.telephony" android:required="false" />
    private void testCall() {
        String number = "+380958560675";
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));

        //проверка разрешения (сгенерированый метод!)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, 1234);
            return;
        }
        startActivity(intent);
    }

    //Проверка разрешения после запроса и дальнейшие действия
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1234: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    testCall();
                } else {
                    Toast.makeText(this, "Permission not greanted!", Toast.LENGTH_LONG);
                }
                return;
            }
        }
    }

    //Google map
    private void testOpenMap() {
        String scheme = "geo";
        String latitude = "50.4514403";
        String longitude = "30.628536";
        String zoom = "15";
        String uri = scheme + ":" + latitude + "," + longitude + "?zoom=" + zoom;
        //uri = "geo:50.4514403,30.628536?zoom=15";
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri)); //через конструктор
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        startActivity(Intent.createChooser(intent, "Open map..."));
    }

    private final int REQ_CODE_1 = 1234;

    private void testStartActivityForResult() {
        Intent intent = new Intent(this, IntentTestActivityForResult.class);
        startActivityForResult(intent, REQ_CODE_1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) return;
        if(requestCode == REQ_CODE_1 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.intent_tests_root), name, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
