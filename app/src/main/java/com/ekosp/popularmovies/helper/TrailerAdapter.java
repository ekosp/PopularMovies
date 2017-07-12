package com.ekosp.popularmovies.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eko S.P on 08/07/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieViewHolder> {


    private final List<Trailer> mTrailerList;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final trailerCallbacks mTrailerCallbacks;

    public interface trailerCallbacks {
        void open(Trailer trailer);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
            public MovieViewHolder(View itemView)
            {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
            }

        }

    public TrailerAdapter(Context context, trailerCallbacks mTrailerCallbacks) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
        this.mTrailerCallbacks = mTrailerCallbacks;
        this.mTrailerList = new ArrayList<>();
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
        Trailer trailer = mTrailerList.get(position);
      /*  Picasso.with(mContext)
                .load(trailer.getPoster())
                .placeholder(R.color.colorPrimary)
                .into(holder.imageView);
*/
        // add onclick
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                 int pos = holder.getAdapterPosition();
                Trailer trailer = mTrailerList.get(pos);
                mTrailerCallbacks.open(trailer);

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return (mTrailerList == null) ? 0 : mTrailerList.size();
    }

    public void setMovieList(List<Trailer> trailerList)
    {
        this.mTrailerList.clear();
        this.mTrailerList.addAll(trailerList);
        notifyDataSetChanged();
    }


}