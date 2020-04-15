package com.example.smartgreenhouse.ui.Account;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

    private static int PAGE_NUMBER = 2;

    public CustomFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0 : // 여기 서의 숫자와 MainActivity.java에서 Tab에서탭 번호가 매칭이되느 겁니다.
                return new Tab_findID();
            case 1 :
                return new Tab_findPW();
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
