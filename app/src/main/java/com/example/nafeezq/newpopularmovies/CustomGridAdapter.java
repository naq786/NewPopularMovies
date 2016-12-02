package com.example.nafeezq.newpopularmovies;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by nafeezq on 11/7/2016.
 * This class is for populating poster bitmaps on the GridView
 */
public class CustomGridAdapter extends BaseAdapter {

    public MainActivityFragment.FetchPosterTask mContext;
    public ArrayList<Bitmap> mThumbIds;

    // Constructor

    public CustomGridAdapter(ArrayList<Bitmap> mPoster) {

        this.mThumbIds = mPoster;
    }

    @Override
    public int getCount() {
        return mThumbIds.size();
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        imageView = new ImageView(parent.getContext());
        imageView.setImageBitmap(mThumbIds.get(position));

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,600));
        imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }

}
