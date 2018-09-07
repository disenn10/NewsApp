package com.example.disen.newsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by disen on 7/23/2017.
 */

public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {

    private Bundle fragmentBundle;

    public NewsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void passBundle(Bundle value) {
        this.fragmentBundle = value;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                Articles articles = new Articles();
                articles.setArguments(this.fragmentBundle);
                return articles;
            default:
                Videos videos = new Videos();
                videos.setArguments(this.fragmentBundle);
                return videos;

        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return "Articles";
            default:
                return "Videos";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
