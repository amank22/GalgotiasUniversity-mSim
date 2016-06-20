package com.aman.teenscribblers.galgotiasuniversitymsim.Parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aman on 10/6/16.
 */
public class SubjectParcel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SubjectParcel> CREATOR = new Parcelable.Creator<SubjectParcel>() {
        @Override
        public SubjectParcel createFromParcel(Parcel in) {
            return new SubjectParcel(in);
        }

        @Override
        public SubjectParcel[] newArray(int size) {
            return new SubjectParcel[size];
        }
    };
    private String Present;
    private String Absent;
    private String Total;
    private String Percentage;
    private String Subject;

    public SubjectParcel(String present, String absent, String total, String percentage, String subject) {
        Present = present;
        Absent = absent;
        Total = total;
        Percentage = percentage;
        Subject = subject;
    }

    protected SubjectParcel(Parcel in) {
        Present = in.readString();
        Absent = in.readString();
        Total = in.readString();
        Percentage = in.readString();
        Subject = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Present);
        dest.writeString(Absent);
        dest.writeString(Total);
        dest.writeString(Percentage);
        dest.writeString(Subject);
    }

    public String getPresent() {
        return Present;
    }

    public void setPresent(String present) {
        Present = present;
    }

    public String getAbsent() {
        return Absent;
    }

    public void setAbsent(String absent) {
        Absent = absent;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getPercentage() {
        return Percentage;
    }

    public void setPercentage(String percentage) {
        Percentage = percentage;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }
}
