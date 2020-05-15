package com.example.smartgreenhouse.ui.graph;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ChartFragmentPagerAdapter extends FragmentPagerAdapter {

    private static int PAGE_NUMBER = 3;

    public ChartFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0 : //  MainActivity.java에서 Tab에서의 탭 번호
                return new ChartTempFragment();
            case 1 :
                return new ChartHumiFragment();
            case 2 :
                return new ChartLevelFragment();
            default:
                break;
        }
        return null;
    }


    @Override
    public int getCount() {
        return PAGE_NUMBER; // 원하는 페이지 수
    }
}
