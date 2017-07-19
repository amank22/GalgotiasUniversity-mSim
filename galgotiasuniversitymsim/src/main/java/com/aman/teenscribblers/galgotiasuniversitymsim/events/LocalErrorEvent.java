package com.aman.teenscribblers.galgotiasuniversitymsim.events;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class LocalErrorEvent {
    String response;

    public LocalErrorEvent(String response){
        this.response=response;
    }

    public String getResponse() {
        return response;
    }
}
