package com.aman.teenscribblers.galgotiasuniversitymsim.parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amankapoor on 11/07/17.
 */

public class TodayAttParcel implements Parcelable {


    private String session;
    private String program;
    private String semester;
    private String subject;
    private String timeSlot;
    private String attendanceType;
    private String status;

    public TodayAttParcel(String session, String program, String semester, String subject, String timeSlot, String attendanceType, String status) {
        this.session = session;
        this.program = program;
        this.semester = semester;
        this.subject = subject;
        this.timeSlot = timeSlot;
        this.attendanceType = attendanceType;
        this.status = status;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.session);
        dest.writeString(this.program);
        dest.writeString(this.semester);
        dest.writeString(this.subject);
        dest.writeString(this.timeSlot);
        dest.writeString(this.attendanceType);
        dest.writeString(this.status);
    }

    protected TodayAttParcel(Parcel in) {
        this.session = in.readString();
        this.program = in.readString();
        this.semester = in.readString();
        this.subject = in.readString();
        this.timeSlot = in.readString();
        this.attendanceType = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<TodayAttParcel> CREATOR = new Parcelable.Creator<TodayAttParcel>() {
        public TodayAttParcel createFromParcel(Parcel source) {
            return new TodayAttParcel(source);
        }

        public TodayAttParcel[] newArray(int size) {
            return new TodayAttParcel[size];
        }
    };
}
