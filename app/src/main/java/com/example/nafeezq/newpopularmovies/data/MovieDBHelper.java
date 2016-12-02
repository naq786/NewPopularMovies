package com.example.nafeezq.newpopularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nafeezq on 9/25/2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper{

    public static final String LOG_TAG = MovieDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override


    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_MOVIE + "(" + MovieContract.MovieEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_POSTER + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE +
                " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_SYNOPSIS +
                " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_RATING +
                " DOUBLE NOT NULL," +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE +
                " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    // Upgrade database when version is changed.


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_MOVIE);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieContract.MovieEntry.TABLE_MOVIE + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
