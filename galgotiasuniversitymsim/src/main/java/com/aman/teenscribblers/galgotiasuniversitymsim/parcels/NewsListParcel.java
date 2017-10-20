package com.aman.teenscribblers.galgotiasuniversitymsim.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amankapoor on 05/08/17.
 */

public class NewsListParcel implements Parcelable {

    /**
     * error : false
     * status : 200
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("result")
    private String result;
    @SerializedName("news")
    private List<NewsParcel> news;
    @SerializedName("status")
    private int status;

    public boolean isError() {
        return error;
    }

    public List<NewsParcel> getNews() {
        return news;
    }

    public String getResult() {
        return result;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "NewsListParcel{" +
                "error=" + error +
                ", result='" + result + '\'' +
                ", news=" + news +
                ", status=" + status +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(error ? (byte) 1 : (byte) 0);
        dest.writeString(this.result);
        dest.writeTypedList(news);
        dest.writeInt(this.status);
    }

    public NewsListParcel() {
    }

    protected NewsListParcel(Parcel in) {
        this.error = in.readByte() != 0;
        this.result = in.readString();
        this.news = in.createTypedArrayList(NewsParcel.CREATOR);
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<NewsListParcel> CREATOR = new Parcelable.Creator<NewsListParcel>() {
        public NewsListParcel createFromParcel(Parcel source) {
            return new NewsListParcel(source);
        }

        public NewsListParcel[] newArray(int size) {
            return new NewsListParcel[size];
        }
    };
}
