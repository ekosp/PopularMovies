package com.ekosp.popularmovies.helper;

import com.ekosp.popularmovies.model.Movie;
import com.ekosp.popularmovies.model.Trailer;
import com.ekosp.popularmovies.model.Trailer3;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Eko S.P on 01/07/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public interface MoviesApiService {

    @GET("/movie/popular")
    void getPopularMovies(Callback<Movie.MovieResult> callback);

    @GET("/movie/top_rated")
    void getTopRatedMovies(Callback<Movie.MovieResult> callback);

    @GET("/movie/{id}/videos")
    void getTrailerMovies(@Path("id") long movieId, Callback<Trailer.TrailerResult> callback );


    // @GET("/movie/popular")
    @GET("/movie/321612/videos")
    void getTrailerMovies2 (Callback<Trailer.TrailerResult> callback);
}