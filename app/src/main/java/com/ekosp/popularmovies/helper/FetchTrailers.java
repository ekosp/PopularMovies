package com.ekosp.popularmovies.helper;

import android.util.Log;

import com.ekosp.popularmovies.BuildConfig;
import com.ekosp.popularmovies.model.Trailer;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by eko.purnomo on 06/07/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class FetchTrailers {

    public void fetchTrailer(long id) {
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
        service.getTrailerMovies(  id, new Callback<Trailer.TrailerResult>() {
                @Override
                public void success(Trailer.TrailerResult trailerResult, Response response) {
                   // mAdapter.setMovieList(movieResult.getResults());
                    Log.i("SUKSES", "trailer url"+trailerResult.getResults());
                }
                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });


    }
}
