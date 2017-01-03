package com.example.martin.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.martin.popularmovies.R;
import com.example.martin.popularmovies.data.Review;

import java.util.List;

/**
 * Created by martin on 03/01/2017.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView mAuthor;
        public TextView mContent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAuthor = (TextView) mView.findViewById(R.id.review_author_textview);
            mContent = (TextView) mView.findViewById(R.id.review_content_textview);
        }
    }

    private List<Review> mReviews;

    public ReviewAdapter(List<Review> reviews) {
        mReviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.mAuthor.setText(review.getAuthor());
        holder.mContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void addAll(List<Review> reviews) {
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }
}
