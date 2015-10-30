package com.aman.teenscribblers.galgotiasuniversitymsim.Events;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class TimeTableErrorEvent {
    String response;

    public TimeTableErrorEvent(String response){
        this.response=response;
    }

    public String getResponse() {
        return response;
    }
}
