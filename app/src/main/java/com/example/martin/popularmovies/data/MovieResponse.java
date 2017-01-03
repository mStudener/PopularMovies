package com.example.martin.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 24.05.2016.
 */
public class MovieResponse {

    @SerializedName("results")
    private List<Movie> mResults;

    public MovieResponse() {
        this.mResults = new ArrayList<Movie>();
    }

    public List<Movie> getResults() {
        return mResults;
    }
}
