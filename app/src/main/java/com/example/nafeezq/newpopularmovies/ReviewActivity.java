package com.example.nafeezq.newpopularmovies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by nafeezq on 11/9/2016.
 */
public class ReviewActivity extends Activity{

    private String movieIDFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        Intent intent = getIntent();

        if(getIntent().hasExtra("idMovieReviewIntent")){
            //retrieving movie ID  String sent from MainActivityDetailFragment
            movieIDFromIntent = intent.getExtras().getString("idMovieReviewIntent");
        }

        FetchReviewTask FT = new FetchReviewTask();
        FT.execute();

    }


    public class FetchReviewTask extends AsyncTask<String, Void, ArrayList<String>> {

        //ArrayList to hold review Authors and Review Text
        private ArrayList<String> reviewArray = new ArrayList<>();


        public ArrayList<String> getMovieReviewsFromJson(String movieReviewString) throws JSONException {

            //Defining Constants to parse Movie DB JSON Data

            final String MOVIEDB_RESULTS = "results";
            final String MOVIEDB_REVIEW_AUTHOR = "author";
            final String MOVIEDB_REVIEW = "content";

            JSONObject movieReview = new JSONObject(movieReviewString);
            JSONArray movieReviewJSONArray = movieReview.getJSONArray(MOVIEDB_RESULTS);

            for (int i = 0; i < movieReviewJSONArray.length(); i++) {


                JSONObject review = movieReviewJSONArray.getJSONObject(i);

                String reviewAuthor = review.getString(MOVIEDB_REVIEW_AUTHOR);
                String reviewContent = review.getString(MOVIEDB_REVIEW);

                Log.d("MovieReviewAuthor",reviewAuthor);

                reviewArray.add(reviewAuthor);
                reviewArray.add(reviewContent);


            }

            return reviewArray;

        }


        @Override
        protected ArrayList<String> doInBackground (String...prams){

            //key removed for GitHub upload

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;

            try {
                final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/" + movieIDFromIntent + "/reviews?";
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

            if(reviewArray.size()!=0){

                for(int i=0;i<reviewArray.size();i++){

                    TextView review = (TextView)findViewById(R.id.fullReview);

                    String reviewItem = (String) (reviewArray.get(i));
                    review.append(reviewItem);
                    review.append("\n");

                }

            } else{


                TextView review = (TextView)findViewById(R.id.fullReview);
                review.setText("THE MOVIE DOES NOT HAVE ANY REVIEW AVAILABLE YET!!");

            }


        }

    }


        }





