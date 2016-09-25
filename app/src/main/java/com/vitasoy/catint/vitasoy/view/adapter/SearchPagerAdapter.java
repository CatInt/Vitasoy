package com.vitasoy.catint.vitasoy.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.vitasoy.catint.vitasoy.SearchResultFragment;

import java.util.ArrayList;
import java.util.List;

public final class SearchPagerAdapter extends FragmentStatePagerAdapter {

    private final List<SearchResultFragment> mSearchResultFragments = new ArrayList<>();
    private final List<String> mFragmentTitle = new ArrayList<>();
    private final List<String> mIdentitiesKey = new ArrayList<>();

    public SearchPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return mSearchResultFragments.get(position);
    }

    @Override
    public int getCount() {
        return mIdentitiesKey.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitle.get(position);
    }

    public void addSearchResultPage(SearchResultFragment fragment, String title, String key) {
        mSearchResultFragments.add(fragment);
        mFragmentTitle.add(title);
        mIdentitiesKey.add(key);
    }

    //if find none,return -1
    public int findPageByKeyword(String keyword) {
        return mIdentitiesKey.indexOf(keyword);
    }

    public void removePagerByKeyword(int index) {
        mSearchResultFragments.remove(index);
        mIdentitiesKey.remove(index);
        mFragmentTitle.remove(index);
        notifyDataSetChanged();
    }
}
