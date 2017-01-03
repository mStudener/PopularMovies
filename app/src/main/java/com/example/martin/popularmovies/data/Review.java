package com.example.martin.popularmovies.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by martin on 03/01/2017.
 */
public class Review {

    @SerializedName("id")
    private String mId;

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("content")
    private String mContent;

    @SerializedName("url")
    private String mUrl;

    public String getId() {
        return mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getContent() {
        return mContent;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
