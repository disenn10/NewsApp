package com.example.disen.newsapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Articles extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<News>>, SwipeRefreshLayout.OnRefreshListener {


    android.support.v4.app.LoaderManager loaderManager;
    NewsAdapter newsAdapter;
    ListView listView;
    String category;
    String site;
    int loaderID;
    Bundle bundle;
    ProgressBar progressBar;
    TextView textView;
    SwipeRefreshLayout refreshLayout;

    public Articles() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_news, container, false);
        //Instantiate variables
        loaderManager = getLoaderManager();
        bundle = getArguments();
        listView = (ListView) rootview.findViewById(R.id.newsID);
        textView = (TextView) rootview.findViewById(R.id.no_internet);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progress_videos);
        refreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swiperefresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(this);
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(getActivity().getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //check internet connection before displaying content
        if (networkInfo != null && networkInfo.isConnected()) {
            displaycontent();
        } else {
            display_noInternet();
        }
        return rootview;
    }

    //display no internet when called
    private void display_noInternet() {
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }

    //get date of today
    private String getDateString(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(timeInMilliseconds);
    }

    //load content when called
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

    //destroy previous loader
    public void destroyer(int id) {
        if (id != 1) {
            for (int i = 1; i < id; i++) {
                loaderManager.destroyLoader(i);
            }
        }
    }

    //Update the user interface
    public void updateUI(final ArrayList<News> news) {
        newsAdapter = new NewsAdapter(getContext(), news);
        listView.setAdapter(newsAdapter);
        progressBar.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News item = news.get(position);
                String web = item.getWebUrl();
                Uri uri = Uri.parse(web);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    public String buildQuery(String section) {
        long time = System.currentTimeMillis();
        String date = getDateString(time);
        switch (section) {
            case "africa":
                section = "https://content.guardianapis.com/search?q="+category+"&to-date=" + date + "&api-key=test";
                break;
            case "basketball":
                section = "https://content.guardianapis.com/search?q="+category+"&to-date=" + date + "&api-key=test";
                break;
            case "europe":
                section = "https://content.guardianapis.com/search?q="+category+"&to-date=" + date + "&api-key=test";
                break;
            case "usa":
                section = "https://content.guardianapis.com/search?q="+category+"&to-date=" + date + "&api-key=test";
                break;
            case "business":
                section = "https://content.guardianapis.com/search?q=debate&tag=" + category + "/" + category + "&to-date=" + date + "&api-key=test";
                break;
            case "sports":
                section = "https://content.guardianapis.com/search?q="+category+"&to-date=" + date + "&api-key=test";
                Log.e(Articles.class.getSimpleName(), ""+ section );
                break;
            case "environment":
                section = "https://content.guardianapis.com/search?q=debate&tag=" + category + "/" + category + "&to-date=" + date + "&api-key=test";

                break;
            case "science":
                section = "https://content.guardianapis.com/search?q="+category+"&to-date=" + date + "&api-key=test";
                break;
            case "stories":
                section = "https://content.guardianapis.com/search?q="+category+"&to-date=" + date + "&api-key=test";
                break;
            case "ivory coast":
                section = "https://content.guardianapis.com/search?q="+category.replace(" ","+")+"&to-date=" + date + "&api-key=test";
                break;
            default:
                section = "https://content.guardianapis.com/search?q=world&to-date=" + date + "&api-key=test";
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
            updateUI(data);
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
