package com.aman.teenscribblers.galgotiasuniversitymsim.Parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amankapoor on 09/07/17.
 */

public class ResultParcel implements Parcelable {

    private String subject;
    private String grade;
    private String semester;

    public ResultParcel(String subject, String grade, String semester) {
        this.subject = subject;
        this.grade = grade;
        this.semester = semester;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultParcel that = (ResultParcel) o;

        if (getSubject() != null ? !getSubject().equals(that.getSubject()) : that.getSubject() != null)
            return false;
        if (getGrade() != null ? !getGrade().equals(that.getGrade()) : that.getGrade() != null)
            return false;
        return getSemester() != null ? getSemester().equals(that.getSemester()) : that.getSemester() == null;

    }

    @Override
    public int hashCode() {
        int result = getSubject() != null ? getSubject().hashCode() : 0;
        result = 31 * result + (getGrade() != null ? getGrade().hashCode() : 0);
        result = 31 * result + (getSemester() != null ? getSemester().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResultParcel{" +
                "subject='" + subject + '\'' +
                ", grade='" + grade + '\'' +
                ", semester='" + semester + '\'' +
                '}';
    }

    public String getSubject() {
        return subject;
    }

    public String getGrade() {
        return grade;
    }

    public String getSemester() {
        return semester;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subject);
        dest.writeString(this.grade);
        dest.writeString(this.semester);
    }

    public ResultParcel() {
    }

    protected ResultParcel(Parcel in) {
        this.subject = in.readString();
        this.grade = in.readString();
        this.semester = in.readString();
    }

    public static final Parcelable.Creator<ResultParcel> CREATOR = new Parcelable.Creator<ResultParcel>() {
        public ResultParcel createFromParcel(Parcel source) {
            return new ResultParcel(source);
        }

        public ResultParcel[] newArray(int size) {
            return new ResultParcel[size];
        }
    };
}
