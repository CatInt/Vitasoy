package com.vitasoy.catint.vitasoy;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lapism.searchview.SearchView;
import com.vitasoy.catint.vitasoy.domain.TorrentBayDomain;
import com.vitasoy.catint.vitasoy.repo.SearchParams;
import com.vitasoy.catint.vitasoy.repo.TorrentInfo;
import com.vitasoy.catint.vitasoy.repo.TorrentPage;
import com.vitasoy.catint.vitasoy.view.adapter.SearchPagerAdapter;
import com.vitasoy.catint.vitasoy.view.adapter.SearchResultListAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscription;
import rx.functions.Action1;

public class VitaActivity extends AppCompatActivity {

    private static final String TAG = "VitaActivity";
    private static final String METHOD_KICKASS = "kick";
    private static final String METHOD_BTSOW = "btso";
    private static final String METHOD_BTSOW_GET = "btso_get";
    private static String mMethod = METHOD_KICKASS;
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
    com.lapism.searchview.SearchView mSearchView;

    private List<TorrentInfo> mList;
    private SearchResultListAdapter mAdapter;
    private SearchPagerAdapter mPagerAdapter;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

//        initActionBar();
//        initDrawer();
        mSearchView.setNavigationIconArrowHamburger();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchResultFragment resultPager = new SearchResultFragment();
                mPagerAdapter.addSearchResultPage(resultPager, query);
                mPagerAdapter.notifyDataSetChanged();
                TorrentBayDomain.getInstance().getSearchSubscriber()
                        .onNext(new SearchParams().putName(query).putMethod(mMethod));
                mViewPager.setCurrentItem(mPagerAdapter.getCount(), true);
                return true;
            }
        });

        initTabLayout();
        subscribe();
    }

    private void initTabLayout() {
        mPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addSearchResultPage(new SearchResultFragment(), "favor");
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

//    private void initActionBar() {
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
//        TextView titleView = findTitleTextView(mToolbar);
//        if (titleView != null) {
//            Typeface typeface = Typeface.createFromAsset(getAssets(), "Trebuchet MS Bold.ttf");
//            titleView.setTypeface(typeface);
//            mToolbar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onSearchRequested();
//                }
//            });
//        }
//    }

    private TextView findTitleTextView(ViewGroup viewgroup) {
        int childNum = viewgroup.getChildCount();
        TextView dest = null;
        for (int i = 0; i < childNum; i++) {
            if (viewgroup.getChildAt(i) instanceof TextView) {
                Log.d(TAG, "find titleTextView!");
                dest = (TextView) viewgroup.getChildAt(i);
                break;
            } else if (viewgroup.getChildAt(i) instanceof ViewGroup) {
                dest = findTitleTextView((ViewGroup) viewgroup.getChildAt(i));
            }
        }
        return dest;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSubscription.unsubscribe();
        super.onDestroy();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        MenuItem item = menu.findItem(R.id.app_bar_search);
//        mSearchView = (SearchView) item.getActionView();
//        mSearchView.setIconifiedByDefault(true);
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (query == null && query.equals("")) {
//                    return false;
//                }
//                Map<String, String> searchParams = new HashMap<String, String>();
//                searchParams.put("name", query);
//                searchParams.put("method", mMethod);
//                mSubscriber.onNext(searchParams);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
////        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
////        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        return true;
//    }

    void onFabClick(View v) {
        int current = mViewPager.getCurrentItem();
        if (current == 0) {
            Snackbar.make(v, "This page couldn't be removed.", Snackbar.LENGTH_SHORT).show();
        }
        mPagerAdapter.removePagerByKeyword(mViewPager.getCurrentItem());
    }

    void onClick(final View magnet) {
        String url = (String) magnet.getTag();
        if (url.startsWith("magnet")) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("magnet", url));
            Snackbar.make(magnet, "Copy magnet to clipboard.", Snackbar.LENGTH_SHORT)
                    .setAction(R.string.action_open, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse((String) magnet.getTag());
                            Log.d(TAG, "uriScheme:" + uri.getScheme());
                            startActivity(new Intent(Intent.ACTION_VIEW, uri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }).show();
        } else if (url.startsWith("http")) {
            //TODO
            Map<String, String> searchParams = new HashMap<>();
            if (url.contains("btso")) {
                searchParams.put("method", METHOD_BTSOW_GET);
                searchParams.put("get", url);
            }
        }
    }

//    private void initDrawer() {
//        //init left-sliding drawer
//
//        //if you want to update the items at a later time it is recommended to keep it in a variable
////        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("PRIMARY").withSelectable(false);
//        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("KICKASS").withSelectable(true);
//        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("BTSOW").withSelectable(true);
//        DividerDrawerItem divider = new DividerDrawerItem().withSelectable(false);
//
//        //create the drawer and remember the `Drawer` result object
//        Drawer result = new DrawerBuilder()
//                .withActivity(this)
//                .addDrawerItems(item2, divider, item3)
//                .withSelectedItem(2)
//                .withMultiSelect(false)
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        switch (position) {
//                            case 1:
//                                mMethod = METHOD_KICKASS;
//                                break;
//                            case 3:
//                                mMethod = METHOD_BTSOW;
//                                break;
//                            default:
//                                Snackbar.make(mRecycleView, "not implement", Snackbar.LENGTH_SHORT).show();
//                                break;
//                        }
//
//                        return true;
//                    }
//                })
//                .withHeader(R.layout.vita_drawer)
//                .withDrawerWidthDp(180)
//                .withHeaderHeight(DimenHolder.fromDp(300))
//                .withHeaderDivider(true)
//                .withSliderBackgroundColorRes(R.color.primary_dark)
//                .withTranslucentStatusBar(true)
//                .withDisplayBelowStatusBar(true)
//                .build();
//
//
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this
////                , result.getDrawerLayout()
////                , mToolbar
////                , R.string.material_drawer_open
////                , R.string.material_drawer_close);
////        result.setActionBarDrawerToggle(toggle);
////        toggle.syncState();
//    }

    private void subscribe() {

        mSubscription = TorrentBayDomain.getInstance().getWorkflow()
                .subscribe(new Action1<TorrentPage>() {
                    @Override
                    public void call(TorrentPage torrentPage) {
                        if (torrentPage.getContent() == null) {
                            Snackbar.make(mViewPager, torrentPage.getMsg(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            mPagerAdapter.findPagerByKeyword(torrentPage.getName())
                                    .getSearchResultListAdapter(torrentPage);
                        }
                    }
                });
    }

}
