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
            case 0 : //  MainActivity.java에서 Tab에서의 탭 번호
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
