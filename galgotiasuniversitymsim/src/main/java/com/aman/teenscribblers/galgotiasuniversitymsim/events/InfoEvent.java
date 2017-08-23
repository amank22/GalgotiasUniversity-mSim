package com.aman.teenscribblers.galgotiasuniversitymsim.events;

import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.InfoParcel;

import java.util.List;

/**
 * Created by aman on 10/6/16.
 */
public class InfoEvent {

    public static final String TYPE_PERSONAL = "type_personal";
    public static final String TYPE_OFFICIAL = "type_official";
    public static final String TYPE_RESULT = "type_result";
    public static final String TYPE_SEATING = "type_seating";

    private String data;
    private String type;
    private boolean isError;
    private boolean isLocal;
    private List<InfoParcel> parsedList;

    public InfoEvent(String type, boolean isLocal, String data, boolean isError) {
        this.data = data;
        this.isError = isError;
        this.isLocal = isLocal;
        this.type = type;
    }

    public InfoEvent(String type, List<InfoParcel> parsedList) {
        this.isLocal = true;
        this.isError = false;
        this.parsedList = parsedList;
        this.type = type;
    }

    public String getType() {
        return type;
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
