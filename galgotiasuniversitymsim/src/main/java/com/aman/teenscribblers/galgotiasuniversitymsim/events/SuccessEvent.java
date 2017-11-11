package com.aman.teenscribblers.galgotiasuniversitymsim.events;

import java.util.List;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class SuccessEvent<T> {
    String response;
    List<T> parcel;

    public SuccessEvent(String response, List<T> parcel) {
        this.response = response;
        this.parcel = parcel;
    }

    public String getResponse() {
        return response;
    }

    public List<T> getParcel() {
        return parcel;
    }
}
