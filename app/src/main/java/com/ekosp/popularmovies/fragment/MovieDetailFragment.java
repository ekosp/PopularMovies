package com.ekosp.popularmovies.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekosp.popularmovies.BuildConfig;
import com.ekosp.popularmovies.activity.MovieDetailActivity;
import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.helper.FetchHelper;
import com.ekosp.popularmovies.helper.MoviesAdapter;
import com.ekosp.popularmovies.helper.MoviesApiService;
import com.ekosp.popularmovies.helper.TrailerAdapter;
import com.ekosp.popularmovies.model.Movie;
import com.ekosp.popularmovies.model.Trailer;
import com.ekosp.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.ekosp.popularmovies.R.id.recyclerView;

public class MovieDetailFragment extends Fragment implements TrailerAdapter.trailerCallbacks {

    public static final String PARAM_MOVIE = "PARAM_MOVIE";
    public static final String PARAM_TRAILERS = "PARAM_TRAILERS";
    public static final String PARAM_REVIEWS = "PARAM_REVIEWS";
    private Movie mMovie;
    private TextView mMovieRatingView;
    private TrailerAdapter mTrailerListAdapter;
    private long idku =0;
    private FetchHelper fetchHelper;

    RecyclerView mRecyclerViewForTrailers;
   // private FetchTrailers fetchTrailers;

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

        //Buttom mButtonWatchTrailer = (Buttom) rootView.findViewById(R.id.)
        Button mButtonSaveFavorites = (Button) rootView.findViewById(R.id.button_mark_as_favorite);
        Button mButtonRemoveFavorites = (Button) rootView.findViewById(R.id.button_remove_from_favorites);

        mMovieTitleView.setText(mMovie.getTitle());
        mMovieOverviewView.setText(mMovie.getOverview());

        //mMovieReleaseDateView.setText(getCustomDate(mMovie.getReleaseDate()));
        mMovieReleaseDateView.setText(mMovie.getReleaseDate());
        ImageView mMoviePosterView = (ImageView) rootView.findViewById(R.id.movie_poster);

        Picasso.with(getContext())
                .load(mMovie.getPoster())
                .config(Bitmap.Config.RGB_565)
                .into(mMoviePosterView);

        updateRatingBar();

     // tampilkan trailer movies



        mRecyclerViewForTrailers = (RecyclerView) rootView.findViewById(R.id.trailer_list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewForTrailers.setLayoutManager(layoutManager);
        mTrailerListAdapter = new TrailerAdapter(getContext(), null); // diisi apa yaaa?

        mRecyclerViewForTrailers.setAdapter(mTrailerListAdapter);
        mRecyclerViewForTrailers.setNestedScrollingEnabled(false);

        if (savedInstanceState != null && savedInstanceState.containsKey(PARAM_TRAILERS)) {
            List<Trailer> trailers = savedInstanceState.getParcelableArrayList(PARAM_TRAILERS);
            mTrailerListAdapter.setTrailerList(trailers);
            //mButtonWatchTrailer.setEnabled(true);
        } else {
            fetchHelper = new FetchHelper();
            fetchHelper.setmTrailerListAdapter(mTrailerListAdapter);
            fetchHelper.fetchTrailer(mMovie.getId() );
        }

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

    @Override
    public void open(Trailer trailer) {
        Toast.makeText(getContext(), "buka trailer dari movie id :"+idku, Toast.LENGTH_SHORT).show();
    }
}
