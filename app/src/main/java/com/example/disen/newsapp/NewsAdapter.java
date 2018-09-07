package com.example.disen.newsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by disen on 7/25/2017.
 */

public class NewsAdapter extends ArrayAdapter {
    String type;

    public NewsAdapter(Context context, ArrayList<News> resource) {
        super(context, 0, resource);
    }

    public static class NewsViewHolder {
        TextView title;
        TextView section;
        ImageView image;

        public NewsViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            section = (TextView) view.findViewById(R.id.section);
            image = (ImageView) view.findViewById(R.id.image);
        }

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_news, parent, false);
            viewHolder = new NewsViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NewsViewHolder) convertView.getTag();
        }
        News currentNews = (News) getItem(position);
        viewHolder.section.setText(currentNews.getSection());
        viewHolder.title.setText(currentNews.getTitle());
        type = currentNews.getType().trim();
        applytheme(viewHolder.title, viewHolder.section, viewHolder.image);
        viewHolder.image.setImageResource(assignImage());
        return convertView;
    }

    //assign image based on which fragment the user is on
    public int assignImage() {
        int resource = R.drawable.ic_keyboard_arrow_right_black_24dp;
        ;
        if (type.equals("video")) {
            resource = R.drawable.ic_play_circle_filled_black_24dp;
        } else if (type.equals("article")) {
            resource = R.drawable.ic_keyboard_arrow_right_black_24dp;
        }
        return resource;
    }

    //change colors based on theme chosen
    public void applytheme(TextView title, TextView section, ImageView image) {
        String theme = MainActivity.Getheme().trim();
        switch (theme) {
            case "Rose theme":
                getContext().setTheme(R.style.RoseTheme);
                title.setTextColor(getContext().getResources().getColor(R.color.roseish));
                section.setTextColor(getContext().getResources().getColor(R.color.red_rose));
                image.setColorFilter(getContext().getResources().getColor(R.color.red_rose));
                break;
            case "Jamaica theme":
                title.setTextColor(getContext().getResources().getColor(R.color.noirJ));
                section.setTextColor(getContext().getResources().getColor(R.color.vert));
                image.setColorFilter(getContext().getResources().getColor(R.color.banana));
                break;
            default:
                title.setTextColor(getContext().getResources().getColor(R.color.noirJ));
                section.setTextColor(getContext().getResources().getColor(R.color.sky_blue));
                image.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));

        }
    }

}
