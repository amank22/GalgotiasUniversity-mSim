package com.aman.teenscribblers.galgotiasuniversitymsim.events;

import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.ResultParcel;

import java.util.List;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class ResultSuccessEvent {

    private List<ResultParcel> parcel;
    private String semester;

    public ResultSuccessEvent(List<ResultParcel> parcel, String semester) {
        this.parcel = parcel;
        this.semester = semester;
    }

    public String getSemester() {
        return semester;
    }

    public List<ResultParcel> getParcel() {
        return parcel;
    }
}
