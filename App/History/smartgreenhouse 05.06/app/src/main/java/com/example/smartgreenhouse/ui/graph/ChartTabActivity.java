package com.example.smartgreenhouse.ui.graph;

import android.os.Bundle;
import android.view.MenuItem;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.smartgreenhouse.R;
import com.google.android.material.tabs.TabLayout;

public class ChartTabActivity extends AppCompatActivity {

    ChartFragmentPagerAdapter ChartFragmentPagerAdapter;
    ViewPager mViewPager;
    TabLayout slidingTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_tab);
        ChartFragmentPagerAdapter mChartFragmentPagerAdapter = new ChartFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.findviewPager);
        mViewPager.setAdapter(mChartFragmentPagerAdapter);
        slidingTabs = (TabLayout) findViewById(R.id.slidingTabs);

        slidingTabs.addTab(slidingTabs.newTab().setText("온도"), 0, true); // 페이지 등록
        slidingTabs.addTab(slidingTabs.newTab().setText("습도"), 1, false);
        slidingTabs.addTab(slidingTabs.newTab().setText("수위"), 2, false);


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
