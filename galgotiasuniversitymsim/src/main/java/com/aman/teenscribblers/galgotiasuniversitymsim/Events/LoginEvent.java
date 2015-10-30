package com.aman.teenscribblers.galgotiasuniversitymsim.Events;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class LoginEvent {

    String Reason;


    boolean error;

    public LoginEvent(String Reason, boolean error) {
        this.Reason = Reason;
        this.error = error;
    }

    public String getReason() {
        return Reason;
    }

    public boolean isError() {
        return error;
    }
}
