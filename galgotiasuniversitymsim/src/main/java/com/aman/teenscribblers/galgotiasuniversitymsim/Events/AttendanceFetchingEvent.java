package com.aman.teenscribblers.galgotiasuniversitymsim.Events;

/**
 * Created by aman on 19-02-2015.
 */
public class AttendanceFetchingEvent {

    private Boolean isNetwork;

    public AttendanceFetchingEvent(Boolean network) {
        this.isNetwork = network;
    }

    public Boolean getIsNetwork() {
        return isNetwork;
    }

}
