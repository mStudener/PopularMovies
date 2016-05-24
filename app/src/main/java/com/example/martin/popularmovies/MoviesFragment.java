package com.example.martin.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.martin.popularmovies.adapter.PosterAdapter;
import com.example.martin.popularmovies.data.Movie;
import com.example.martin.popularmovies.data.MovieResponse;
import com.example.martin.popularmovies.retrofit.TheMovieDBService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
//    private final String LOG_TAG = MoviesFragment.class.getSimpleName();

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
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = preferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popularity));
        movieTask.execute(sortOrder);
    }

    private class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        private final String API_KEY = "fdb55f833f2bd32ed8b5d5b9a6fd5855"; // Insert your themoviedb.org API key here


        @Override
        protected List<Movie> doInBackground(String... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TheMovieDBService service = retrofit.create(TheMovieDBService.class);
            Call<MovieResponse> call = service.discoverMovies(params[0], API_KEY);

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

        private String[] fetchTrailer(String id) {
            // Construct the URL for the query
            final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/" + id + "/trailers?";
            final String API_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .build();

            String trailerJSONStr = fetchData(builtUri);

            try {
                return getTrailerDataFromJSON(trailerJSONStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        private String[] getTrailerDataFromJSON(String trailerJSONStr) throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String MDB_YOUTUBE = "youtube";
            final String MDB_SOURCE = "source";

            ArrayList<String> trailers = new ArrayList<>();
            JSONObject trailerJSON = new JSONObject(trailerJSONStr);
            JSONArray trailersArray = trailerJSON.getJSONArray(MDB_YOUTUBE);

            for (int i = 0; i < trailersArray.length(); i++) {
                // Get the JSON object representing a movie
                JSONObject trailer = trailersArray.getJSONObject(i);
                String source = trailer.getString(MDB_SOURCE);
                Log.d(LOG_TAG, source);
                trailers.add(source);
            }

            return trailers.toArray(new String[trailers.size()]);
        }

        private String fetchData(Uri builtUri) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                URL url = new URL(builtUri.toString());

                // Create the request
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer sB = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    sB.append(line);
                }

                if (sB.length() == 0) {
                    // Stream was empty
                    return null;
                }

                movieJsonStr = sB.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return movieJsonStr;
        }


        private String fetchReview(String id) {
            // Construct the URL for the query
            final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/" + id + "/reviews?";
            final String API_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .build();

            return fetchData(builtUri);
        }

    } // FetchMovieTask
}
