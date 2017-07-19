package com.ekosp.popularmovies.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.ekosp.popularmovies.data.MovieContract;
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
    public Movie mMovie;
    public TextView mMovieRatingView;
    private TrailerAdapter mTrailerListAdapter;
    public long idku =0;
    public FetchHelper fetchHelper;
    public Button mButtonMarkAsFavorite, mButtonRemoveFromFavorites;

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
        mButtonMarkAsFavorite = (Button) rootView.findViewById(R.id.button_mark_as_favorite);
        mButtonRemoveFromFavorites = (Button) rootView.findViewById(R.id.button_remove_from_favorites);

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
        updateFavoriteButtons();

        // tampilkan trailer movies
        mRecyclerViewForTrailers = (RecyclerView) rootView.findViewById(R.id.trailer_list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewForTrailers.setLayoutManager(layoutManager);
        mTrailerListAdapter = new TrailerAdapter(getContext(), this); // diisi apa yaaa?

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
   public void open(String url) {
       Toast.makeText(getContext(), "buka trailer dari movie, url :"+url, Toast.LENGTH_SHORT).show();
   }

    public void markAsFavorite() {

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
        // Needed to avoid "skip frames".
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return isFavorite();
            }

            @Override
            protected void onPostExecute(Boolean isFavorite) {
                if (isFavorite) {
                    mButtonRemoveFromFavorites.setVisibility(View.VISIBLE);
                    mButtonMarkAsFavorite.setVisibility(View.GONE);
                } else {
                    mButtonMarkAsFavorite.setVisibility(View.VISIBLE);
                    mButtonRemoveFromFavorites.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        mButtonMarkAsFavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        markAsFavorite();
                    }
                });

       /* mButtonWatchTrailer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTrailerListAdapter.getItemCount() > 0) {
                            watch(mTrailerListAdapter.getTrailers().get(0), 0);
                        }
                    }
                });
*/
        mButtonRemoveFromFavorites.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFromFavorites();
                    }
                });
    }

    public void removeFromFavorites() {
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
}
