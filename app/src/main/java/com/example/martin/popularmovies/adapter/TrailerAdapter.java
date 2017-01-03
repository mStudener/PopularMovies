package com.example.martin.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martin.popularmovies.R;
import com.example.martin.popularmovies.data.Review;
import com.example.martin.popularmovies.data.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Martin on 13.07.2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    public interface OnTrailerClickListener {
        void onTrailerClick(String url);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public ImageView mThumbnail;
        public TextView mTrailerName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbnail = (ImageView) mView.findViewById(R.id.trailer_thumbnail_image);
            mTrailerName = (TextView) mView.findViewById(R.id.trailer_text_id);
        }
    }

    private List<Trailer> mTrailers;
    private OnTrailerClickListener mListener;

    public TrailerAdapter(List<Trailer> trailers, OnTrailerClickListener listener) {
        mTrailers = trailers;
        mListener = listener;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, parent, false);
        return new TrailerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        final Trailer trailer = mTrailers.get(position);
        final Context context = holder.mView.getContext();

        // load a thumbnail of the trailer
        holder.mTrailerName.setText(trailer.getName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onTrailerClick(trailer.getUrl());
                }
            }
        });

        Picasso.with(context)
                .load(trailer.getThumbnailUrl())
                .into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void addAll(List<Trailer> trailers) {
        mTrailers.clear();
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }
}
