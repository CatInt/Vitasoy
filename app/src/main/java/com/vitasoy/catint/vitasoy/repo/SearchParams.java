package com.vitasoy.catint.vitasoy.repo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yodazone on 2016/9/24.
 */

public class SearchParams implements Parcelable {
    public static final String PARAM_METHOD = "method";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_NUMBER = "number";
    public static final String PARAM_GET = "get";

    public static final String METHOD_KICKASS = "kick";
    public static final String METHOD_BTSOW = "btso";
    public static final String METHOD_BTSOW_GET = "btso_get";

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
        params.put(PARAM_METHOD, method);
        return this;
    }

    public SearchParams putName(String name) {
        params.put(PARAM_NAME, name);
        return this;
    }

    public SearchParams putPageNo(String pageNo) {
        params.put(PARAM_NUMBER, pageNo);
        return this;
    }

    public SearchParams putGet(String get) {
        params.put(PARAM_GET, get);
        return this;
    }

    public String getParamMethod() {
        return params.get(PARAM_METHOD);
    }

    public String getParamName() {
        return params.get(PARAM_NAME);
    }

    public String getParamNumber() {
        return params.get(PARAM_NUMBER);
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
