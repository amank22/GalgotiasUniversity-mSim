package com.aman.teenscribblers.galgotiasuniversitymsim.Events;

import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.TimeTableParcel;

import java.util.List;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class TimeTableSuccessEvent {
    String response;
    List<TimeTableParcel> parcel;

    public TimeTableSuccessEvent(String response, List<TimeTableParcel> parcel) {
        this.response = response;
        this.parcel = parcel;
    }

    public String getResponse() {
        return response;
    }

    public List<TimeTableParcel> getParcel() {
        return parcel;
    }
}
