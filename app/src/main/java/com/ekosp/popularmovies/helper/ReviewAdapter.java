package com.ekosp.popularmovies.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eko S.P.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<Review> mReviewList;

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

    public ReviewAdapter(Context context) {
        this.mReviewList = new ArrayList<>();
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

    public List<Review> getmReviewList() {
        return mReviewList;
    }


}