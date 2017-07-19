package com.aman.teenscribblers.galgotiasuniversitymsim.parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aman on 10/6/16.
 */
public class InfoParcel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<InfoParcel> CREATOR = new Parcelable.Creator<InfoParcel>() {
        @Override
        public InfoParcel createFromParcel(Parcel in) {
            return new InfoParcel(in);
        }

        @Override
        public InfoParcel[] newArray(int size) {
            return new InfoParcel[size];
        }
    };
    private String key;
    private String value;

    public InfoParcel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    protected InfoParcel(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}