package com.ekosp.popularmovies.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekosp.popularmovies.activity.MovieDetailActivity;
import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MovieDetailFragment extends Fragment {

    public static final String PARAM_MOVIE = "PARAM_MOVIE";
    private Movie mMovie;
    private TextView mMovieRatingView;

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

        mMovieTitleView.setText(mMovie.getTitle());
        mMovieOverviewView.setText(mMovie.getOverview());

        mMovieReleaseDateView.setText(getCustomDate(mMovie.getReleaseDate()));
        ImageView mMoviePosterView = (ImageView) rootView.findViewById(R.id.movie_poster);

        Picasso.with(getContext())
                .load(mMovie.getPoster())
                .config(Bitmap.Config.RGB_565)
                .into(mMoviePosterView);

        updateRatingBar();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    private void updateRatingBar() {

        if (mMovie.getUserRating() != null && !mMovie.getUserRating().isEmpty()) {
            String userRatingStr = getResources().getString(R.string.user_rating_movie, mMovie.getUserRating());
            mMovieRatingView.setText(userRatingStr);
        } else {
            mMovieRatingView.setVisibility(View.GONE);
        }
    }

    private String getCustomDate(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        Calendar  calendar = new GregorianCalendar();

        try {
            Date aa = sdf.parse(dateString);
            calendar.setTime( aa );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(calendar.getTime());
    }

}
