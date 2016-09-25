package com.vitasoy.catint.vitasoy.domain;

import android.util.Log;

import com.vitasoy.catint.vitasoy.repo.SearchParams;
import com.vitasoy.catint.vitasoy.repo.TorrentPage;

import java.io.IOException;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yodazone on 16/9/20.
 */
public class TorrentBayDomain {

    private final static String TAG = "TorrentBayDomain";

    private static TorrentBayDomain ourInstance = new TorrentBayDomain();

    private Retrofit mRetrofit;
    private Subscriber<? super SearchParams> mSubscriber;
    private Observable<TorrentPage> mWorkflow;

    public static TorrentBayDomain getInstance() {
        return ourInstance;
    }

    private TorrentBayDomain() {
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
                .observeOn(Schedulers.newThread())
                .map(new Func1<SearchParams, TorrentPage>() {
                    @Override
                    public TorrentPage call(SearchParams p) {
                        return search(p);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


    private synchronized TorrentPage search(final SearchParams dynamic) {
        TorrentPage result = null;
        TorrentBayDomain.TorrentBayService torrentBayService = mRetrofit.create(TorrentBayService.class);
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

    public Observable<TorrentPage> getWorkflow() {
        return mWorkflow;
    }
}
