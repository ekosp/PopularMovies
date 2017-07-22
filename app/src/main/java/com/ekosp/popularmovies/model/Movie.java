package com.ekosp.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Eko S.P.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class Movie implements Parcelable {

    private static final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w500";

    @SerializedName("id")
    private long id;
    @SerializedName("original_title")
    private String title;
    @SerializedName("poster_path")
    private String poster;
    @SerializedName("overview")
    private String overview;
    @SerializedName("vote_average")
    private String userRating;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("backdrop_path")
    private String backdrop;

    public Movie() {}

    private Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        poster = in.readString();
        backdrop = in.readString();
        overview = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
     }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return TMDB_IMAGE_PATH + poster;
    }

    public void setPosterFromDb(String poster){
        this.poster = poster.replace(TMDB_IMAGE_PATH,"");
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBackdrop() {
        return TMDB_IMAGE_PATH  + backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public void setBackdropFromDb(String backdrop) {
        this.backdrop = backdrop.replace(TMDB_IMAGE_PATH,"");
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(poster);
        parcel.writeString(backdrop);
        parcel.writeString(overview);
        parcel.writeString(userRating);
        parcel.writeString(releaseDate);
    }

    public String toString(){
        return title+"/"+poster+"/"+backdrop+"/"+overview+"/"+userRating+"/"+releaseDate;
    }

    public class MovieResult {

        private List<Movie> results;

        public List<Movie> getResults() {
            return results;
        }
    }

}
