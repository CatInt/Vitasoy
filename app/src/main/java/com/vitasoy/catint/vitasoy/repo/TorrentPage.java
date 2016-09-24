package com.vitasoy.catint.vitasoy.repo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yodazone on 16/9/20.
 * Storing server respond(one page content)
 */
public class TorrentPage implements Parcelable {

    public TorrentPage() {
    }

    //result
    String msg;
    String totalPage;
    List<TorrentInfo> content;
    //request params
    String method;
    String number;
    String name;
    String get;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public List<TorrentInfo> getContent() {
        return content;
    }

    public void setContent(List<TorrentInfo> content) {
        this.content = content;
    }
    protected TorrentPage(Parcel in) {
        msg = in.readString();
        totalPage = in.readString();
        content = in.createTypedArrayList(TorrentInfo.CREATOR);
        method = in.readString();
        number = in.readString();
        name = in.readString();
        get = in.readString();
    }

    public static final Creator<TorrentPage> CREATOR = new Creator<TorrentPage>() {
        @Override
        public TorrentPage createFromParcel(Parcel in) {
            return new TorrentPage(in);
        }

        @Override
        public TorrentPage[] newArray(int size) {
            return new TorrentPage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(msg);
        parcel.writeString(totalPage);
        parcel.writeTypedList(content);
        parcel.writeString(method);
        parcel.writeString(name);
        parcel.writeString(number);
        parcel.writeString(get);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n*****TorrentPage*****\n")
                .append("msg:" + msg + "\n")
                .append(totalPage == null ? "" : ("totalPage:" + totalPage + "\n"));
        if (content != null) {
            builder.append("content:\n");
            for (TorrentInfo info : content) {
                builder.append(info.toString() + "\n");
            }
        }
        return builder.toString();
    }


}
