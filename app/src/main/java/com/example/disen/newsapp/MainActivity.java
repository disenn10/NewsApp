package com.example.disen.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import static android.R.attr.fragment;

public class MainActivity extends AppCompatActivity {

    private static String theme;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ViewPager viewPager;
    FragmentManager mFragmentManager;
    String category;
    Bundle bundle;
    NewsFragmentPagerAdapter menu;
    TabLayout tabLayout;
    LinearLayout drawer;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        theme = sharedPreferences.getString(getString(R.string.key), "Default theme");
        switchtheme();
        setContentView(R.layout.activity_main);
        drawer = (LinearLayout) findViewById(R.id.drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mDrawerList = (ListView) findViewById(R.id.navList);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bundle = new Bundle();
        menu = new NewsFragmentPagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        theme = sharedPreferences.getString(getString(R.string.key), "default");
        bundle.putString("category", "usa");
        menu.passBundle(bundle);
        viewPager.setAdapter(menu);
        tabLayout.setupWithViewPager(viewPager);


        themeup();
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mFragmentManager = getSupportFragmentManager();
    }

    public static String Getheme() {
        String theTheme = theme;
        return theTheme;
    }

    public void switchtheme() {
        switch (theme) {
            case "Rose theme":
                this.setTheme(R.style.RoseTheme);
                break;
            case "Jamaica theme":
                this.setTheme(R.style.JamaicaTheme);
                break;
            default:
                this.setTheme(R.style.NewTheme);
        }
    }

    public void themeup() {
        switch (theme) {
            case "Rose theme":
                drawer.setBackgroundColor(getResources().getColor(R.color.rose));
                tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.roseish));
                tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.rose)));
                break;
            case "Jamaica theme":
                drawer.setBackgroundColor(getResources().getColor(R.color.vert));
                tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.vert));
                tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.noirJ)));
                break;
            default:
                drawer.setBackgroundColor(getResources().getColor(R.color.whiteish));
                tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.sky_blue));
                tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
        }

    }


    private void addDrawerItems() {
        final String[] osArray = {getString(R.string.africa), getString(R.string.ivory), getString(R.string.europe), getString(R.string.usa), getString(R.string.sports), getString(R.string.business), getString(R.string.science), getString(R.string.environment), getString(R.string.stories), getString(R.string.basket)};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category = osArray[position];
                bundle.putString("category", category.toLowerCase());

                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getString(R.string.category));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                menu.passBundle(bundle);
                viewPager.setAdapter(menu);
                tabLayout.setupWithViewPager(viewPager);
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Main_settings.class);
            startActivity(intent);
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
