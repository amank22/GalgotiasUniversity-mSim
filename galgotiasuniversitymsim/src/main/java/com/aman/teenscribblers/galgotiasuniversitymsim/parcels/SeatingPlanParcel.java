package com.aman.teenscribblers.galgotiasuniversitymsim.parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amankapoor on 11/11/17.
 */

public class SeatingPlanParcel implements Parcelable {

    private String examDate;
    private String examTime;
    private String subjectCode;
    private String roomNumber;
    private String seatNumber;

    public SeatingPlanParcel(String examDate, String examTime, String subjectCode, String roomNumber, String seatNumber) {
        this.examDate = examDate;
        this.examTime = examTime;
        this.subjectCode = subjectCode;
        this.roomNumber = roomNumber;
        this.seatNumber = seatNumber;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.examDate);
        dest.writeString(this.examTime);
        dest.writeString(this.subjectCode);
        dest.writeString(this.roomNumber);
        dest.writeString(this.seatNumber);
    }

    protected SeatingPlanParcel(Parcel in) {
        this.examDate = in.readString();
        this.examTime = in.readString();
        this.subjectCode = in.readString();
        this.roomNumber = in.readString();
        this.seatNumber = in.readString();
    }

    public static final Parcelable.Creator<SeatingPlanParcel> CREATOR = new Parcelable.Creator<SeatingPlanParcel>() {
        public SeatingPlanParcel createFromParcel(Parcel source) {
            return new SeatingPlanParcel(source);
        }

        public SeatingPlanParcel[] newArray(int size) {
            return new SeatingPlanParcel[size];
        }
    };
}
