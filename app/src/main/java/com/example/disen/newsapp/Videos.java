package com.example.disen.newsapp;


import android.app.LoaderManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.view.WindowCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Videos extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<News>>, SwipeRefreshLayout.OnRefreshListener {
    android.support.v4.app.LoaderManager loaderManager;
    NewsAdapter newsAdapter;
    ListView listView;
    LinearLayout llayout;
    String category;
    String site;
    int loaderID;
    Bundle bundle;
    ProgressBar progressBar;
    TextView textView;
    SwipeRefreshLayout refreshLayout;


    public Videos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_videos, container, false);
        //Inialize variables
        loaderManager = getLoaderManager();
        bundle = getArguments();
        refreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swiperefresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(this);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progress_videos);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        llayout = (LinearLayout) rootview.findViewById(R.id.no_videos);
        textView = (TextView) rootview.findViewById(R.id.no_internet);
        listView = (ListView) rootview.findViewById(R.id.videos);
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(getActivity().getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //Check network connection
        if (networkInfo != null && networkInfo.isConnected()) {
            displaycontent();
        } else {
            display_noInternet();
        }
        return rootview;
    }

    //display no internet connection once called
    private void display_noInternet() {
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }

    //return date of today
    private String getDateString(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(timeInMilliseconds);
    }

    //destroy previous loader
    public void destroyer(int id) {
        if (id != 1) {
            for (int i = 1; i < id; i++) {
                loaderManager.destroyLoader(i);
            }
        }
    }

    //display news content
    public void displaycontent() {
        loaderID += 1;
        destroyer(loaderID);
        if (bundle != null) {
            category = bundle.getString("category");
        }
        if (category != null) {
            site = buildQuery(category);
            loaderManager.initLoader(loaderID, null, this);
        }
    }

    //update the ui once data are returned
    public void updateUI(final ArrayList<News> news) {
        newsAdapter = new NewsAdapter(getContext(), news);
        listView.setAdapter(newsAdapter);
        progressBar.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News video = news.get(position);
                String web = video.getWebUrl();
                Uri uri = Uri.parse(web);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    //build query based on categoty chosen by the user
    public String buildQuery(String section) {
        long time = System.currentTimeMillis();
        String date = getDateString(time);
        switch (section) {
            case "africa":
                section = "http://content.guardianapis.com/tags?q=" + category + "&to-date=" + date + "&type=video&api-key=test";
                break;
            case "basketball":
                section = "http://content.guardianapis.com/tags?q=" + category + "&to-date=" + date + "&type=video&api-key=test";
                break;
            case "europe":
                section = "http://content.guardianapis.com/tags?q=" + category + "&to-date=" + date + "&type=video&api-key=test";
                break;
            case "usa":
                section = "https://content.guardianapis.com/search?tag=politics/politics&type=video&api-key=test";
                break;
            case "business":
                section = "https://content.guardianapis.com/search?tag=business/business&type=video&api-key=test";
                break;
            case "sports":
                section = "https://content.guardianapis.com/search?tag=football/football&type=video&api-key=test";
                break;
            case "environment":
                section = "https://content.guardianapis.com/search?tag=environment/recycling&type=video&api-key=test";
                break;
            case "science":
                section = "https://content.guardianapis.com/search?tag=science/science&type=video&api-key=test";
                break;
            case "stories":
                section = "http://content.guardianapis.com/tags?q=" + category + "&type=video&to-date=" + date + "&api-key=test";
                break;
            case "ivory coast":
                section = "https://content.guardianapis.com/search?q="+category+"&type=video&to-date=" + date + "&api-key=test";
                break;
            default:
                section = "http://content.guardianapis.com/tags?q=World&to-date=" + date + "&type=video&api-key=test";
        }
        return section;
    }


    @Override
    public android.support.v4.content.Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(getContext(), site);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<News>> loader, ArrayList<News> data) {
        refreshLayout.setRefreshing(false);
        if (data != null) {
            if (data.size() == 0) {
                progressBar.setVisibility(View.GONE);
                llayout.setVisibility(View.VISIBLE);
            } else {
                updateUI(data);
            }
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<News>> loader) {
        if (newsAdapter != null) {
            newsAdapter.clear();
        }

    }

    @Override
    public void onRefresh() {
        loaderManager.restartLoader(loaderID, null, this);
    }
}
