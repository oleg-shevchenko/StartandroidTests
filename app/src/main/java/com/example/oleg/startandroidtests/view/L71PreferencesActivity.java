package com.example.oleg.startandroidtests.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.view.prefences.DatePreference;

import java.util.Calendar;

public class L71PreferencesActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener{

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display fragment as main content
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //регистрируем наше активити слушателем изменений SharedPreferences для отлова выбора файла (просто для примера)
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //удаляем из слушателей
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    //Слушаем изменение ключа myfile в SharedPreferences  (просто для примера)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("myfile")) {
            Uri uri = Uri.parse(sharedPreferences.getString(key, ""));
            Toast.makeText(this, "Choosed file: " + uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
        }
    }



    //Наш главный фрагмент
    public static class PrefsFragment extends PreferenceFragment {
        Preference filePicker;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.l71_pref1);

            //Динамическое заполнение ListPreference
            ListPreference lp = (ListPreference) findPreference("my_prefs_list");
            lp.setEntries(new String[]{"One", "Two", "Three"});
            lp.setEntryValues(new String[]{"1", "2", "3"});

            //Присваиваем слушатель изменения нужной настройки, в нем обрабатываем поле summary
            bindPreferenceSummaryToValue(lp);
            bindPreferenceSummaryToValue(findPreference("address"));
            initFilePicker();
            bindPreferenceSummaryToValue(filePicker);

            DatePreference dp = (DatePreference) findPreference("myDate");
            dp.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
            //bindPreferenceSummaryToValue(dp);
            sBindPreferenceSummaryToValueListener.onPreferenceChange(dp, PreferenceManager.getDefaultSharedPreferences(dp.getContext()).getLong(dp.getKey(), 0));
        }

        final int CHOOSE_FILE_REQUEST_CODE = 1;

        private void initFilePicker() {
            //File picker
            filePicker = findPreference("myfile");
            //При нажатии на поле открываем выбор файлов
            filePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("file/*");
                    //intent.addCategory(Intent.CATEGORY_OPENABLE);
                    try {
                        startActivityForResult(Intent.createChooser(intent, "Choose file..."), CHOOSE_FILE_REQUEST_CODE);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getActivity().getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            //обрабатываем результат выбора файла
            if(requestCode == CHOOSE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
                String path = data.getDataString();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("myfile", path);
                editor.apply();
                filePicker.setSummary(path);
            }
            else super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Слушатель изменения настроек
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            } else if(preference instanceof DatePreference) {
                if(((Long)value) == 0) return false;
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis((Long) value);
                String year = String.valueOf(cal.get(Calendar.YEAR));
                int m = cal.get(Calendar.MONTH);
                int d = cal.get(Calendar.DAY_OF_MONTH);
                String month = m > 8 ? String.valueOf(m + 1) : "0" + (m + 1);
                String day = d > 9 ? String.valueOf(d) : "0" + d;
                preference.setSummary(year + "." + month + "." + day);
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    //Присваиваем слушатель изменения настройки, в нем обрабатываем поле summary
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        String defSummary = preference.getSummary() != null ? preference.getSummary().toString() : "";

        // Trigger the listener immediately with the preference's current value.
        //Т.е сразу вызываем метод listener-а, который установит нужное значение поля summary
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), defSummary));
    }

}


