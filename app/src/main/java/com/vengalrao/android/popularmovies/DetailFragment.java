package com.vengalrao.android.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailFragment extends Fragment {

    private ImageView mImageView;
    private ImageView poster;
    private TextView mTextView;
    private TextView release;
    private TextView overview;
    private TextView rating;
    public DetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_detail, container, false);

        mImageView=(ImageView)view.findViewById(R.id.image_poster_detail_frag);
        poster=(ImageView)view.findViewById(R.id.poster);
        mTextView=(TextView)view.findViewById(R.id.original_name);
        release=(TextView)view.findViewById(R.id.release);
        overview=(TextView)view.findViewById(R.id.overview);
        rating=(TextView)view.findViewById(R.id.rating_details);

        Intent intent=getActivity().getIntent();
        Movie movie=(Movie)intent.getSerializableExtra("Movie");
        Picasso.with(getContext()).load(movie.backDropPath).into(mImageView);
        Picasso.with(getContext()).load(movie.posterPath).into(poster);
        mTextView.setText(movie.originalName);
        release.setText(movie.releaseDate);
        overview.setText(movie.overview);
        String temp=movie.rating.concat("/10");
        rating.setText(temp);
        return view;
    }

}
