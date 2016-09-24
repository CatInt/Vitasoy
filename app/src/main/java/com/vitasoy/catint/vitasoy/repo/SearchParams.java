package com.vitasoy.catint.vitasoy.repo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yodazone on 2016/9/24.
 */

public class SearchParams implements Parcelable{
    private Map<String, String> params = new HashMap<>();

    public SearchParams() {
    }

    protected SearchParams(Parcel in) {
    }

    public static final Creator<SearchParams> CREATOR = new Creator<SearchParams>() {
        @Override
        public SearchParams createFromParcel(Parcel in) {
            return new SearchParams(in);
        }

        @Override
        public SearchParams[] newArray(int size) {
            return new SearchParams[size];
        }
    };

    public SearchParams putMethod(String method) {
        params.put("method", method);
        return this;
    }

    public SearchParams putName(String name) {
        params.put("name", name);
        return this;
    }

    public SearchParams putPageNo(String pageNo) {
        params.put("number", pageNo);
        return this;
    }

    public SearchParams putGet(String get) {
        params.put("get", get);
        return this;
    }

    public Map<String, String> getParamsMap() {
        return params;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
