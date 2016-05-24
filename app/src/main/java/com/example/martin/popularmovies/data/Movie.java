package com.example.martin.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Martin on 26.07.2015.
 */
public class Movie implements Parcelable {
    private static final String POSTER_BASE_PATH = "http://image.tmdb.org/t/p/";
    public static final String MAX_RATING = "10";
    public static final String POSTER_SIZE_W92 = "w92/";
    public static final String POSTER_SIZE_W154 = "w154/";
    public static final String POSTER_SIZE_W185 = "w185/";
    public static final String POSTER_SIZE_W342 = "w342/";
    public static final String POSTER_SIZE_W500 = "w500/";
    public static final String POSTER_SIZE_W780 = "w780/";
    public static final String POSTER_SIZE_ORIGINAL = "original";

    @SerializedName("title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("vote_average")
    private String mUserRating;

    @SerializedName("release_date")
    private String mReleaseDate;

    public Movie(String title, String posterPath, String overview, String userRating, String releaseDate) {
        this.mTitle = title;
        this.mPosterPath = posterPath;
        this.mOverview = overview;
        this.mUserRating = userRating;
        this.mReleaseDate = releaseDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getUserRating() { return mUserRating; }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    /**
     * @return Fully qualified poster URL with default poster size.
     */
    public String getFullPosterURL() {
        return getFullPosterURL(POSTER_SIZE_W185);
    }

    /**
     * @param posterSize Preferred poster size.
     * @return Fully qualified poster URL for a given {@code posterSize}.
     */
    public String getFullPosterURL(String posterSize) {
        return POSTER_BASE_PATH + posterSize + mPosterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeString(mUserRating);
        dest.writeString(mReleaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        this.mTitle = in.readString();
        this.mPosterPath = in.readString();
        this.mOverview = in.readString();
        this.mUserRating = in.readString();
        this.mReleaseDate = in.readString();
    }
}
