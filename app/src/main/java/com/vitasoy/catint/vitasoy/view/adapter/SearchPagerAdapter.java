package com.vitasoy.catint.vitasoy.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.vitasoy.catint.vitasoy.SearchResultFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yodazone on 2016/9/24.
 */

public final class SearchPagerAdapter extends FragmentStatePagerAdapter {

    private final List<SearchResultFragment> mSearchResultFragments = new ArrayList<>();
    private final List<String> mSearchKeywords = new ArrayList<>();
    private final List<SearchResultListAdapter> mSearchResultListAdapters = new ArrayList<>();

    public SearchPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return mSearchResultFragments.get(position);
    }

    @Override
    public int getCount() {
        return mSearchKeywords.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mSearchKeywords.get(position);
    }

    public void addSearchResultPage(SearchResultFragment fragment, String searchKeyword) {
        mSearchResultFragments.add(fragment);
        mSearchKeywords.add(searchKeyword);
    }

    public SearchResultFragment findPagerByKeyword(String keyword) {
        int index = mSearchKeywords.indexOf(keyword);
        return mSearchResultFragments.get(index);
    }

    public void removePagerByKeyword(int index) {
        mSearchResultFragments.remove(index);
        mSearchKeywords.remove(index);
        notifyDataSetChanged();
    }
}
