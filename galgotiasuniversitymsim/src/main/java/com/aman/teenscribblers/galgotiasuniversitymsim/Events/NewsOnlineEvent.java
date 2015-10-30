package com.aman.teenscribblers.galgotiasuniversitymsim.Events;

/**
 * Created by aman on 24-03-2015 in Galgotias University(mSim).
 */
public class NewsOnlineEvent {

    String result;
    boolean error;

    public boolean isError() {
        return error;
    }

    public String getResult() {
        return result;
    }

    public NewsOnlineEvent(String result, boolean error) {
        this.result = result;
        this.error = error;
    }
}
