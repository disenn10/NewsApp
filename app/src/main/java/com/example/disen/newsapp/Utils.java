package com.example.disen.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by disen on 7/24/2017.
 */

public class Utils {

    public static ArrayList<News> FetchData(String webUrl) {
        if (webUrl == null) {
            return null;
        }
        URL url = createURL(webUrl);
        String json = makeHttpRequest(url);
        ArrayList<News> news = extractNews(json);
        return news;
    }

    public static URL createURL(String url) {
        URL newURL = null;
        if (url.length() != 0) {
            try {
                newURL = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return newURL;
    }

    public static String makeHttpRequest(URL url) {
        String json = "";
        if (url == null) {
            return null;
        }
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                json = readFromJson(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    public static String readFromJson(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        String line = "";
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        line = reader.readLine();
        while (line != null) {
            output.append(line);
            line = reader.readLine();
        }
        return output.toString();
    }

    public static ArrayList<News> extractNews(String json) {
        ArrayList<News> newsArrayList = new ArrayList<>();
        String section = "";
        String type = "";
        if (json != null) {
            try {
                JSONObject newsjsonObject = new JSONObject(json);
                JSONObject json_result = newsjsonObject.getJSONObject("response");
                JSONArray newsArray = json_result.getJSONArray("results");
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject newsObject = newsArray.getJSONObject(i);
                    String title = newsObject.getString("webTitle");
                    if (newsObject.has("sectionName")) {
                        section = newsObject.getString("sectionName");
                    }
                    type = newsObject.getString("type");
                    String web = newsObject.getString("webUrl");
                    News news = new News(title, section, web, type);
                    newsArrayList.add(news);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newsArrayList;
    }
}
