package com.example.nafeezq.newpopularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


/*
MainActivity either inflates layout activity_main for Phone or layout-sw600p activity_main for tablet. Layout for tablet is a Two-pane layout
 */
public class MainActivity extends AppCompatActivity {


    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String GRIDFRAGMENT_TAG = "GFTAG";

    //The API Key is used in MainActivityFragment.java, MovieDetailActivityFragment.java and ReviewActivity.java

    //Key removed for GitHub upload
    protected static final String API_KEY = "***************";

    private MovieDetailActivityFragment mdafObject = new MovieDetailActivityFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (findViewById(R.id.movie_details_fragments) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.

            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.gridview_movies_fragment, new MainActivityFragment(), GRIDFRAGMENT_TAG)
                        .commit();

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_details_fragments, mdafObject, DETAILFRAGMENT_TAG)
                        .commit();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        if(id==R.id.action_refresh){

            startActivity(new Intent(this,MainActivity.class));
            return true;
        }

        if(id==R.id.action_favorite){

            startActivity(new Intent(this,FavoriteActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
