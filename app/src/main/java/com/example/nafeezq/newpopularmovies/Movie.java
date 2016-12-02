package com.example.nafeezq.newpopularmovies;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by nafeezq on 9/30/2016.
 */
public class Movie implements Serializable {

    String title;
    byte [] poster;
    String synopsis;
    String rating;
    String releaseDate;
    String id;


    public Movie(String title, byte [] poster, String synopsis, String rating, String releaseDate,String id){
        this.title = title;
        this.poster = poster;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id = id;

    }

    public Movie(){

    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {this.id = id;}

}
