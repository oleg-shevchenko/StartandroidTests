package com.example.oleg.startandroidtests.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

/**
 * Created by Oleg on 08.07.2017.
 */

public class L71MainActivity extends AppCompatActivity {
    TextView tvInfo;
    SharedPreferences sp;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l71_main);

        tvInfo = (TextView) findViewById(R.id.tvInfo);

        // получаем SharedPreferences, которое работает с файлом настроек
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        // полная очистка настроек
        // sp.edit().clear().commit();
    }

    protected void onResume() {
        Boolean notif = sp.getBoolean("notif", false);
        String address = sp.getString("address", "");
        String text = "Notifications are " + ((notif) ? "enabled, address = " + address : "disabled");
        tvInfo.setText(text);
        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(0, 1, 0, "Preferences");
        mi.setIntent(new Intent(this, L71PreferencesActivity.class));
        return super.onCreateOptionsMenu(menu);
    }
}
