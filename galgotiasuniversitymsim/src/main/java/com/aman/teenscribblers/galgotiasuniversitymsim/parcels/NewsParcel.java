package com.aman.teenscribblers.galgotiasuniversitymsim.parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aman on 24-03-2015 in Galgotias University(mSim).
 */
public class NewsParcel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NewsParcel> CREATOR = new Parcelable.Creator<NewsParcel>() {
        @Override
        public NewsParcel createFromParcel(Parcel in) {
            return new NewsParcel(in);
        }

        @Override
        public NewsParcel[] newArray(int size) {
            return new NewsParcel[size];
        }
    };
    public int id;
    public String note;
    public String image_url;
    public String author;

    public NewsParcel(int id, String note, String image_url, String author) {
        this.id = id;
        this.note = note;
        this.image_url = image_url;
        this.author = author;
    }

    protected NewsParcel(Parcel in) {
        id = in.readInt();
        note = in.readString();
        image_url = in.readString();
        author = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(note);
        dest.writeString(image_url);
        dest.writeString(author);
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getAuthor() {
        return author;
    }


}