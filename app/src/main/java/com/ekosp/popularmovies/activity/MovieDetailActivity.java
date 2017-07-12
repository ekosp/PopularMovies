package com.ekosp.popularmovies.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.ekosp.popularmovies.BuildConfig;
import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.fragment.MovieDetailFragment;
import com.ekosp.popularmovies.helper.MoviesApiService;
import com.ekosp.popularmovies.helper.TrailerAdapter;
import com.ekosp.popularmovies.model.Movie;
import com.ekosp.popularmovies.model.Trailer;
import com.ekosp.popularmovies.model.Trailer3;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private TrailerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

       if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.PARAM_MOVIE,
                    getIntent().getParcelableExtra(MovieDetailFragment.PARAM_MOVIE));
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
          // Movie aa =  arguments().getParcelable(PARAM_MOVIE);
           fetchTrailer(1212);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchTrailer(final long movieId) {

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
        service.getTrailerMovies2( new Callback<Trailer.MovieResult>() {
            @Override
            public void success(Trailer.MovieResult trailerResult, Response response) {
//                mAdapter.setMovieList(trailerResult.getResults());
                Log.i("SUKSES", "movieDetailActivity getTrailer: ======================="+trailerResult.getResults());
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });


    }
}
