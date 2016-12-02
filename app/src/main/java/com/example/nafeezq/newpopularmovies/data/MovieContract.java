package com.example.nafeezq.newpopularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nafeezq on 9/25/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.nafeezq.newpopularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class MovieEntry implements BaseColumns {
        // table name
        public static final String TABLE_MOVIE = "movie";
        // columns
        public static final String _ID = "_id";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release";


        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_MOVIE).build();

        // create cursor of base type directory for multiple entries
        public static final String CURSOR_DIR_BASE_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIE;
        // create cursor of base type item for single entry
        public static final String CURSOR_ITEM_BASE_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIE;

        // for building URIs on insertion
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
