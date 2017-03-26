package com.mn2square.videolistingmvp.activity.views;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.activity.viewpageradapter.ViewPagerAdapter;

/**
 * Created by nitinagarwal on 3/5/17.
 */

public class VideoListingHolderMvpImpl implements ViewMvp, ViewMvpSearch, SearchView.OnClickListener,
        SearchView.OnCloseListener, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener,
        TabLayout.OnTabSelectedListener
{

    private View mRootView;
    private SearchView mSearchView;
    private SearchVideo mSearchListener;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private Context mContext;
    Window mWindow;

    private static String TAG = "videolistmvp";

    public VideoListingHolderMvpImpl(Context context, ViewGroup container)
    {

        mContext = context;
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
        mViewPager.setCurrentItem(1);

        mWindow = ((AppCompatActivity) context).getWindow();

        mTabLayout = (TabLayout)mRootView.findViewById(R.id.tablayout);

        mAppBarLayout = (AppBarLayout)mRootView.findViewById(R.id.appBarLayout);

        mTabLayout.setupWithViewPager(mViewPager);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mTabLayout.addOnTabSelectedListener(this);

        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.tool_bar);
        appCompatActivity.setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) mRootView.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                (Activity) context, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) mRootView.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fabRecordCameraVideo = (FloatingActionButton)mRootView.findViewById(R.id.fab_video_record);
        fabRecordCameraVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"fab clicked", Toast.LENGTH_SHORT).show();
            }
        });

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

        if (id == R.id.nav_settings) {
            Toast.makeText(mContext, "settings Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(mContext, "gallery Clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_help) {
            Toast.makeText(mContext, "hep Clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(mContext, "share Clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_buy_pro) {
            Toast.makeText(mContext, "buy pro Clicked", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) mRootView.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition())
        {
            case 0:
                mAppBarLayout.setBackgroundResource(R.color.black_dialog_header);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    mWindow.setStatusBarColor(mContext.getResources().getColor(R.color.transparent_black));
                break;
            case 1:
                mAppBarLayout.setBackgroundResource(R.color.colorPrimary);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    mWindow.setStatusBarColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                break;

            case 2:
                mAppBarLayout.setBackgroundResource(R.color.primary);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    mWindow.setStatusBarColor(mContext.getResources().getColor(R.color.primaryDark));
                break;

            default:
                mAppBarLayout.setBackgroundResource(R.color.black_dialog_header);

        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
