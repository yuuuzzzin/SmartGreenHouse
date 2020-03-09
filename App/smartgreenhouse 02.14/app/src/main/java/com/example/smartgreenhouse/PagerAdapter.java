package com.example.smartgreenhouse;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.smartgreenhouse.ui.graph.GraphFragmentTab1;
import com.example.smartgreenhouse.ui.graph.GraphFragmentTab2;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                GraphFragmentTab1 tab1 = new GraphFragmentTab1();
                return tab1;
            case 1:
                GraphFragmentTab2 tab2 = new GraphFragmentTab2();
                return tab2;
//            case 2:
//                TabFragment3 tab3 = new TabFragment3();
//                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}