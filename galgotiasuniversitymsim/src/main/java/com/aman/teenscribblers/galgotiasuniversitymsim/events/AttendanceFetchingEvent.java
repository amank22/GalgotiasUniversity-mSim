package com.aman.teenscribblers.galgotiasuniversitymsim.events;

/**
 * Created by aman on 19-02-2015.
 */
public class AttendanceFetchingEvent {

    private Boolean fromNetwork;

    public AttendanceFetchingEvent(Boolean network) {
        this.fromNetwork = network;
    }

    public Boolean getFromNetwork() {
        return fromNetwork;
    }

}
