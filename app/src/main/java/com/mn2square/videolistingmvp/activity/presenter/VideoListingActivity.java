package com.mn2square.videolistingmvp.activity.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.utils.FolderListGenerator;
import com.mn2square.videolistingmvp.utils.VideoSearch;
import com.mn2square.videolistingmvp.swipetabfragments.ListFragement.presenter.ListFragment;
import com.mn2square.videolistingmvp.activity.model.VideoListInfo;
import com.mn2square.videolistingmvp.activity.model.VideoListManager;
import com.mn2square.videolistingmvp.activity.model.VideoListManagerImpl;
import com.mn2square.videolistingmvp.swipetabfragments.SavedListFragment.presenter.SavedListFragment;
import com.mn2square.videolistingmvp.swipetabfragments.folderlistfragment.presenter.FolderListFragment;
import com.mn2square.videolistingmvp.activity.views.VideoListingHolderMvpImpl;
import com.mn2square.videolistingmvp.activity.views.ViewMvpSearch;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VideoListingActivity extends AppCompatActivity
        implements VideoListManager.VideoListManagerListener, ObservableScrollViewCallbacks{

    private static final String SORT_TYPE_PREFERENCE_KEY = "sort_type";
    private int mSortingType;
    public static final int NAME_ASC = 0;
    public static final int NAME_DESC = 1;
    public static final int DATE_ASC = 2;
    public static final int DATE_DESC = 3;
    public static final int SIZE_ASC = 4;
    public static final int SIZE_DESC = 5;

    VideoListingHolderMvpImpl mVideoListingHolderMvpImpl;
    VideoListManagerImpl mVideoListManagerImpl;
    ViewPager mViewPager;
    ListFragment mListFragment;
    SavedListFragment mSavedListFragment;
    FolderListFragment mFolderListFragment;
    VideoListInfo mVideoListInfo;

    boolean mIsInSearchMode;
    String mSearchText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVideoListingHolderMvpImpl = new VideoListingHolderMvpImpl(this, null);

        setContentView(mVideoListingHolderMvpImpl.getRootView());

        mViewPager = mVideoListingHolderMvpImpl.getViewPager();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        mSortingType = settings.getInt(SORT_TYPE_PREFERENCE_KEY, 3);
        mVideoListManagerImpl = new VideoListManagerImpl(this, mSortingType);
        mVideoListManagerImpl.registerListener(this);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mVideoListingHolderMvpImpl.AddSearchBar(menu.findItem(R.id.action_search));

        ViewMvpSearch.SearchVideo searchVideoListener = new ViewMvpSearch.SearchVideo() {
            @Override
            public void onVideoSearched(String seachText) {

                mSearchText = seachText;
                if(seachText.trim().equals(""))
                {
                    mIsInSearchMode = false;
                    mVideoListInfo.getVideosList().clear();
                    mVideoListInfo.getVideosList().addAll(mVideoListInfo.getVideoListBackUp());
                    mVideoListInfo.getFolderListHashMap().clear();
                    mVideoListInfo.getFolderListHashMap().putAll(mVideoListInfo.getFolderListHashMapBackUp());

                }
                else
                {
                    mIsInSearchMode = true;

                    mVideoListInfo.setVideosList(VideoSearch.SearchResult(seachText, mVideoListInfo.getVideoListBackUp()));
                    mVideoListInfo.setFolderListHashMap(VideoSearch.SearchResult(seachText,
                            mVideoListInfo.getFolderListHashMapBackUp()));
                }

                mVideoListInfo.setSavedVideoList(FolderListGenerator.getSavedVideoListFromFolderHashMap(
                                              mVideoListInfo.getFolderListHashMap()));

                if(mListFragment != null)
                    mListFragment.bindVideoList(mVideoListInfo.getVideosList(), mVideoListInfo);
                if(mFolderListFragment != null)
                    mFolderListFragment.bindVideoList(mVideoListInfo.getFolderListHashMap(), mVideoListInfo);
                if(mSavedListFragment != null)
                    mSavedListFragment.bindVideoList(mVideoListInfo.getSavedVideoList(), mVideoListInfo);
            }
        };
        mVideoListingHolderMvpImpl.SetSearchListener(searchVideoListener);

        setSortingOptionChecked(menu);
        return true;
    }

    private void setSortingOptionChecked(Menu menu)
    {
        switch (mSortingType) {
            case NAME_ASC:
                menu.findItem(R.id.sort_name_asc).setChecked(true);
                break;
            case NAME_DESC:
                menu.findItem(R.id.sort_name_dsc).setChecked(true);
                break;
            case DATE_ASC:
                menu.findItem(R.id.sort_date_asc).setChecked(true);
                break;
            case DATE_DESC:
                menu.findItem(R.id.sort_date_dsc).setChecked(true);
                break;
            case SIZE_ASC:
                menu.findItem(R.id.sort_size_asc).setChecked(true);
                break;
            case SIZE_DESC:
                menu.findItem(R.id.sort_size_dsc).setChecked(true);
                break;
            default:
                menu.findItem(R.id.sort_date_dsc).setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.sort_name_asc:
                updateSharedPreferenceAndGetNewList(NAME_ASC);
                item.setChecked(true);
                break;
            case R.id.sort_name_dsc:
                updateSharedPreferenceAndGetNewList(NAME_DESC);
                item.setChecked(true);
                break;
            case R.id.sort_date_asc:
                updateSharedPreferenceAndGetNewList(DATE_ASC);
                item.setChecked(true);
                break;
            case R.id.sort_date_dsc:
                updateSharedPreferenceAndGetNewList(DATE_DESC);
                item.setChecked(true);
                break;
            case R.id.sort_size_asc:
                updateSharedPreferenceAndGetNewList(SIZE_ASC);
                item.setChecked(true);
                break;
            case R.id.sort_size_dsc:
                updateSharedPreferenceAndGetNewList(SIZE_DESC);
                item.setChecked(true);
                break;
            default:
                updateSharedPreferenceAndGetNewList(DATE_DESC);
                item.setChecked(true);
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateSharedPreferenceAndGetNewList(int sortType)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SORT_TYPE_PREFERENCE_KEY, sortType);
        editor.apply();
        mVideoListManagerImpl.getVideosWithNewSorting(sortType);

    }

    @Override
    public void onVideoListUpdate(VideoListInfo videoListInfo) {
        mVideoListInfo = videoListInfo;

        if (mIsInSearchMode)
        {
            mVideoListInfo.setVideosList(VideoSearch.SearchResult(mSearchText, mVideoListInfo.getVideoListBackUp()));
            mVideoListInfo.setFolderListHashMap(VideoSearch.SearchResult(mSearchText,
                            mVideoListInfo.getFolderListHashMapBackUp()));
        }
        else
        {
            if(mVideoListInfo.getVideosList() != null)
                mVideoListInfo.getVideosList().clear();
            mVideoListInfo.getVideosList().addAll(mVideoListInfo.getVideoListBackUp());

            if(mVideoListInfo.getFolderListHashMap() != null)
                mVideoListInfo.getFolderListHashMap().clear();
            mVideoListInfo.getFolderListHashMap().putAll(mVideoListInfo.getFolderListHashMapBackUp());

        }

        mVideoListInfo.setSavedVideoList(FolderListGenerator.getSavedVideoListFromFolderHashMap(
                mVideoListInfo.getFolderListHashMap()));
        fetchFolderList();
        fetchVideoList();
        fetchSavedList();
    }

    public void fetchVideoList() {
        if(mListFragment != null && mVideoListInfo != null) {
            mListFragment.bindVideoList(mVideoListInfo.getVideosList(), mVideoListInfo);
        }
    }

    public void fetchSavedList() {
        if(mSavedListFragment != null && mVideoListInfo != null)
            mSavedListFragment.bindVideoList(mVideoListInfo.getSavedVideoList(), mVideoListInfo);
    }

    public void fetchFolderList()
    {
        if(mFolderListFragment != null && mVideoListInfo != null)
            mFolderListFragment.bindVideoList(mVideoListInfo.getFolderListHashMap(), mVideoListInfo);
    }

    public void registerListener(ListFragment listFragment)
    {
        mListFragment = listFragment;
    }

    public void registerListener(SavedListFragment savedListFragment)
    {
        mSavedListFragment = savedListFragment;
    }

    public void registerListener(FolderListFragment folderListFragment)
    {
        mFolderListFragment = folderListFragment;
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        mVideoListingHolderMvpImpl.onScrollChanged(scrollY,firstScroll, dragging);
    }

    @Override
    public void onDownMotionEvent() {

        mVideoListingHolderMvpImpl.onDownMotionEvent();
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mVideoListingHolderMvpImpl.onUpOrCancelMotionEvent(scrollState);
    }
}
