package com.example.martin.popularmovies.retrofit;

import com.example.martin.popularmovies.data.Movie;
import com.example.martin.popularmovies.data.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Martin on 13.05.2016.
 */
public interface TheMovieDBService {
    @GET("3/movie/{sort_by}")
    Call<MovieResponse> discoverMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);
}
