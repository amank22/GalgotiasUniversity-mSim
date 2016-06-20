package com.aman.teenscribblers.galgotiasuniversitymsim.Parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aman on 25-12-2014.
 */
public class TimeTableParcel implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TimeTableParcel> CREATOR = new Parcelable.Creator<TimeTableParcel>() {
        @Override
        public TimeTableParcel createFromParcel(Parcel in) {
            return new TimeTableParcel(in);
        }

        @Override
        public TimeTableParcel[] newArray(int size) {
            return new TimeTableParcel[size];
        }
    };
    private String subject;
    private String day;
    private String group;
    private String faculty;
    private String timeslot;
    private String hallno;

    public TimeTableParcel(String subject, String day, String group, String faculty, String timeslot, String hallno) {
        this.subject = subject;
        this.day = day;
        this.group = group;
        this.faculty = faculty;
        this.timeslot = timeslot;
        this.hallno = hallno;
    }

    protected TimeTableParcel(Parcel in) {
        subject = in.readString();
        day = in.readString();
        group = in.readString();
        faculty = in.readString();
        timeslot = in.readString();
        hallno = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(day);
        dest.writeString(group);
        dest.writeString(faculty);
        dest.writeString(timeslot);
        dest.writeString(hallno);
    }

    public String getSubject() {
        return subject;
    }

    public String getDay() {
        return day;
    }

    public String getGroup() {
        return group;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public String getHallno() {
        return hallno;
    }

}
