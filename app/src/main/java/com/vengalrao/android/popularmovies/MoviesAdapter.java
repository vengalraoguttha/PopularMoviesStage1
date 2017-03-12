package com.vengalrao.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by vengalrao on 11-03-2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    Movie[] movies;
    final private GridItemClickListener mGridItemClickListener;

    public interface  GridItemClickListener{
        void onGridItemClick(int clickedItemIndex);
    }

    public MoviesAdapter(GridItemClickListener listener){
        mGridItemClickListener=listener;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.grid_item_view;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view=inflater.inflate(layoutIdForGridItem,parent,shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        Movie currentMovie=movies[position];
        if(currentMovie!=null){
            Picasso.with(holder.movieImageView.getContext()).load(currentMovie.posterPath).into(holder.movieImageView);
            holder.rating.setText(currentMovie.rating);
        }
    }

    @Override
    public int getItemCount() {
        if(null == movies)
        return 0;
        else
        return movies.length;
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView movieImageView;
        public final TextView rating;

        public MoviesAdapterViewHolder(View view){
            super(view);
            movieImageView=(ImageView)view.findViewById(R.id.movie_image);
            rating=(TextView)view.findViewById(R.id.rating);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            mGridItemClickListener.onGridItemClick(clickedPosition);
        }
    }

    public void setImageData(Movie[] urls){
        movies=urls;
        notifyDataSetChanged();
    }
}
