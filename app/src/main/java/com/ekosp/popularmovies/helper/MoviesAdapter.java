package com.ekosp.popularmovies.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.ekosp.popularmovies.R.id.recyclerView;

/**
 * Created by Eko S.P on 08/07/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {


    private final List<Movie> mMovieList;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final movieCallbacks mMovieCallbacks;

    public interface movieCallbacks {
        void open(Movie movie);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
            public MovieViewHolder(View itemView)
            {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
            }

        }

    public MoviesAdapter(Context context, movieCallbacks mMovieCallbacks) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMovieCallbacks = mMovieCallbacks;
        this.mMovieList = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_movie, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position)
    {
        Movie movie = mMovieList.get(position);
        Picasso.with(mContext)
                .load(movie.getPoster())
                .placeholder(R.color.colorPrimary)
                .into(holder.imageView);

        // add onclick
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                 int pos = holder.getAdapterPosition();
                Movie movie = mMovieList.get(pos);
                mMovieCallbacks.open(movie);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList)
    {
        this.mMovieList.clear();
        this.mMovieList.addAll(movieList);
        notifyDataSetChanged();
    }


}