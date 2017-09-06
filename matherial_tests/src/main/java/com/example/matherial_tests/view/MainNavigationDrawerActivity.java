package com.example.matherial_tests.view;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.matherial_tests.R;

public class MainNavigationDrawerActivity extends AppCompatActivity {

    static MainNavigationDrawerActivity instance;

    DrawerLayout drawer;
    NavigationView navigationView;
    Fragment frTestCoordinator1, frBottomSheet, frModalBottomSheet, frSwipeCardView, frCustomBehavior, frTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main_navigation_drawer);
        initDrawer();
        initFragments();
    }

    //инициализация дравера
    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //Обработка нажатий меню NavigationView
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                switch (item.getItemId()) {
                    case R.id.nav_coord1:
                        replaceFragment(frTestCoordinator1);
                        break;
                    case R.id.nav_bottom_sheet:
                        replaceFragment(frBottomSheet);
                        break;
                    case R.id.nav_modal_bottom_sheet:
                        replaceFragment(frModalBottomSheet);
                        break;
                    case R.id.nav_card_swipe_behavior:
                        replaceFragment(frSwipeCardView);
                        break;
                    case R.id.nav_custom_behavior:
                        replaceFragment(frCustomBehavior);
                        break;
                    case R.id.nav_tablayout:
                        replaceFragment(frTabLayout);
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //открыть при запуске
        drawer.openDrawer(GravityCompat.START);
        //Также нужно установить ActionBarDrawerToggle для туллбара, чтобы показывался "бутерброд".
        //Т.к. у нас разные туллбары во фрагментах, то будем делать это там
    }

    //Инициализируем фрагменты
    private void initFragments() {
        frTestCoordinator1 = new TestCoordinator1Fragment();
        frBottomSheet = new TestBottomSheetFragment();
        frModalBottomSheet = new TestModalBottomSheetFragment();
        frSwipeCardView = new TestCardViewAndSwipeDismissBehaviorFragment();
        frCustomBehavior = new TestCoordLayBehavior();
        frTabLayout = new TestTabLayoutFragment();
        replaceFragment(frTestCoordinator1);
    }

    //Смена фрагмента
    private void replaceFragment(final Fragment fragment) {
        if(fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.base_fragment_content, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation_drawer_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        //Закрытие drawer при нажатии кнопки назад
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }
}
