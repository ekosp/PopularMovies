package com.ekosp.popularmovies.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ekosp.popularmovies.BuildConfig;
import com.ekosp.popularmovies.model.Movie;
import com.ekosp.popularmovies.model.Trailer;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
        } else if (short_by.equals(FAVORITES)) {
            List<Movie> filem = new List<Movie>() {
                @Override
                public int size() {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public boolean contains(Object o) {
                    return false;
                }

                @NonNull
                @Override
                public Iterator<Movie> iterator() {
                    return null;
                }

                @NonNull
                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @NonNull
                @Override
                public <T> T[] toArray(@NonNull T[] a) {
                    return null;
                }

                @Override
                public boolean add(Movie movie) {
                    return false;
                }

                @Override
                public boolean remove(Object o) {
                    return false;
                }

                @Override
                public boolean containsAll(@NonNull Collection<?> c) {
                    return false;
                }

                @Override
                public boolean addAll(@NonNull Collection<? extends Movie> c) {
                    return false;
                }

                @Override
                public boolean addAll(int index, @NonNull Collection<? extends Movie> c) {
                    return false;
                }

                @Override
                public boolean removeAll(@NonNull Collection<?> c) {
                    return false;
                }

                @Override
                public boolean retainAll(@NonNull Collection<?> c) {
                    return false;
                }

                @Override
                public void clear() {

                }

                @Override
                public Movie get(int index) {
                    return null;
                }

                @Override
                public Movie set(int index, Movie element) {
                    return null;
                }

                @Override
                public void add(int index, Movie element) {

                }

                @Override
                public Movie remove(int index) {
                    return null;
                }

                @Override
                public int indexOf(Object o) {
                    return 0;
                }

                @Override
                public int lastIndexOf(Object o) {
                    return 0;
                }

                @Override
                public ListIterator<Movie> listIterator() {
                    return null;
                }

                @NonNull
                @Override
                public ListIterator<Movie> listIterator(int index) {
                    return null;
                }

                @NonNull
                @Override
                public List<Movie> subList(int fromIndex, int toIndex) {
                    return null;
                }
            };
            //filem.add
            mAdapter.setMovieList(filem);
        }
    }

}
