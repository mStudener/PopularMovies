package com.example.martin.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import com.example.martin.popularmovies.adapter.PosterAdapter;
import com.example.martin.popularmovies.data.Movie;
import com.example.martin.popularmovies.data.MovieResponse;
import com.example.martin.popularmovies.retrofit.TheMovieDBService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Martin on 10.08.2016.
 */
class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private final String API_KEY = "fdb55f833f2bd32ed8b5d5b9a6fd5855"; // Insert your themoviedb.org API key here

    private PosterAdapter mPosterAdapter;

    public FetchMovieTask(PosterAdapter posterAdapter) {
        mPosterAdapter = posterAdapter;
    }


    @Override
    protected List<Movie> doInBackground(String... params) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDBService service = retrofit.create(TheMovieDBService.class);
        Call<MovieResponse> call = service.discoverMovies(params[0], API_KEY);
        Log.d(LOG_TAG, params[0]);

        try {
            Response<MovieResponse> response = call.execute();
            List<Movie> movies = response.body().getResults();
            return movies;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            mPosterAdapter.clear();
            mPosterAdapter.addAll(movies);
        }
    }
} // FetchMovieTask
