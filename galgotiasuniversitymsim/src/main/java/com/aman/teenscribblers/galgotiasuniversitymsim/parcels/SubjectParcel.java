package com.aman.teenscribblers.galgotiasuniversitymsim.parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aman on 10/6/16.
 */
public class SubjectParcel implements Parcelable {

    private String semester;
    private String subject;
    private String colorCode;
    private String colorText;
    private int present;
    private int absent;
    private int total;
    private float percentage;

    public SubjectParcel(String semester, String subject, int present, int absent, int total, float percentage) {
        this.semester = semester;
        this.subject = subject;
        this.present = present;
        this.absent = absent;
        this.total = total;
        this.percentage = percentage;
    }

    public SubjectParcel(String semester, String subject, String colorCode, String colorText) {
        this.semester = semester;
        this.subject = subject;
        this.colorCode = colorCode;
        this.colorText = colorText;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.semester);
        dest.writeString(this.subject);
        dest.writeString(this.colorCode);
        dest.writeString(this.colorText);
        dest.writeInt(this.present);
        dest.writeInt(this.absent);
        dest.writeInt(this.total);
        dest.writeFloat(this.percentage);
    }

    protected SubjectParcel(Parcel in) {
        this.semester = in.readString();
        this.subject = in.readString();
        this.colorCode = in.readString();
        this.colorText = in.readString();
        this.present = in.readInt();
        this.absent = in.readInt();
        this.total = in.readInt();
        this.percentage = in.readFloat();
    }

    public static final Parcelable.Creator<SubjectParcel> CREATOR = new Parcelable.Creator<SubjectParcel>() {
        public SubjectParcel createFromParcel(Parcel source) {
            return new SubjectParcel(source);
        }

        public SubjectParcel[] newArray(int size) {
            return new SubjectParcel[size];
        }
    };
}
