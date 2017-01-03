package com.example.martin.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 03/01/2017.
 */
public class ReviewResponse {
    @SerializedName("results")
    private List<Review> mResults;

    public ReviewResponse() {
        mResults = new ArrayList<>();
    }

    public List<Review> getReviews() {
        return mResults;
    }
}
