package com.example.konekq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout = findViewById(R.id.home_tab_layout);
        viewPager = findViewById(R.id.view_pager2);
        initTabs();
    }

    private void initTabs(){
        HomeTabsAdapter tabsAdapter = new HomeTabsAdapter(this);
        tabsAdapter.addFragment(new NewsFeedFragment(),"Home");
        tabsAdapter.addFragment(new NewsFeedFragment(),"Home");
        tabsAdapter.addFragment(new UserProfileFragment(),"Profile");
        tabsAdapter.addFragment(new NewsFeedFragment(),"Home");
        viewPager.setAdapter(tabsAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}