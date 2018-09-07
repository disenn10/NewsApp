package com.example.disen.newsapp;

/**
 * Created by disen on 7/25/2017.
 */

public class News {

    private String title;
    private String section;
    private String WebUrl;
    private String type;

    public News(String title, String section, String WebUrl, String type) {
        this.title = title;
        this.section = section;
        this.type = type;
        this.WebUrl = WebUrl;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getWebUrl() {
        return WebUrl;
    }
}
