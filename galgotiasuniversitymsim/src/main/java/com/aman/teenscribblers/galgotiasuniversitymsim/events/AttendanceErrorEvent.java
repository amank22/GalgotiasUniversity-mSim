package com.aman.teenscribblers.galgotiasuniversitymsim.events;

/**
 * Created by aman on 19-02-2015.
 */
public class AttendanceErrorEvent {

    private String proccessed;

    public AttendanceErrorEvent(String error) {
        this.proccessed = error;
    }

    public String getError() {
        return proccessed;
    }
}
