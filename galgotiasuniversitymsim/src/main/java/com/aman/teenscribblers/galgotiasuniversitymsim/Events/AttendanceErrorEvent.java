package com.aman.teenscribblers.galgotiasuniversitymsim.Events;

/**
 * Created by aman on 19-02-2015.
 */
public class AttendanceErrorEvent {

    public String getError() {
        return proccessed;
    }

    private String proccessed;

    public AttendanceErrorEvent(String error) {
        this.proccessed = error;
    }
}
