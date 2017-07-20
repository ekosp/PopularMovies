package com.ekosp.popularmovies.helper;

import android.content.Context;
import android.graphics.Bitmap;
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
 * Created by Eko S.P.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieViewHolder> {


    public List<Trailer> getTrailerList() {
        return mTrailerList;
    }

    private final List<Trailer> mTrailerList;
    private final Context mContext;
    private final trailerCallbacks mTrailerCallbacks;

    public interface trailerCallbacks {
       void open(Trailer trailer);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mThumbnailView;
        public MovieViewHolder(View itemView)
        {
           super(itemView);
           mThumbnailView = (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
        }
        }

    public TrailerAdapter(Context context, trailerCallbacks mTrailerCallbacks) {
            this.mContext = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        this.mTrailerCallbacks = mTrailerCallbacks;
        this.mTrailerList = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_content, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position)
    {
        final Trailer trailer = mTrailerList.get(position);
        String thumbnailUrl = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";
        Picasso.with(mContext)
                .load(thumbnailUrl)
                .config(Bitmap.Config.RGB_565)
                .into(holder.mThumbnailView);
        // add onclick
        holder.mThumbnailView.setOnClickListener(new View.OnClickListener() {
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

    public void setTrailerList(List<Trailer> trailerList)
    {
        this.mTrailerList.clear();
        this.mTrailerList.addAll(trailerList);
        notifyDataSetChanged();
    }


}