package com.example.konekq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ImageButton btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout = findViewById(R.id.home_tab_layout);
        viewPager = findViewById(R.id.view_pager2);
        btnMenu = findViewById(R.id.btn_menu);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MenuActivity.class));
            }
        });
        initTabs();
    }

    private void initTabs(){
        HomeTabsAdapter tabsAdapter = new HomeTabsAdapter(this);
        tabsAdapter.addFragment(new NewsFeedFragment(),"Home");
        tabsAdapter.addFragment(new FriendsFragment(),"Friends");
        tabsAdapter.addFragment(new UserProfileFragment(),"Profile");
        tabsAdapter.addFragment(new NotificationsFragment(),"Notifications");
        viewPager.setAdapter(tabsAdapter);

        //tabs
        TabItemModel[] tabModels = {
                new TabItemModel(R.drawable.home_icon,0),
                new TabItemModel(R.drawable.friends_icon,0),
                new TabItemModel(R.drawable.person_icon,0),
                new TabItemModel(R.drawable.bell_icon,8),
        };

        for (int x = 0;x<tabModels.length;x++){

            TabItemModel tabItem = tabModels[x];

            TabLayout.Tab tab = tabLayout.getTabAt(x);

            View view = LayoutInflater.from(this).inflate(R.layout.tab_item_view_with_badge,null);
            ImageView icon = (ImageView) view.findViewById(R.id.tab_item_icon);
            icon.setImageDrawable(getResources().getDrawable(tabItem.getIcon()));

            TextView badge = (TextView) view.findViewById(R.id.text_view_badge);

            if(tabItem.getBadgeValue() > 0){
                badge.setText(tabItem.getBadgeValue() + "");
                badge.setVisibility(View.VISIBLE);
            }
            tab.setCustomView(view);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                clearTabIndicators();
                setActiveTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(@NonNull View v) {

            }

            @Override
            public void onViewDetachedFromWindow(@NonNull View v) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));

                clearTabIndicators();
                setActiveTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    public void clearTabIndicators(){
        for(int x=0;x<tabLayout.getTabCount();x++){
            TabLayout.Tab tab = tabLayout.getTabAt(x);
            //get tab's custom view
            View view = tab.getCustomView();

            //change icon's color
            ImageView icon = (ImageView) view.findViewById(R.id.tab_item_icon);
            icon.setImageTintList(null);

            //show indicator
            view.findViewById(R.id.tab_indicator).setVisibility(View.GONE);
        }
    }

    private void setActiveTab(int index){
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        //get tab's custom view
        View view = tab.getCustomView();

        //change icon's color
        ImageView icon = (ImageView) view.findViewById(R.id.tab_item_icon);
        icon.getDrawable().setTint(getResources().getColor(R.color.blue));

        //show indicator
        view.findViewById(R.id.tab_indicator).setVisibility(View.VISIBLE);
    }
}