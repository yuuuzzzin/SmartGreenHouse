package com.example.smartgreenhouse.ui.Account;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.smartgreenhouse.R;
import com.google.android.material.tabs.TabLayout;

public class FindAccountActivity extends AppCompatActivity {

    CustomFragmentPagerAdapter mCustomFragmentPagerAdapter;
    ViewPager mViewPager;
    TabLayout slidingTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        mCustomFragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.findviewPager);
        mViewPager.setAdapter(mCustomFragmentPagerAdapter);
        slidingTabs = (TabLayout) findViewById(R.id.slidingTabs);

        slidingTabs.addTab(slidingTabs.newTab().setText("ID"), 0, true); // 페이지 등록
        slidingTabs.addTab(slidingTabs.newTab().setText("PW"), 1, true);

        slidingTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition()); // 슬라이딩이 아니라 위에 페이지를 선택했을 때도 페이지 이동 가능하게.
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(slidingTabs));
    }
}
