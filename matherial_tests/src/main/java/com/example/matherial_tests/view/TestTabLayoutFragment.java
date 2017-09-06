package com.example.matherial_tests.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matherial_tests.R;
import com.example.matherial_tests.view.other.PeopleFragment;

import java.util.ArrayList;
import java.util.List;

public class TestTabLayoutFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public TestTabLayoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test_tab_layout, container, false);
        initToolbar(v);
        initView(v);
        return v;
    }

    //Определяем toolbar (напрямую к примеру не относится)
    private void initToolbar(View v) {
        toolbar = (Toolbar) v.findViewById(R.id.main_toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        MainNavigationDrawerActivity activity = MainNavigationDrawerActivity.instance;
        activity.setSupportActionBar(toolbar);

        //Инициализируем "бутерброд"
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), activity.drawer, toolbar, R.string.str_open_drawer, R.string.str_close_drawer);
        activity.drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Инициализируем ViewPager и TabLayout
    private void initView(View v) {
        //Определяем наш ViewPager
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        //Создаем FragmentStatePagerAdapter и добавляем нужные нам фрагметы и их заголовки
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new PeopleFragment().setCenterText("People"), "People");
        adapter.addFragment(new PeopleFragment().setCenterText("Group"), "Group");
        adapter.addFragment(new PeopleFragment().setCenterText("Calls"), "Calls");
        //Устанавливаем адаптер для ViewPager
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) v.findViewById(R.id.tablayout);
        //Инициализируем TabLayout с помощью ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    //PagerAdapter – это базовый абстрактный класс, для которого разработчик дописывает реализацию так, как ему надо.
    //Существует распространенная стандартная (частичная) реализация PagerAdapter, которая работает с фрагментами – это FragmentPagerAdapter.
    //Разработчику остается только создать фрагмент и определить кол-во страниц.
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        //Список фрагментов
        private final List<Fragment> mFragmentList = new ArrayList<>();
        //Список заголовков
        private final List<String> mFragmentTitleList = new ArrayList<>();

        //Конструктор
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //Получить фрагмент по индексу
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        //получить колличество фрагментов
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        //Получить заголовок по индексу
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        //С помощью этого метода добавляем фрагменты в List, а также их названия
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
