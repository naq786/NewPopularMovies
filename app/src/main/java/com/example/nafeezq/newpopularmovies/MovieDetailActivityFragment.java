package com.example.nafeezq.newpopularmovies;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.nafeezq.newpopularmovies.data.MovieContract;
import com.example.nafeezq.newpopularmovies.data.MovieDBHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a Movie Details consist for Title, Poster, Synopsis, Rating, Release, Link to Video Trailer, Link to Movie Review.
 */
public class MovieDetailActivityFragment extends Fragment {
    private String favMovieTitle = null;
    private byte[] favMoviePoster = null;
    private String favMovieSynopsis = null;
    private String favMovieRating = null;
    private String favMovieRelease = null;
    private String movieID=null;
    private SQLiteDatabase dbR;
    private SQLiteDatabase dbW;
    private String favTitle;


    public MovieDetailActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        TextView movieTitleText = (TextView) rootView.findViewById(R.id.titleText);
        ImageView moviePoster = (ImageView) rootView.findViewById(R.id.detailImage);
        TextView movieSynopsisText = (TextView) rootView.findViewById(R.id.detailText);
        TextView movieRatingText = (TextView) rootView.findViewById(R.id.ratingText);
        TextView movieReleaseText = (TextView) rootView.findViewById(R.id.releaseDate);


        Bundle bundleReceive = this.getArguments();

        //bundleReceive contains serialized Movie details if its a two pane layout


        if (bundleReceive!=null){

            Movie movieReceive = (Movie) bundleReceive.getSerializable("MOVIE_KEY");

            String twoPaneTitleText = movieReceive.getTitle();
            String twoPaneSynopsisText = movieReceive.getSynopsis();
            String twoPaneRatingText = movieReceive.getRating();
            String twoPaneReleaseText = movieReceive.getReleaseDate();
            Bitmap twoPanePosterBitmap = BitmapFactory.decodeByteArray(movieReceive.getPoster(), 0, movieReceive.getPoster().length);
            movieID = movieReceive.getId();
            Log.d("MOVIE", movieID);

            movieTitleText.setText(twoPaneTitleText);
            movieSynopsisText.setText(twoPaneSynopsisText);
            movieRatingText.setText(twoPaneRatingText);
            movieReleaseText.setText(twoPaneReleaseText);
            moviePoster.setImageBitmap(twoPanePosterBitmap);

            favMovieTitle= twoPaneTitleText;
            favMovieSynopsis= twoPaneSynopsisText;
            favMovieRating = twoPaneRatingText;
            favMovieRelease = twoPaneReleaseText;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            twoPanePosterBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            favMoviePoster = bos.toByteArray();


        } else {

            //for phone layout

            Intent intent = getActivity().getIntent();

            if (intent.hasExtra("titleIntent")) {
                //retrieving title text sent from MainActivityFragment and populating on MovieDetail activity layout
                String movieTitleFromIntent = intent.getExtras().getString("titleIntent");

                movieTitleText.setText(movieTitleFromIntent);
                favMovieTitle = movieTitleFromIntent;
            }

            if (intent.hasExtra("byteArray")) {
                //decompressing image and populating on MovieDetail activity layout

                Bitmap b = BitmapFactory.decodeByteArray(
                        intent.getByteArrayExtra("byteArray"), 0, intent.getByteArrayExtra("byteArray").length);
                moviePoster.setImageBitmap(b);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 100, bos);
                favMoviePoster = bos.toByteArray();

            }

            if (intent.hasExtra("synopsisIntent")) {
                //retrieving synopsis text sent from MainActivityFragment and populating on MovieDetail activity layout
                String movieSynopsisFromIntent = intent.getExtras().getString("synopsisIntent");
                movieSynopsisText.setText(movieSynopsisFromIntent);
                movieSynopsisText.setMovementMethod(new ScrollingMovementMethod());
                favMovieSynopsis = movieSynopsisFromIntent;
            }

            if (intent.hasExtra("ratingIntent")) {
                //retrieving rating String sent from MainActivityFragment and populating on MovieDetail activity layout
                String movieRatingFromIntent = intent.getExtras().getString("ratingIntent");
                String movieRatingTextFull = "RATING: " + movieRatingFromIntent;
                movieRatingText.setText(movieRatingTextFull);
                favMovieRating = movieRatingFromIntent;
            }
            if (intent.hasExtra("releaseIntent")) {
                //retrieving release date String sent from MainActivityFragment and populating on MovieDetail activity layout
                String movieReleaseFromIntent = intent.getExtras().getString("releaseIntent");
                String movieReleaseTextFull = "Release Date: " + movieReleaseFromIntent;
                movieReleaseText.setText(movieReleaseTextFull);
                favMovieRelease = movieReleaseFromIntent;
            }


            if (intent.hasExtra("idIntent")) {
                //retrieving release date String sent from MainActivityFragment and populating on MovieDetail activity layout
                movieID = intent.getExtras().getString("idIntent");

            }


        }


        //Review Text OnClick Review Activity Launch
        TextView reviewText = (TextView) rootView.findViewById(R.id.reviewText);

        reviewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewActivityIntent = new Intent(getActivity().getApplicationContext(),ReviewActivity.class);
                reviewActivityIntent.putExtra("idMovieReviewIntent",movieID);
                startActivity(reviewActivityIntent);
            }
        });



        //Button for Making a Movie Favorite

        Button FB = (Button)rootView.findViewById(R.id.favButton);

        FB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //to insert favorite movie data in to SQLite DB
                insertData();

            }

        });



        if(movieID!=null){

            FetchVideoKeyTask FV = new FetchVideoKeyTask();
            FV.execute();

        }


        return rootView;
    }

    private void insertData() {

        MovieDBHelper mDB = new MovieDBHelper(getActivity());
        dbR=mDB.getReadableDatabase();
        dbW=mDB.getWritableDatabase();

        String[] col = {"title","poster","synopsis","rating","release"};

        Cursor c = dbR.query(MovieContract.MovieEntry.TABLE_MOVIE,col,null,null,null,null,null);

        boolean matchExist = false;

        if (c!=null && c.moveToFirst()){

            do{

                favTitle = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));


                if(favTitle.equals(favMovieTitle)){

                    Toast.makeText(getActivity(),"Movie Already Exists In Favorite!!",Toast.LENGTH_LONG).show();
                    matchExist = true;
                }

            }while (c.moveToNext());



        }


        if(!matchExist){

            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MovieEntry.COLUMN_TITLE, favMovieTitle);
            cv.put(MovieContract.MovieEntry.COLUMN_POSTER, favMoviePoster);
            cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, favMovieSynopsis);
            cv.put(MovieContract.MovieEntry.COLUMN_RATING,favMovieRating);
            cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,favMovieRelease);

            dbW.insert(MovieContract.MovieEntry.TABLE_MOVIE,null,cv);

        }

    }


    public class FetchVideoKeyTask extends AsyncTask<String, Void, ArrayList<String>> {

        //ArrayList to hold Video Keys require to compose Video Trailer YouTube link

        private ArrayList<String> videoKeyArray = new ArrayList<>();


        public ArrayList<String> getMovieReviewsFromJson(String movieReviewString) throws JSONException {

            //Defining Constants to parse Movie DB JSON Data

            final String MOVIEDB_RESULTS = "results";
            final String MOVIEDB_VIDEO_KEY = "key";


            JSONObject movieReview = new JSONObject(movieReviewString);
            JSONArray movieVideosJSONArray = movieReview.getJSONArray(MOVIEDB_RESULTS);

            for (int i = 0; i < movieVideosJSONArray.length(); i++) {


                JSONObject videos = movieVideosJSONArray.getJSONObject(i);

                String videoTrailerKey = videos.getString(MOVIEDB_VIDEO_KEY);


                videoKeyArray.add(videoTrailerKey);

            }

            return videoKeyArray;

        }


        @Override
        protected ArrayList<String> doInBackground (String...prams){
            //Background method to go to Movie DB Movie Trailers details link based on Movie ID

            //key removed for GitHub upload

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;

            try {
                final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/" + movieID + "/videos?";
                final String APIKEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(APIKEY_PARAM, MainActivity.API_KEY)
                        .build();


                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {

                return getMovieReviewsFromJson(moviesJsonStr);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute (ArrayList reviewArray){

            ImageView playTrailer =(ImageView) getActivity().findViewById(R.id.trailerImage);

            //Play Trailer Button launches **FIRST** trailer

            playTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String firstTrailerKey = videoKeyArray.get(0);

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+firstTrailerKey)));

                }
            });


        }

    }


}
