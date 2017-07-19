package com.aman.teenscribblers.galgotiasuniversitymsim.events;

import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SimParcel;

import java.util.List;

/**
 * Created by aman on 19-02-2015.
 */
public class AttendanceProccesedEvent {

    String proccessed;

    List<SimParcel> parcel;

    public AttendanceProccesedEvent(String proccessed, List<SimParcel> parcel) {
        this.proccessed = proccessed;
        this.parcel = parcel;
    }

    public String getProccessed() {
        return proccessed;
    }

    public List<SimParcel> getParcel() {
        return parcel;
    }
}
