package com.example.martin.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martin.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mDate;
    private TextView mRating;
    private TextView mOverview;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Movie movieData = null;
        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra("Data")) {
            movieData = (Movie) intent.getParcelableExtra("Data");
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mTitle = (TextView)rootView.findViewById(R.id.detail_titel_textview);
        mPoster = (ImageView)rootView.findViewById(R.id.detail_poster_imageview);
        mDate = (TextView)rootView.findViewById(R.id.detail_date_textview);
        mRating = (TextView)rootView.findViewById(R.id.detail_rating_textview);
        mOverview = (TextView)rootView.findViewById(R.id.detail_overview_textview);
        updateViews(movieData);
        return rootView;

    }

    private void updateViews(Movie movieData) {
        mTitle.setText(movieData.getTitle());
        Picasso.with(getActivity()).load(movieData.getFullPosterURL(Movie.POSTER_SIZE_W154)).placeholder(R.drawable.ic_local_movies_black_48dp).into(mPoster);
        mDate.setText(movieData.getmReleaseDate());
        mRating.setText(movieData.getUserRating());
        mOverview.setText(movieData.getOverview());
    }

}
