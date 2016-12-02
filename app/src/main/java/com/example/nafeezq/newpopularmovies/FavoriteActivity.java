package com.example.nafeezq.newpopularmovies;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;

import android.app.LoaderManager;
import android.content.Loader;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


import com.example.nafeezq.newpopularmovies.data.MovieContract;
import com.example.nafeezq.newpopularmovies.data.MovieDBHelper;
import com.example.nafeezq.newpopularmovies.data.MovieProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/*
 * The Activity launches when Favorite Movies from Menu is selected. Upon selection of Favorite Movies an Intent
 * from MainAcivity triggers to launch FavoriteActivity
 */

public class FavoriteActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 101;

    private byte[] posterImage;
    private GridView mFavGridView;


    private ArrayList<Bitmap> favPosters = new ArrayList<>();
    private ArrayList<String> favTitleArray = new ArrayList<>();
    private ArrayList<String> favSynopsisArray = new ArrayList<>();
    private ArrayList<String> favRatingArray = new ArrayList<>();
    private ArrayList<String> favReleaseArray = new ArrayList<>();

    private String favTitle;
    private String favSynopsis;
    private String favRating;
    private String favRelease;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_favorite);
            mFavGridView = (GridView) findViewById(R.id.fav_gridview_movies);

            // initialize loader
             getLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        CursorLoader c = new CursorLoader(this,MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);

        return c;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {

        if (c!=null && c.moveToFirst()){


            do{
                //posterImage is a Byte Array
                posterImage = c.getBlob(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));
                ByteArrayInputStream ips = new ByteArrayInputStream(posterImage);
                Bitmap posterBitmap = BitmapFactory.decodeStream(ips);
                Log.d("Movie", Integer.toString(posterImage.length));
                favPosters.add(posterBitmap);


                favTitle = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                favSynopsis = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS));
                favRating = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
                favRelease = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));


                favTitleArray.add(favTitle);
                favSynopsisArray.add(favSynopsis);
                favRatingArray.add(favRating);
                favReleaseArray.add(favRelease);


            }while (c.moveToNext());
        }else{

            Toast.makeText(this,"There are no movies selected as Favorite!!",Toast.LENGTH_LONG).show();
        }




        mFavGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // start-media code could go in fragment or adapter
                Intent intent = new Intent(getApplicationContext(), FavMovieDetail.class);

                Bitmap b = favPosters.get(position);

                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 50, bs);
                intent.putExtra("favByteArray", bs.toByteArray());
                intent.putExtra("favTitleIntent",favTitleArray.get(position));
                intent.putExtra("favSynopsisIntent",favSynopsisArray.get(position));
                intent.putExtra("favRatingIntent",favRatingArray.get(position));
                intent.putExtra("favReleaseIntent",favReleaseArray.get(position));

                startActivity(intent);
            }
        });

        CustomGridAdapter adapter = new CustomGridAdapter(favPosters);
        mFavGridView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
