package com.aman.teenscribblers.galgotiasuniversitymsim.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aman on 24-03-2015 in Galgotias University(mSim).
 */
public class NewsParcel implements Parcelable {
    public int id;
    public String note;
    @SerializedName("i_url")
    public String image_url;
    @SerializedName("author")
    public String author;
    @SerializedName("a_email")
    public String authorEmail;
    @SerializedName("a_pic")
    public String authorPic;

    public NewsParcel(int id, String note, String image_url, String author, String authorEmail, String authorPic) {
        this.id = id;
        this.note = note;
        this.image_url = image_url;
        this.author = author;
        this.authorEmail = authorEmail;
        this.authorPic = authorPic;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getAuthorPic() {
        return authorPic;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.note);
        dest.writeString(this.image_url);
        dest.writeString(this.author);
        dest.writeString(this.authorEmail);
        dest.writeString(this.authorPic);
    }

    protected NewsParcel(Parcel in) {
        this.id = in.readInt();
        this.note = in.readString();
        this.image_url = in.readString();
        this.author = in.readString();
        this.authorEmail = in.readString();
        this.authorPic = in.readString();
    }

    public static final Creator<NewsParcel> CREATOR = new Creator<NewsParcel>() {
        public NewsParcel createFromParcel(Parcel source) {
            return new NewsParcel(source);
        }

        public NewsParcel[] newArray(int size) {
            return new NewsParcel[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsParcel that = (NewsParcel) o;

        if (id != that.id) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;
        return image_url != null ? image_url.equals(that.image_url) : that.image_url == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (image_url != null ? image_url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewsParcel{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", image_url='" + image_url + '\'' +
                ", author='" + author + '\'' +
                ", authorEmail='" + authorEmail + '\'' +
                ", authorPic='" + authorPic + '\'' +
                '}';
    }
}