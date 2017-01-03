package com.example.martin.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.martin.popularmovies.R;
import com.example.martin.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Martin on 18.07.2015.
 */
public class PosterAdapter extends BaseAdapter {
//    private final String LOG_TAG = PosterAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Movie> mMovies;

    public PosterAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        mMovies = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item_poster, parent, false);
        }

        imageView = (ImageView) convertView.findViewById(R.id.grid_item_poster_imageview);
        Picasso.with(mContext).load(mMovies.get(position).getFullPosterURL(Movie.POSTER_SIZE_W342)).placeholder(R.drawable.ic_local_movies_black_48dp).into(imageView);

        return imageView;
    }

    public void add(Movie movie) {
        mMovies.add(movie);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends Movie> collection) {
        mMovies.addAll(collection);
        notifyDataSetChanged();
    }

    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }
}
