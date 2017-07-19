package com.ekosp.popularmovies.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.model.Review;
import com.ekosp.popularmovies.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eko S.P on 08/07/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<Review> mReviewList;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final reviewCallbacks mReviewCallbacks;

    public interface reviewCallbacks {
        void open(Review review);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public final TextView mContentView;
        public final TextView mAuthorView;

        public ReviewViewHolder(View itemView)
            {
                super(itemView);
                mContentView = (TextView) itemView.findViewById(R.id.review_content);
                mAuthorView = (TextView) itemView.findViewById(R.id.review_author);
        }
    }

    public ReviewAdapter(Context context, reviewCallbacks mReviewCallbacks) {
        this.mReviewList = new ArrayList<>();
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mReviewCallbacks = mReviewCallbacks;

    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_content, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position)
    {
        final Review review = mReviewList.get(position);
        holder.mContentView.setText(review.getmContent());
        holder.mAuthorView.setText(review.getmAuthor());

        // add onclick
      /*  holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                 int pos = holder.getAdapterPosition();
                Trailer trailer = mTrailerList.get(pos);
                mTrailerCallbacks.open(trailer);

            }
        });*/
    }

    @Override
    public int getItemCount()
    {
        return (mReviewList == null) ? 0 : mReviewList.size();
    }

    public void setReviewList(List<Review> reviewList)
    {
        this.mReviewList.clear();
        this.mReviewList.addAll(reviewList);
        notifyDataSetChanged();
    }


}