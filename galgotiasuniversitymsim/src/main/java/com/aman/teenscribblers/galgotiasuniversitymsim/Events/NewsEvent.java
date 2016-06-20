package com.aman.teenscribblers.galgotiasuniversitymsim.Events;

import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.NewsParcel;

import java.util.List;

/**
 * Created by aman on 24-03-2015 in Galgotias University(mSim).
 */
public class NewsEvent {

    String result;
    List<NewsParcel> parcel;
    boolean error;

    public NewsEvent(String result, List<NewsParcel> parcel, boolean error) {
        this.result = result;
        this.parcel = parcel;
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public String getResult() {
        return result;
    }

    public List<NewsParcel> getParcel() {
        return parcel;
    }
}
