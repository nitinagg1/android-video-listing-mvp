package com.mn2square.videolistingmvp.activity.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.activity.viewpageradapter.ViewPagerAdapter;

/**
 * Created by nitinagarwal on 3/5/17.
 */

public class VideoListingHolderMvpImpl implements ViewMvp, ViewMvpSearch, SearchView.OnClickListener,
        SearchView.OnCloseListener, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener
{

    private View mRootView;
    private SearchView mSearchView;
    private SearchVideo mSearchListener;
    private ListView mListView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private static String TAG = "videolistmvp";

    public VideoListingHolderMvpImpl(Context context, ViewGroup container)
    {

        AppCompatActivity appCompatActivity = (AppCompatActivity)context;
        mRootView = LayoutInflater.from(context).inflate(R.layout.activity_main, container);
        mViewPager = (ViewPager)mRootView.findViewById(R.id.viewpager);
        mViewPager.setCurrentItem(0);
        CharSequence[] titles =
                {context.getResources().getString(R.string.folder_tab_name),
                        context.getResources().getString(R.string.list_tab_name),
                        context.getResources().getString(R.string.saved_tab_name),
//                getResources().getString(R.string.recent_tab_name)
    };
        ViewPagerAdapter viewPagerAdapter =
                new ViewPagerAdapter(appCompatActivity.getSupportFragmentManager(), titles);
        mViewPager.setAdapter(viewPagerAdapter);

        mTabLayout = (TabLayout)mRootView.findViewById(R.id.tablayout);

        mTabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.tool_bar);
        appCompatActivity.setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) mRootView.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                (Activity) context, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) mRootView.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    
    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public Bundle getViewState() {
        //this mvp view has no state that should be retrieved
        return null;
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchListener.onVideoSearched(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSearchListener.onVideoSearched(newText);
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void SetSearchListener(SearchVideo searchListener) {
        mSearchListener = searchListener;

    }

    @Override
    public void AddSearchBar(MenuItem searchViewMenuItem) {
        mSearchView = (SearchView) searchViewMenuItem.getActionView();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        
    }

    @Override
    public void searchClose() {
        mSearchView.setQuery("", false);
        mSearchListener.onVideoSearched("");
        mSearchView.onActionViewCollapsed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) mRootView.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
