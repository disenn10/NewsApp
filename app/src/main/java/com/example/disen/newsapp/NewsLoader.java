package com.example.disen.newsapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by disen on 7/25/2017.
 */

public class NewsLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<News>> {
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public ArrayList<News> loadInBackground() {
        if (url == null) {
            return null;
        }
        ArrayList<News> news = Utils.FetchData(url);
        return news;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
