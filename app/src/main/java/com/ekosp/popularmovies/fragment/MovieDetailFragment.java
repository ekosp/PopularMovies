package com.ekosp.popularmovies.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekosp.popularmovies.activity.MovieDetailActivity;
import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.data.MovieContract;
import com.ekosp.popularmovies.helper.FetchHelper;
import com.ekosp.popularmovies.helper.ReviewAdapter;
import com.ekosp.popularmovies.helper.TrailerAdapter;
import com.ekosp.popularmovies.model.Movie;
import com.ekosp.popularmovies.model.Review;
import com.ekosp.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Eko S.P.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class MovieDetailFragment extends Fragment implements TrailerAdapter.trailerCallbacks {

    public static final String PARAM_MOVIE = "PARAM_MOVIE";
    private static final String PARAM_TRAILERS = "PARAM_TRAILERS";
    private static final String PARAM_REVIEWS = "PARAM_REVIEWS";
    private Movie mMovie;
    private TextView mMovieRatingView;
    private TrailerAdapter mTrailerListAdapter;
    private ReviewAdapter mReviewAdapter;
    private Button mButtonAddFavorite, mButtonRemoveFavorites;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(PARAM_MOVIE)) {
            mMovie = getArguments().getParcelable(PARAM_MOVIE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && activity instanceof MovieDetailActivity) {
            appBarLayout.setTitle(mMovie.getTitle());
        }

        ImageView movieBackdrop = ((ImageView) activity.findViewById(R.id.movie_backdrop));
        if (movieBackdrop != null) {
            Picasso.with(activity)
                    .load(mMovie.getBackdrop())
                    .config(Bitmap.Config.RGB_565)
                    .into(movieBackdrop);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_movie_detail, container, false);

        TextView mMovieTitleView = (TextView) rootView.findViewById(R.id.movie_title);
        TextView mMovieOverviewView = (TextView) rootView.findViewById(R.id.movie_overview);
        TextView mMovieReleaseDateView = (TextView) rootView.findViewById(R.id.movie_release_date);
        mMovieRatingView = (TextView) rootView.findViewById(R.id.movie_user_rating);

        mButtonAddFavorite = (Button) rootView.findViewById(R.id.button_mark_as_favorite);
        mButtonRemoveFavorites = (Button) rootView.findViewById(R.id.button_remove_from_favorites);

        mMovieTitleView.setText(mMovie.getTitle());
        mMovieOverviewView.setText(mMovie.getOverview());

        mMovieReleaseDateView.setText(customDate(mMovie.getReleaseDate()));
        ImageView mMoviePosterView = (ImageView) rootView.findViewById(R.id.movie_poster);

        Picasso.with(getContext())
                .load(mMovie.getPoster())
                .config(Bitmap.Config.RGB_565)
                .into(mMoviePosterView);

        updateRatingBar();
        updateFavoriteButtons();

        // tampilkan trailer movies
        RecyclerView mRecyclerViewForTrailers = (RecyclerView) rootView.findViewById(R.id.trailer_list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewForTrailers.setLayoutManager(layoutManager);
        mTrailerListAdapter = new TrailerAdapter(getContext(), this);

        mRecyclerViewForTrailers.setAdapter(mTrailerListAdapter);
        mRecyclerViewForTrailers.setNestedScrollingEnabled(false);

        FetchHelper fetchHelper;
        if (savedInstanceState != null && savedInstanceState.containsKey(PARAM_TRAILERS)) {
            List<Trailer> trailers = savedInstanceState.getParcelableArrayList(PARAM_TRAILERS);
            mTrailerListAdapter.setTrailerList(trailers);
        } else {
            fetchHelper = new FetchHelper();
            fetchHelper.setmTrailerListAdapter(mTrailerListAdapter);
            fetchHelper.fetchTrailer(mMovie.getId() );
        }

        // tampilkan review movie
        RecyclerView mRecycleViewForReviews = (RecyclerView) rootView.findViewById(R.id.review_list);
        mReviewAdapter = new ReviewAdapter(getContext());
        mRecycleViewForReviews.setAdapter(mReviewAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(PARAM_REVIEWS)) {
            List<Review> reviews = savedInstanceState.getParcelableArrayList(PARAM_REVIEWS);
            mReviewAdapter.setReviewList(reviews);
        } else {
            fetchHelper = new FetchHelper();
            fetchHelper.setmReviewListAdapter(mReviewAdapter);
            fetchHelper.fetchReview(mMovie.getId() );
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Trailer> trailers = mTrailerListAdapter.getTrailerList();
        ArrayList<Trailer> alTrailers = new ArrayList<>(trailers.size());
        alTrailers.addAll(trailers);
        if (!trailers.isEmpty()) {
            outState.putParcelableArrayList (PARAM_TRAILERS, alTrailers);
        }

        List<Review> reviews = mReviewAdapter.getmReviewList();
        ArrayList<Review> alReviews = new ArrayList<>(reviews.size());
        alReviews.addAll(reviews);
        if (!reviews.isEmpty()) {
            outState.putParcelableArrayList(PARAM_REVIEWS, alReviews);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.action_menu, menu);
    }

    private void updateRatingBar() {

        if (mMovie.getUserRating() != null && !mMovie.getUserRating().isEmpty()) {
            String userRatingStr = getResources().getString(R.string.user_rating_movie, mMovie.getUserRating());
            mMovieRatingView.setText(userRatingStr);
        } else {
            mMovieRatingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void open(Trailer trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex){
            startActivity(webIntent);
        }
    }

    private void markAsFavorite() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (!isFavorite()) {
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                            mMovie.getId());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                            mMovie.getTitle());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                            mMovie.getPoster());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                            mMovie.getOverview());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                            mMovie.getUserRating());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                            mMovie.getReleaseDate());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
                            mMovie.getBackdrop());
                    getContext().getContentResolver().insert(
                            MovieContract.MovieEntry.CONTENT_URI,
                            movieValues
                    );
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavoriteButtons();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean isFavorite() {
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + mMovie.getId(),
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }

    private void updateFavoriteButtons() {
       new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return isFavorite();
            }

            @Override
            protected void onPostExecute(Boolean isFavorite) {
                if (isFavorite) {
                    mButtonRemoveFavorites.setVisibility(View.VISIBLE);
                    mButtonAddFavorite.setVisibility(View.GONE);
                } else {
                    mButtonAddFavorite.setVisibility(View.VISIBLE);
                    mButtonRemoveFavorites.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        mButtonAddFavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        markAsFavorite();
                    }
                });

        mButtonRemoveFavorites.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFromFavorites();
                    }
                });
    }

    private void removeFromFavorites() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (isFavorite()) {
                    getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + mMovie.getId(), null);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavoriteButtons();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private String customDate (String actual_date){
        SimpleDateFormat month_date = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date date;
        try {
            date = sdf.parse(actual_date);
        } catch (ParseException e) {
            e.printStackTrace();
            return actual_date;
        }

        return month_date.format(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                //get 1st trailer
                Trailer tr = mTrailerListAdapter.getTrailerList().get(0);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mMovie.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, tr.getName() + ": "
                        + tr.getTrailerUrl());
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
