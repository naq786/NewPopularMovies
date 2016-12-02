package com.example.nafeezq.newpopularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by nafeezq on 11/7/2016.

 This Activity launches with an intent passed from FavoriteActivity class when clicking Favorite Movies posters.
 */
public class FavMovieDetail extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_movie_detail);


        Intent intent = getIntent();


        if(getIntent().hasExtra("favTitleIntent")){
            //retrieving favorite movie title text sent from FavoriteActivity and populating on FavMovieDetail activity layout
            String movieTitleFromIntent = intent.getExtras().getString("favTitleIntent");
            TextView movieTitleText = (TextView) findViewById(R.id.titleText);
            movieTitleText.setText(movieTitleFromIntent);

        }

        if(intent.hasExtra("favByteArray")) {
            //decompressing image and populating on FavMovieDetail activity layout
            ImageView moviePoster= (ImageView) findViewById(R.id.detailImage);
            Bitmap b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("favByteArray"),0,intent.getByteArrayExtra("favByteArray").length);
            moviePoster.setImageBitmap(b);

        }

        if(getIntent().hasExtra("favSynopsisIntent")){
            //retrieving synopsis text sent from FavoriteActivity and populating on FavMovieDetail activity layout
            String movieSynopsisFromIntent = intent.getExtras().getString("favSynopsisIntent");
            TextView movieSynopsisText = (TextView) findViewById(R.id.detailText);
            movieSynopsisText.setText(movieSynopsisFromIntent);
            movieSynopsisText.setMovementMethod(new ScrollingMovementMethod());
        }

        if(getIntent().hasExtra("favRatingIntent")){
            //retrieving rating String sent from FavoriteActivity and populating on FavMovieDetail activity layout
            String movieRatingFromIntent = intent.getExtras().getString("favRatingIntent");
            TextView movieRatingText = (TextView) findViewById(R.id.ratingText);
            String movieRatingTextFull = "RATING: " + movieRatingFromIntent;
            movieRatingText.setText(movieRatingTextFull);
        }
        if(getIntent().hasExtra("favReleaseIntent")){
            //retrieving release date String sent from FavoriteActivity and populating on FavMovieDetail activity layout
            String movieReleaseFromIntent = intent.getExtras().getString("favReleaseIntent");
            TextView movieReleaseText = (TextView) findViewById(R.id.releaseDate);
            String movieReleaseTextFull = "Release Date: "+ movieReleaseFromIntent;
            movieReleaseText.setText(movieReleaseTextFull);
        }


    }


}
