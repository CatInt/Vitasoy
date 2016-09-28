package com.vitasoy.catint.vitasoy.domain;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.vitasoy.catint.vitasoy.repo.SearchParams;
import com.vitasoy.catint.vitasoy.repo.TorrentInfo;
import com.vitasoy.catint.vitasoy.repo.TorrentPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yodazone on 16/9/20.
 */
public class TorrentSearchWorkflow {

    private final static String TAG = "TorrentSearchWorkflow";

    final private static String URL_KICKASS = "http://kickasstorrents.to/usearch/";
    final private static String URL_BTSO = "https://btso.pw/search/";

    final private static Pattern PATTERN_QUALITY = Pattern.compile("(?:PPV\\.)?[HP]DTV|(?:HD)?CAM|B[rR]Rip|(?:PPV )?[Ww][Ee][Bb]-?[Dd][Ll](?: DVDRip)?|H[dD]Rip|DVDRip|DVDRiP|DVDRIP|CamRip|W[EB]B[rR]ip|[Bb]lu[Rr]ay|DvDScr|hdtv");

    private static TorrentSearchWorkflow ourInstance = new TorrentSearchWorkflow();

    private Retrofit mRetrofit;
    private Subscriber<? super SearchParams> mSubscriber;
    private Observable<TorrentPage> mWorkflow;

    public static TorrentSearchWorkflow getInstance() {
        return ourInstance;
    }

    private TorrentSearchWorkflow() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.3.38:8080")
//                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        mWorkflow = Observable.create(new Observable.OnSubscribe<SearchParams>() {
            @Override
            public void call(final Subscriber<? super SearchParams> subscriber) {
                mSubscriber = subscriber;
            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Func1<SearchParams, TorrentPage>() {
                    @Override
                    public TorrentPage call(SearchParams p) {
//                        return searchFromAPI(p);
                        return searchDirectly(p);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private synchronized TorrentPage searchDirectly(SearchParams p) {
        TorrentPage result = null;
        String msg = null;
        String url;
        String method = p.getParamMethod();
        String name = p.getParamName();
        String number = p.getParamNumber();
        Document doc = null;
        try {
            switch (method) {
                case SearchParams.METHOD_KICKASS:
                    url = new StringBuilder().append(URL_KICKASS)
                            .append(name + "/")
                            .append(number == null ? "" : number + "/")
                            .toString();
                    doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (compatible; MSIE 5.0; Windows NT; DigExt)").timeout(5000).get();
                    result = fetchTorrentPageFromKat(doc.body());
                    break;
                case SearchParams.METHOD_BTSOW:
                    url = URL_BTSO + name + (number == null ? "" : number + "/");
                    doc = Jsoup
                            .connect(url)
                            .header("Host", "btso.pw")
                            .header("User-Agent",
                                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36")
                            .header("Accept",
                                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .header("Accept-Encoding", "gzip, deflate, sdch, br")
                            .header("Accept-Language",
                                    "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4")
                            .header("Upgrade-Insecure-Requests", "1")
                            .header("Connection", "keep-alive")
                            .timeout(5000)
                            .followRedirects(true)
                            .get();

                    result = fetchTorrentPageFromBtso(doc.body());
                    break;
                case SearchParams.METHOD_BTSOW_GET:
                    url = p.getParamsMap().get(SearchParams.PARAM_GET);
                    doc = Jsoup
                            .connect(url)
                            .header("Host", "btso.pw")
                            .header("User-Agent",
                                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36")
                            .header("Accept",
                                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .header("Accept-Encoding", "gzip, deflate, sdch, br")
                            .header("Accept-Language",
                                    "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4")
                            .header("Upgrade-Insecure-Requests", "1")
                            .header("Connection", "keep-alive")
                            .timeout(5000)
                            .followRedirects(true)
                            .get();
                    result = fetchTorrentLinkFromBtso(doc.body());
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        if (result == null) {
            result = new TorrentPage();
        }
        result.setMsg(msg == null ? "success" : msg);
        result.setMethod(method);
        result.setName(name);
        Log.d(TAG, "respond:" + result.toString());
        return result;
    }

    private TorrentPage fetchTorrentPageFromKat(Element body) {
        List<TorrentInfo> magnetList = new ArrayList<>();
        Elements trs = body.select("table.data").select("tr");
        try {
            for (Element tr : trs) {
                if (tr.hasClass("firstr")) {
                    continue;
                }
                Elements tds = tr.select("td");
                String json = tds.get(0).select("div.iaconbox.center.floatright")
                        .select("Div[data-sc-params]").attr("data-sc-params").replaceAll("'", "\"");
//                String json = tds.get(0).select("div.iaconbox.center.floatright").select("Div[data-sc-params]").attr("data-sc-params").replaceAll("'", "\"");
                TorrentInfo t = new GsonBuilder().create().fromJson(json, TorrentInfo.class);
                t.setName(URLDecoder.decode(t.getName()));
                t.setSize(tds.get(1).html().replace("<span>", "").replace("</span>", ""));
                t.setFiles(tds.get(2).html());
                t.setAges(tds.get(3).html().replace("&nbsp;", " "));
                t.setSeed(tds.get(4).html());
                t.setLeech(tds.get(5).html());
                t.setQuality(findMatchPattern(t.getName(), PATTERN_QUALITY));
                magnetList.add(t);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

        TorrentPage result = new TorrentPage();
        result.setContent(magnetList);
        return result;
    }

    private TorrentPage fetchTorrentPageFromBtso(Element body) {
        List<TorrentInfo> magnetList = new ArrayList<>();
        Elements rows = body.select("div.data-list").select("div.row");
        for (Element row : rows) {
            Element a = row.select("a").first();
            if (a == null) continue;
            Elements divs = row.select("div");
            TorrentInfo t = new TorrentInfo();
            t.setName(a.attr("title"));
            t.setMagnet(a.attr("href"));
            t.setSize(row.select("div.size").first().html());
            t.setAges(row.select("div.date").first().html());
            magnetList.add(t);
        }
        TorrentPage result = new TorrentPage();
        result.setContent(magnetList);
        return result;
    }

    private TorrentPage fetchTorrentLinkFromBtso(Element body) {
        String link = body.select("textarea[id]").first().html();
        TorrentPage result = new TorrentPage();
        result.setMagnet(link);
        result.setGet(body.baseUri());
        return result;
    }

    private String findMatchPattern(String source, Pattern patternQuality) {
        String result = null;
        Matcher matcher = patternQuality.matcher(source);
        while (matcher.find()) {
            result = matcher.group();
        }
        return result == null ? "" : result;
    }

    @Deprecated
    private synchronized TorrentPage searchFromAPI(final SearchParams dynamic) {
        TorrentPage result = null;
        TorrentSearchWorkflow.TorrentBayService torrentBayService = mRetrofit.create(TorrentBayService.class);
        Call<TorrentPage> request = torrentBayService.getRepos(dynamic.getParamsMap());
        Log.d(TAG, request.request().url().toString());
        try {
            result = request.execute().body();
        } catch (IOException e) {
            result = new TorrentPage();
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        result.setName(dynamic.getParamsMap().get(SearchParams.PARAM_NAME));
        result.setMethod(dynamic.getParamsMap().get(SearchParams.PARAM_METHOD));
        Log.d(TAG, "respond:" + result.toString());
        return result;
    }

    public interface TorrentBayService {
        @GET("/chapter1/hello")
        Call<TorrentPage> getRepos(@QueryMap Map<String, String> dynamic);
    }

    public void getSearchResult(SearchParams p) {
        mSubscriber.onNext(p);
    }

    public Subscription subscribe(Action1<TorrentPage> action1) {
        return mWorkflow.subscribe(action1);
    }
}
