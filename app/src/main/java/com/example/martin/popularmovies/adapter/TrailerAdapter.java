package com.example.martin.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.martin.popularmovies.R;
import com.example.martin.popularmovies.data.Trailer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Martin on 13.07.2016.
 */
public class TrailerAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Trailer> mTrailers;

    public TrailerAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        mTrailers = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public Object getItem(int position) {
        return mTrailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_trailer, parent, false);
        }

        TextView trailerName = (TextView)convertView.findViewById(R.id.trailer_text_id);
        trailerName.setText(mTrailers.get(position).getName());

        return convertView;
    }

    public void add(Trailer trailer) {
        mTrailers.add(trailer);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends Trailer> collection) {
        mTrailers.addAll(collection);
        notifyDataSetChanged();
    }

    public void clear() {
        mTrailers.clear();
        notifyDataSetChanged();
    }
}
