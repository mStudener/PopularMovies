package com.example.martin.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.martin.popularmovies.adapter.PosterAdapter;
import com.example.martin.popularmovies.data.Movie;
import com.example.martin.popularmovies.data.MovieContract;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private static final int FAVORITES_LOADER = 0;

    private PosterAdapter mPosterAdapter;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_poster);

        // Create and set the adapter of the grid view
        mPosterAdapter = new PosterAdapter(getActivity());
        gridView.setAdapter(mPosterAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra("Data", (Movie) mPosterAdapter.getItem(position));
                startActivity(detailIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateMovies();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            ArrayList<Movie> favMovies = new ArrayList<>();
            do {
                String id = Integer.toString(data.getInt(MovieContract.MovieEntry.COL_MOVIE_ID));
                String title = data.getString(MovieContract.MovieEntry.COL_TITLE);
                String posterPath = data.getString(MovieContract.MovieEntry.COL_POSTER);
                String overview = data.getString(MovieContract.MovieEntry.COL_OVERVIEW);
                String userRating = data.getString(MovieContract.MovieEntry.COL_USER_RATING);
                String releaseDate = data.getString(MovieContract.MovieEntry.COL_RELEASE_DATE);

                Movie movie = new Movie(id, title, posterPath, overview, userRating, releaseDate);
                favMovies.add(movie);
            } while (data.moveToNext());

            mPosterAdapter.clear();
            mPosterAdapter.addAll(favMovies);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onSortOrderChanged() {
        updateMovies();
    }

    private void updateMovies() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = preferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popularity));

        if (!sortOrder.equals(getString(R.string.pref_sort_favorites))) {
            FetchMovieTask movieTask = new FetchMovieTask(mPosterAdapter);
            movieTask.execute(sortOrder);
        } else {
            // Query local db for favorite movies
            getLoaderManager().initLoader(FAVORITES_LOADER, null, this);
        }
    }
}
