package com.aman.teenscribblers.galgotiasuniversitymsim.Parcels;

import android.os.Parcel;
import android.os.Parcelable;

public class SimParcel implements Parcelable {

	public static final Creator<SimParcel> CREATOR = new Creator<SimParcel>() {
		@Override
		public SimParcel createFromParcel(Parcel in) {
			return new SimParcel(in);
		}

		@Override
		public SimParcel[] newArray(int size) {
			return new SimParcel[size];
		}
	};
	private String subject;
	private String month;
	private String sem;
	private int present;
	private int absent;
	private int total;
	private float percent;
	private String timeslot;
	private String status;
	private String classtype;

	public SimParcel(String subject, String sem, int present, int absent, int total, float percent) {
		this.subject = subject;
		this.sem = sem;
		this.present = present;
		this.absent = absent;
		this.total = total;
		this.percent = percent;
	}

	public SimParcel(String subject, String month, String sem, int present,
					 int absent, int total, float percent, String timeslot,
					 String status, String classtype) {
		this.subject = subject;
		this.month = month;
		this.sem = sem;
		this.present = present;
		this.absent = absent;
		this.total = total;
		this.percent = percent;
		this.timeslot = timeslot;
		this.status = status;
		this.classtype = classtype;
	}

	protected SimParcel(Parcel in) {
		subject = in.readString();
		month = in.readString();
		sem = in.readString();
		present = in.readInt();
		absent = in.readInt();
		total = in.readInt();
		percent = in.readFloat();
		timeslot = in.readString();
		status = in.readString();
		classtype = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(subject);
		dest.writeString(month);
		dest.writeString(sem);
		dest.writeInt(present);
		dest.writeInt(absent);
		dest.writeInt(total);
		dest.writeFloat(percent);
		dest.writeString(timeslot);
		dest.writeString(status);
		dest.writeString(classtype);
	}

	public String getSubject() {
		return subject;
	}

	public String getMonth() {
		return month;
	}

	public String getSem() {
		return sem;
	}

	public int getPresent() {
		return present;
	}

	public int getAbsent() {
		return absent;
	}

	public int getTotal() {
		return total;
	}

	public float getPercnt() {
		return percent;
	}

	public String getTimeslot() {
		return timeslot;
	}

	public String getStatus() {
		return status;
	}

	public String getClasstype() {
		return classtype;
	}
}