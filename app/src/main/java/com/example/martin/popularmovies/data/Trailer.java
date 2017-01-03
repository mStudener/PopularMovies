package com.example.martin.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Martin on 26.05.2016.
 */
public class Trailer {
    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("key")
    private String mKey;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getKey() {
        return mKey;
    }

    public String getUrl() {
        return "http://www.youtube.com/watch?v=" + mKey;
    }

    public String getThumbnailUrl() { return "http://img.youtube.com/vi/" + mKey + "/hqdefault.jpg";}
}
