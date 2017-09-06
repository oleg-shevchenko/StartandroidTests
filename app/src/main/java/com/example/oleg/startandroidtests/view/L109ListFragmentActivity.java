package com.example.oleg.startandroidtests.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.view.fragments.L109ListFragment1;
import com.example.oleg.startandroidtests.view.fragments.L109ListFragment2;

public class L109ListFragmentActivity extends AppCompatActivity {

    ListFragment fragment1, fragment2;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l109_list_fragment);
        //Настраиваем toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle("Test ListFragment");

        initFragments();
    }

    //Создаем меню опций
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.l109_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //При выборе элемента меню опций
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item1:
                replaceFragment(fragment1);
                break;
            case R.id.menu_item2:
                replaceFragment(fragment2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragments() {
        //Создаем фрагменты
        fragment1 = new L109ListFragment1();
        //Помещаем массив строк для отображения во фрагменте. Во фрагменте можем их получить с помощью getArguments().getStringArray("items");
        Bundle fr1Args = new Bundle(1);
        fr1Args.putStringArray("items", new String[] { "one", "two", "three", "four" });
        fragment1.setArguments(fr1Args);

        fragment2 = new L109ListFragment2();
        Bundle fr2Args = new Bundle(1);
        fr2Args.putStringArray("items", new String[] { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten" });
        fragment2.setArguments(fr2Args);

        //Фрагмент по умолчанию
        replaceFragment(fragment1);
    }

    //смена фрагмента
    private void replaceFragment(Fragment fragment) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }
}
