package com.vengalrao.android.popularmovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.util.Objects;


public class MovieGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>,SharedPreferences.OnSharedPreferenceChangeListener,MoviesAdapter.GridItemClickListener{

    private String cData;
    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private MoviesAdapter mMovieAdapter;
    private MovieNetworkUtilities movieNetworkUtilities;
    private static int LOADER_IDENTIFIER=0;

    public Movie movie;

    public MovieGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_movie_list, container, false);

        mRecyclerView=(RecyclerView)view.findViewById(R.id.recyclerView_movie_fragment);
        mTextView =(TextView)view.findViewById(R.id.error_message);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),2,GridLayoutManager.VERTICAL,true);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mMovieAdapter=new MoviesAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);
        movieNetworkUtilities=new MovieNetworkUtilities();
        movie=new Movie();

        loadMovieData();

        return view;
    }


    public void loadMovieData(){
        Bundle queryBundle=new Bundle();
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getContext());
        queryBundle.putString("sortBy",sharedPreferences.getString(getString(R.string.pref_movie_key),getString(R.string.pref_popular_key)));
        LoaderManager loaderManager=getActivity().getSupportLoaderManager();
        Loader<String> movieLoader=loaderManager.getLoader(LOADER_IDENTIFIER);
        if(movieLoader==null){
            loaderManager.initLoader(LOADER_IDENTIFIER,queryBundle,this);
        }else{
            loaderManager.restartLoader(LOADER_IDENTIFIER,queryBundle,this);
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void showMovieData(){
        mTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage(){
        mTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }


    @Override
    public Loader onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader(getContext()) {
            @Override
            public String loadInBackground() {
                URL url=movieNetworkUtilities.buildUrl(args.getString("sortBy"));
                return movieNetworkUtilities.getResposeFromHttpUrl(url);
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, String data) {
        if(data!=null&&!data.equals("")){
            parseJson(data);
            showMovieData();
        }else {
            showErrorMessage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    public void parseJson(String data){
        cData=data;
        Movie[] movies=null;
        try {
            JSONObject object=new JSONObject(data);
            JSONArray array=object.getJSONArray("results");
            movies=new Movie[array.length()];
            for(int i=0;i<array.length();i++){
                JSONObject jsonObject=array.getJSONObject(i);
                String path=jsonObject.getString("poster_path");
                movies[i]=new Movie();
                if(movies[i]!=null){
                    movies[i].posterPath=movieNetworkUtilities.buidPosterPath(path);
                    movies[i].rating=jsonObject.getString("vote_average");
                }
            }
            mMovieAdapter.setImageData(movies);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Bundle queryBundle=new Bundle();
        queryBundle.putString("sortBy",sharedPreferences.getString(getString(R.string.pref_movie_key),getString(R.string.pref_popular_key)));
        LoaderManager loaderManager=getActivity().getSupportLoaderManager();
        Loader<String> movieLoader=loaderManager.getLoader(LOADER_IDENTIFIER);
        if(movieLoader==null){
            loaderManager.initLoader(LOADER_IDENTIFIER,queryBundle,this);
        }else{
            loaderManager.restartLoader(LOADER_IDENTIFIER,queryBundle,this);
        }
    }

    @Override
    public void onGridItemClick(int clickedItemIndex) {
        try {
            JSONObject object=new JSONObject(cData);
            JSONArray array=object.getJSONArray("results");
            JSONObject jsonObject=array.getJSONObject(clickedItemIndex);
            String poster=movieNetworkUtilities.buidPosterPath(jsonObject.getString("poster_path"));
            String backdrop_path=movieNetworkUtilities.buidPosterPath(jsonObject.getString("backdrop_path"));
            String originalTitle=jsonObject.getString("original_title");
            String releaseDate=jsonObject.getString("release_date");
            String overview=jsonObject.getString("overview");
            String votes=jsonObject.getString("vote_average");

            movie.originalName=originalTitle;
            movie.rating=votes;
            movie.backDropPath=backdrop_path;
            movie.posterPath=poster;
            movie.overview=overview;
            movie.releaseDate=releaseDate;

            Intent intent=new Intent(getContext(),DetailedActivity.class);
            intent.putExtra("Movie",  movie);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
