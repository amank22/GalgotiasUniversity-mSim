package com.aman.teenscribblers.galgotiasuniversitymsim.Parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aman on 10/6/16.
 */
public class DateParcel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DateParcel> CREATOR = new Parcelable.Creator<DateParcel>() {
        @Override
        public DateParcel createFromParcel(Parcel in) {
            return new DateParcel(in);
        }

        @Override
        public DateParcel[] newArray(int size) {
            return new DateParcel[size];
        }
    };
    private String Date;
    private String SubjectName;
    private String TimeSlot;
    private String AttendanceType;
    private String Status;
    private String Additional;

    public DateParcel(String date, String subjectName, String timeSlot, String attendanceType, String status, String additional) {
        Date = date;
        SubjectName = subjectName;
        TimeSlot = timeSlot;
        AttendanceType = attendanceType;
        Status = status;
        Additional = additional;
    }

    protected DateParcel(Parcel in) {
        Date = in.readString();
        SubjectName = in.readString();
        TimeSlot = in.readString();
        AttendanceType = in.readString();
        Status = in.readString();
        Additional = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Date);
        dest.writeString(SubjectName);
        dest.writeString(TimeSlot);
        dest.writeString(AttendanceType);
        dest.writeString(Status);
        dest.writeString(Additional);
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public String getAttendanceType() {
        return AttendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        AttendanceType = attendanceType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAdditional() {
        return Additional;
    }

    public void setAdditional(String additional) {
        Additional = additional;
    }
}
