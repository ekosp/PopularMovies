package com.ekosp.popularmovies.helper;

import android.content.Context;
import android.database.Cursor;
import android.util.DisplayMetrics;

import com.ekosp.popularmovies.BuildConfig;
import com.ekosp.popularmovies.data.MovieContract;
import com.ekosp.popularmovies.model.Movie;
import com.ekosp.popularmovies.model.Review;
import com.ekosp.popularmovies.model.Trailer;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Eko S.P on 13/07/2017.
 */

public class FetchHelper {

    private final static String MOST_POPULAR = "popular";
    private final static String HIGHEST_RATED = "top_rated";
    private final static String FAVORITES = "favorites";
    private TrailerAdapter mTrailerListAdapter;
    private MoviesAdapter mAdapter;
    private ReviewAdapter mReviewListAdapter;

    public ReviewAdapter getmReviewListAdapter() {
        return mReviewListAdapter;
    }

    public void setmReviewListAdapter(ReviewAdapter mReviewListAdapter) {
        this.mReviewListAdapter = mReviewListAdapter;
    }

    public TrailerAdapter getmTrailerListAdapter() {
        return mTrailerListAdapter;
    }

    public void setmTrailerListAdapter(TrailerAdapter mTrailerListAdapter) {
        this.mTrailerListAdapter = mTrailerListAdapter;
    }

    public MoviesAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(MoviesAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void fetchTrailer(final long movieId) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", BuildConfig.THE_MOVIE_DATABASE_API_KEY);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();


        MoviesApiService service = restAdapter.create(MoviesApiService.class);
        service.getTrailerMovies( movieId, new Callback<Trailer.TrailerResult>() {
            @Override
            public void success(Trailer.TrailerResult trailerResult, Response response) {
                mTrailerListAdapter.setTrailerList(trailerResult.getResults());
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void fetchMovies(String short_by) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", BuildConfig.THE_MOVIE_DATABASE_API_KEY);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        MoviesApiService service = restAdapter.create(MoviesApiService.class);

        switch (short_by) {
            case MOST_POPULAR:
                service.getPopularMovies(new Callback<Movie.MovieResult>() {
                    @Override
                    public void success(Movie.MovieResult movieResult, Response response) {
                        mAdapter.setMovieList(movieResult.getResults());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
                break;
            case HIGHEST_RATED:
                service.getTopRatedMovies(new Callback<Movie.MovieResult>() {
                    @Override
                    public void success(Movie.MovieResult movieResult, Response response) {
                        mAdapter.setMovieList(movieResult.getResults());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
                break;

        }
    }

    public void fetchReview(final long movieId) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", BuildConfig.THE_MOVIE_DATABASE_API_KEY);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();


        MoviesApiService service = restAdapter.create(MoviesApiService.class);
        service.getReviewMovies( movieId, new Callback<Review.ReviewsResult>() {
            @Override
            public void success(Review.ReviewsResult reviewsResult, Response response) {
                mReviewListAdapter.setReviewList(reviewsResult.getResults());
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void fetchFavorite(Cursor cursor) {

        List<Movie> mMovieList = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            for (int i=0; i< cursor.getCount(); i++){
                // set favorite movie to model
                Movie movie = new Movie();
                movie.setId(cursor.getInt(MovieContract.INDEX_COL_MOVIE_ID));
                movie.setPosterFromDb(cursor.getString(MovieContract.INDEX_COL_MOVIE_POSTER_PATH));
                movie.setReleaseDate(cursor.getString(MovieContract.INDEX_COL_MOVIE_RELEASE_DATE));
                movie.setTitle(cursor.getString(MovieContract.INDEX_COL_MOVIE_TITLE));
                movie.setOverview(cursor.getString(MovieContract.INDEX_COL_MOVIE_OVERVIEW));
                movie.setUserRating(cursor.getString(MovieContract.INDEX_COL_MOVIE_VOTE_AVERAGE));
                movie.setBackdropFromDb(cursor.getString(MovieContract.INDEX_COL_MOVIE_BACKDROP_PATH));

                mMovieList.add(movie);
                cursor.moveToNext();
            }
        }

        mAdapter.setMovieList(mMovieList);
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }
}