package com.vitasoy.catint.vitasoy.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitasoy.catint.vitasoy.R;
import com.vitasoy.catint.vitasoy.repo.TorrentPage;
import com.vitasoy.catint.vitasoy.view.adapter.SearchResultListAdapter;

public class SearchResultFragment extends Fragment {
    private SearchResultListAdapter searchResultListAdapter;
    private TorrentPage torrentPage = null;

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    @Deprecated
    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance() {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView root = (RecyclerView) inflater.inflate(R.layout.fragment_search_result, container, false);
        if (searchResultListAdapter == null){
            searchResultListAdapter = new SearchResultListAdapter();
        }
        root.setAdapter(searchResultListAdapter);
        if (savedInstanceState != null && torrentPage == null) {
            if (savedInstanceState.containsKey("data")) {
                torrentPage = savedInstanceState.getParcelable("data");
                setSearchResult(torrentPage);
            }
        }
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (torrentPage != null)
            outState.putParcelable("data", torrentPage);
    }

    public void setSearchResult(TorrentPage torrentPage) {
        try {
            searchResultListAdapter.setResultData(torrentPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
