package com.example.martin.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 26.05.2016.
 */
public class TrailerResponse {
    @SerializedName("results")
    private List<Trailer> mResults;

    public TrailerResponse() {
        mResults = new ArrayList<>();
    }

    public List<Trailer> getTrailers() {
        return mResults;
    }
}
