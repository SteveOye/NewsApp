package com.example.android.newsapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.newsapp.Fragments.LifestyleFragment;
import com.example.android.newsapp.Fragments.NewsFragment;
import com.example.android.newsapp.Fragments.SportFragment;
import com.example.android.newsapp.Fragments.TechnologyFragment;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NewsFragment();
        } else if (position == 1) {
            return new TechnologyFragment();
        } else if (position == 2) {
            return new SportFragment();
        } else {
            return new LifestyleFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.news);
        } else if (position == 1){
            return mContext.getString(R.string.technology);
        } else if (position == 2){
            return mContext.getString(R.string.sport);
        } else {
            return mContext.getString(R.string.lifestyle);
        }
    }
}
