package com.vitasoy.catint.vitasoy;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.vitasoy.catint.vitasoy.domain.TorrentSearchWorkflow;
import com.vitasoy.catint.vitasoy.repo.SearchParams;
import com.vitasoy.catint.vitasoy.repo.TorrentPage;
import com.vitasoy.catint.vitasoy.view.CustomSearchView;
import com.vitasoy.catint.vitasoy.view.SearchResultFragment;
import com.vitasoy.catint.vitasoy.view.adapter.SearchPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscription;
import rx.functions.Action1;

public class VitaActivity extends FragmentActivity {

    private static final String TAG = "VitaActivity";
    //    private static final int MESSAGE_NEW_DATA = 100;
//    private static final int MESSAGE_TCP_CONNECTED = 200;
//    private static final int MESSAGE_TCP_MESSAGE = 201;
//    private static final int MESSAGE_PAGE_ARRIVE = 300;
//    private static final int MESSAGE_PAGE_SEARCH_ERROR = 301;

    //    @InjectView(R.id.list_main)
//    RecyclerView mRecycleView;
    @InjectView(R.id.view_page)
    ViewPager mViewPager;
    @InjectView(R.id.tab_bar)
    TabLayout mTabLayout;
    @InjectView(R.id.search_view)
    CustomSearchView mSearchView;
    @InjectView(R.id.fab_remove)
    FloatingActionButton mFab;

    private SearchPagerAdapter mPagerAdapter;
    private Subscription mSubscription;

    SearchHistoryTable mSearchHistoryTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initSearchView();
        initTabLayout();
        subscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        mSubscription.unsubscribe();
        super.onDestroy();
    }

    private void initSearchView() {
        mSearchHistoryTable = new SearchHistoryTable(this);
        mSearchHistoryTable.setHistorySize(5);
        if (mSearchView != null) {
            mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
            mSearchView.setNavigationIconArrowHamburger();
            mSearchView.setTextSize(16);
            mSearchView.setHint("Search");
            mSearchView.setDivider(false);
            mSearchView.setVoice(false);
            mSearchView.setAnimationDuration(SearchView.ANIMATION_DURATION);
//            mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    onQuerySubmit(query);
                    mSearchView.close(true);
                    return true;
                }
            });
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public void onOpen() {
                    if (mFab != null) {
                        mFab.hide();
                    }
                }

                @Override
                public void onClose() {
                    if (mFab != null) {
                        mFab.show();
                    }
                }
            });

            if (mSearchView.getAdapter() == null) {
                List<SearchItem> suggestionsList = new ArrayList<>();
                SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
                searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                        String query = textView.getText().toString();
                        mSearchView.setQuery(query);
//                        onQuerySubmit(query);
                        // mSearchView.close(false);
                    }
                });
                mSearchView.setAdapter(searchAdapter);
            }
            List<CustomSearchView.RadiusFilter> filters = new ArrayList<>();
            filters.add(new CustomSearchView.RadiusFilter("KICKASS", SearchParams.METHOD_KICKASS));
            filters.add(new CustomSearchView.RadiusFilter("BTSOW", SearchParams.METHOD_BTSOW));
            mSearchView.setRadiusFilters(filters);
        }
    }

    private void initTabLayout() {
        mPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager());
        SearchResultFragment favor = SearchResultFragment.newInstance();
        mPagerAdapter.addSearchResultPage(favor, "History", "History");
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void subscribe() {
        mSubscription = TorrentSearchWorkflow.getInstance().subscribe(new Action1<TorrentPage>() {
            @Override
            public void call(TorrentPage result) {
                //deal with the search result
                String method = result.getMethod();
                String name = result.getName();
                if (result.getContent() == null || result.getMagnet() == null) {
                    Snackbar.make(mViewPager, result.getMsg(), Snackbar.LENGTH_SHORT).show();
                }
                switch (method) {
                    case SearchParams.METHOD_BTSOW_GET:
                        showMagnetAction(result.getMagnet());
                        break;
                    case SearchParams.METHOD_KICKASS:
                    case SearchParams.METHOD_BTSOW:
                    default:
                        int page = mPagerAdapter.findPageByKeyword(method + name);
                        if (page > -1) {
                            SearchResultFragment fragment = mPagerAdapter.getSearchResultFragment(page);
                            fragment.setSearchResult(result);
                        }
                        break;
                }
            }
        });
    }

    @SuppressWarnings("unused")
    void onFabClick(View v) {
        int current = mViewPager.getCurrentItem();
        if (current == 0) {
            Snackbar.make(v, "This page couldn't be removed.", Snackbar.LENGTH_SHORT).show();
            return;
        }
        mPagerAdapter.removePagerByKeyword(mViewPager.getCurrentItem());
    }

    @SuppressWarnings("unused")
    void onClick(final View magnet) {
        String url = (String) magnet.getTag();
        if (url.startsWith("magnet")) {
            showMagnetAction(url);
        } else if (url.startsWith("http")) {
            if (url.contains("btso")) {
                TorrentSearchWorkflow.getInstance().getSearchResult(
                        new SearchParams().putMethod(SearchParams.METHOD_BTSOW_GET).putGet(url));
            }
        }
    }

    private void showMagnetAction(final String magnet) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("magnet", magnet));
        Snackbar.make(mViewPager, "Copy magnet to clipboard.", Snackbar.LENGTH_SHORT)
                .setAction(R.string.action_open, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(magnet);
                        Log.d(TAG, "uriScheme:" + uri.getScheme());
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, uri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        } catch (Exception e) {
                            Snackbar.make(mViewPager, "No app can handle magnet.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    void onQuerySubmit(String query) {
        String method = mSearchView.getCheckedFilterTag();
        String keyword = method + query;
        int page = mPagerAdapter.findPageByKeyword(keyword);
        mSearchHistoryTable.addItem(new SearchItem(query));
        if (page < 0) {
            SearchResultFragment resultPager = SearchResultFragment.newInstance();
            mPagerAdapter.addSearchResultPage(resultPager, query, keyword);
            mPagerAdapter.notifyDataSetChanged();
            page = mPagerAdapter.getCount()-1;
        }
        TorrentSearchWorkflow.getInstance().getSearchResult(new SearchParams().putName(query).putMethod(method));
        mViewPager.setCurrentItem(page, true);
    }


}
