package com.ekosp.popularmovies.activity;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.fragment.MovieDetailFragment;

/**
 * Created by Eko S.P.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";
    private MovieDetailFragment movieDetailFragment;

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
           // passing movie_id to fragment
           Bundle arguments = new Bundle();
           arguments.putParcelable(MovieDetailFragment.PARAM_MOVIE,
                    getIntent().getParcelableExtra(MovieDetailFragment.PARAM_MOVIE));
           // set fragment programatically
           movieDetailFragment = new MovieDetailFragment();
           movieDetailFragment.setArguments(arguments);
           getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, movieDetailFragment, TAG_FRAGMENT)
                    .commit();
        } else {
           // passing movie_id
           Bundle arguments = new Bundle();
           arguments.putParcelable(MovieDetailFragment.PARAM_MOVIE,
                   getIntent().getParcelableExtra(MovieDetailFragment.PARAM_MOVIE));

           // set existing fragment
           movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
           if (movieDetailFragment != null && movieDetailFragment.isAdded()) {
           getSupportFragmentManager().beginTransaction()
                       .replace(R.id.movie_detail_container, movieDetailFragment, TAG_FRAGMENT)
                       .commit();

           }
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

    //save value on onSaveInstanceState
  @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save fragment state
       movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
       if (movieDetailFragment != null && movieDetailFragment.isVisible()) {
            getSupportFragmentManager().putFragment(outState,TAG_FRAGMENT,movieDetailFragment);
       }
    }

}
