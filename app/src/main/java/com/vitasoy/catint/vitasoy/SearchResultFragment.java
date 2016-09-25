package com.vitasoy.catint.vitasoy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitasoy.catint.vitasoy.repo.TorrentPage;
import com.vitasoy.catint.vitasoy.view.adapter.SearchResultListAdapter;

public class SearchResultFragment extends Fragment {
    private SearchResultListAdapter searchResultListAdapter;
    private TorrentPage torrentPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView root = (RecyclerView) inflater.inflate(R.layout.fragment_search_result, container, false);
        searchResultListAdapter = new SearchResultListAdapter();
        root.setAdapter(searchResultListAdapter);
        if (torrentPage != null) {
            searchResultListAdapter.setResultData(torrentPage);
        }
        return root;
    }

    public void setSearchResult(TorrentPage torrentPage) {
        this.torrentPage = torrentPage;
        searchResultListAdapter.setResultData(torrentPage);
    }
}
