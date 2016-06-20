package com.aman.teenscribblers.galgotiasuniversitymsim.Events;

import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.InfoParcel;

import java.util.List;

/**
 * Created by aman on 10/6/16.
 */
public class InfoEvent {
    private String data;
    private boolean isError;
    private boolean isLocal;
    private List<InfoParcel> parsedList;

    public InfoEvent(boolean isLocal, String data, boolean isError) {
        this.data = data;
        this.isError = isError;
        this.isLocal = isLocal;
    }

    public InfoEvent(List<InfoParcel> parsedList) {
        this.isLocal = true;
        this.isError = false;
        this.parsedList = parsedList;
    }

    public String getData() {
        return data;
    }


    public Boolean getError() {
        return isError;
    }

    public Boolean getLocal() {
        return isLocal;
    }

    public List<InfoParcel> getParsedList() {
        return parsedList;
    }

}
