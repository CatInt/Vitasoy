package com.vitasoy.catint.vitasoy.repo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class TorrentInfo implements Parcelable {

    public TorrentInfo() {
    }

    public TorrentInfo(String magnet, @Nullable String name, @Nullable String ages, @Nullable String seed, @Nullable String size) {
        this.magnet = magnet;
        this.name = name;
        this.ages = ages;
        this.seed = seed;
        this.size = size;
    }

    private String magnet;
    private String name;
    private String ages;
    private String seed;
    private String leech;
    private String files;
    private String size;
    private String extension;
    private String resolution;
    private String acode;
    private String vcode;
    private String quality;

    protected TorrentInfo(Parcel in) {
        magnet = in.readString();
        name = in.readString();
        ages = in.readString();
        seed = in.readString();
        leech = in.readString();
        files = in.readString();
        size = in.readString();
        extension = in.readString();
        resolution = in.readString();
        acode = in.readString();
        vcode = in.readString();
        quality = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(magnet);
        dest.writeString(name);
        dest.writeString(ages);
        dest.writeString(seed);
        dest.writeString(leech);
        dest.writeString(files);
        dest.writeString(size);
        dest.writeString(extension);
        dest.writeString(resolution);
        dest.writeString(vcode);
        dest.writeString(acode);
        dest.writeString(quality);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TorrentInfo> CREATOR = new Creator<TorrentInfo>() {
        @Override
        public TorrentInfo createFromParcel(Parcel in) {
            return new TorrentInfo(in);
        }

        @Override
        public TorrentInfo[] newArray(int size) {
            return new TorrentInfo[size];
        }
    };

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAges() {
        return ages;
    }

    public void setAges(String ages) {
        this.ages = ages;
    }

    public String getLeech() {
        return leech;
    }

    public void setLeech(String leech) {
        this.leech = leech;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getAcode() {
        return acode;
    }

    public void setAcode(String acode) {
        this.acode = acode;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("*****TorrentInfo*****\n")
                .append("name=" + name + "\n")
                .append("magnet=" + magnet + "\n")
                .append("ages=" + ages + "\n")
                .append("seed=" + seed + "\n")
                .append("leech=" + leech + "\n")
                .append("size=" + size + "\n")
                .append("files=" + files + "\n")
                .append("extension=" + extension + "\n")
                .append("resolution=" + resolution + "\n")
                .append("vcode=" + vcode + "\n")
                .append("acode=" + acode + "\n")
                .append("quality=" + quality + "\n")
                .append("*********************");
        return builder.toString();
    }
}
