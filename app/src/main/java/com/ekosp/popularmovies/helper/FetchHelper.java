package com.ekosp.popularmovies.helper;

import android.util.Log;

import com.ekosp.popularmovies.BuildConfig;
import com.ekosp.popularmovies.model.Movie;
import com.ekosp.popularmovies.model.Trailer;

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
    private final static String FAVORITES = "favotires";
    private TrailerAdapter mTrailerListAdapter;
    private MoviesAdapter mAdapter;

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
//                mAdapter.setMovieList(trailerResult.getResults());
                mTrailerListAdapter.setTrailerList(trailerResult.getResults());
                Log.i("SUKSES", "movieDetailActivity getTrailer: ======================="+trailerResult.getResults());
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

        if (short_by.equals(MOST_POPULAR)) {
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
        } else if (short_by.equals(HIGHEST_RATED)) {
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
        }
    }

}
