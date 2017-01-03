package com.example.martin.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Martin on 28.07.2016.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.martin.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "favorite_movie";

        // Weather id as returned by API, to identify the movie
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // Projections
        public static final String[] MOVIE_COLUMNS = {
                COLUMN_MOVIE_ID,
                COLUMN_TITLE,
                COLUMN_POSTER,
                COLUMN_OVERVIEW,
                COLUMN_USER_RATING,
                COLUMN_RELEASE_DATE,
        };

        public static final int COL_MOVIE_ID = 0;
        public static final int COL_TITLE = 1;
        public static final int COL_POSTER = 2;
        public static final int COL_OVERVIEW = 3;
        public static final int COL_USER_RATING = 4;
        public static final int COL_RELEASE_DATE = 5;
    }
}
