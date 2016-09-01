package com.example.martin.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.martin.popularmovies.adapter.TrailerAdapter;
import com.example.martin.popularmovies.data.Movie;
import com.example.martin.popularmovies.data.MovieContract;
import com.example.martin.popularmovies.data.MovieProvider;
import com.example.martin.popularmovies.data.Trailer;
import com.example.martin.popularmovies.data.TrailerResponse;
import com.example.martin.popularmovies.retrofit.TheMovieDBService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private TrailerAdapter mTrailerAdapter;
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mDate;
    private TextView mRating;
    private TextView mOverview;

    private Movie mMovie;

    public DetailActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("Data")) {
            mMovie = intent.getParcelableExtra("Data");
            fetchTrailers(mMovie.getId());
            updateViews(mMovie);
            addToFavorites(); // TODO Let the user decide
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mTitle = (TextView) rootView.findViewById(R.id.detail_titel_textview);
        mPoster = (ImageView) rootView.findViewById(R.id.detail_poster_imageview);
        mDate = (TextView) rootView.findViewById(R.id.detail_date_textview);
        mRating = (TextView) rootView.findViewById(R.id.detail_rating_textview);
        mOverview = (TextView) rootView.findViewById(R.id.detail_overview_textview);

        mTrailerAdapter = new TrailerAdapter(getActivity());
        ListView mTrailers = (ListView) rootView.findViewById(R.id.detail_trailers_listview);
        mTrailers.setAdapter(mTrailerAdapter);

        mTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = (Trailer) mTrailerAdapter.getItem(position);
                watchTrailer(trailer.getUrl());
            }
        });

        return rootView;
    }

    private void updateViews(Movie movieData) {
        mTitle.setText(movieData.getTitle());
        Picasso.with(getActivity()).load(movieData.getFullPosterURL(Movie.POSTER_SIZE_W185)).placeholder(R.drawable.ic_local_movies_black_48dp).into(mPoster);
        mDate.setText(movieData.getReleaseDate());
        mRating.setText(String.format("%1$s/%2$s", movieData.getUserRating(), Movie.MAX_RATING));
        mOverview.setText(movieData.getOverview());
    }

    private void fetchTrailers(String id) {
        FetchTrailersTask trailersTask = new FetchTrailersTask();
        trailersTask.execute(id);
    }

    private void watchTrailer(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * Helper method to handle insertion of a new movie in the movie database.
     *
     * @return the row ID of the added movie.
     */
    private long addToFavorites() {
        long rowId;

        // TODO Only select the complete row if really necessary
        // Check if the movie already exists in the db
        Cursor movieCursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{ " * " },
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{mMovie.getId()},
                null
        );

        if (movieCursor.moveToFirst()) {
            Log.d(LOG_TAG, "The movie is already stored in the db");
            int movieIdIdx = movieCursor.getColumnIndex(MovieContract.MovieEntry._ID);
            rowId = movieCursor.getLong(movieIdIdx);
        } else {
            Log.d(LOG_TAG, "Add the movie to the db");
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,mMovie.getId());
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER, mMovie.getPosterPath());
            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
            movieValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, mMovie.getUserRating());
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());

            Uri insertedUri = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
            rowId = ContentUris.parseId(insertedUri);
        }

        movieCursor.close();
        return rowId;
    }

    private class FetchTrailersTask extends AsyncTask<String, Void, List<Trailer>> {
        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
        private final String API_KEY = "fdb55f833f2bd32ed8b5d5b9a6fd5855"; // Insert your themoviedb.org API key here


        @Override
        protected List<Trailer> doInBackground(String... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TheMovieDBService service = retrofit.create(TheMovieDBService.class);
            Call<TrailerResponse> call = service.fetchTrailers(params[0], API_KEY);

            try {
                Response<TrailerResponse> response = call.execute();
                List<Trailer> trailers = response.body().getTrailers();
                return trailers;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if (trailers != null) {
                mTrailerAdapter.clear();
                mTrailerAdapter.addAll(trailers);
            }
        }
    } // FetchTrailersTask

}
