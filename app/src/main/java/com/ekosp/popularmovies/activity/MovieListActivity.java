package com.ekosp.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.data.MovieContract;
import com.ekosp.popularmovies.fragment.MovieDetailFragment;
import com.ekosp.popularmovies.helper.FetchHelper;
import com.ekosp.popularmovies.helper.MoviesAdapter;
import com.ekosp.popularmovies.model.Movie;
import com.ekosp.popularmovies.model.Trailer;

import java.util.ArrayList;
import java.util.List;

import static com.ekosp.popularmovies.R.id.recyclerView;
import static com.ekosp.popularmovies.helper.FetchHelper.calculateNoOfColumns;

/**
 * Created by Eko S.P.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class MovieListActivity extends AppCompatActivity implements MoviesAdapter.movieCallbacks,
        LoaderManager.LoaderCallbacks<Cursor>{

    private final static String MOST_POPULAR = "popular";
    private final static String HIGHEST_RATED = "top_rated";
    private final static String FAVORITES = "favorites";
    private final static String PARAM_MOVIES = "PARAM_MOVIES";
    private final static String PARAM_SORT_BY = "PARAM_SORT_BY";
    private String mSortBy = MOST_POPULAR;
    private static final int FAVORITE_LOADER = 0;
    private FetchHelper fetchHelper;
    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView mRecyclerView;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_movie_list);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(recyclerView);
        int mNoOfColumns = calculateNoOfColumns(getApplicationContext());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
        mAdapter = new MoviesAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        fetchHelper = new FetchHelper();
        fetchHelper.setmAdapter(mAdapter);

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getString(PARAM_SORT_BY);
            if (savedInstanceState.containsKey(PARAM_MOVIES)) {
                List<Movie> movies = savedInstanceState.getParcelableArrayList(PARAM_MOVIES);
                mAdapter.setMovieList(movies);
            }
        } else {
            // Fetch Movies only if savedInstanceState == null
            fetchHelper.fetchMovies(mSortBy);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_short_by, menu);

        switch (mSortBy) {
            case MOST_POPULAR:
                menu.findItem(R.id.sort_by_most_popular).setChecked(true);
                break;
            case HIGHEST_RATED:
                menu.findItem(R.id.sort_by_top_rated).setChecked(true);
                break;
            case FAVORITES:
                menu.findItem(R.id.sort_by_favorites).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_top_rated:
                getSupportLoaderManager().destroyLoader(FAVORITE_LOADER);
                fetchHelper.fetchMovies(HIGHEST_RATED);
                item.setChecked(true);
                break;
            case R.id.sort_by_most_popular:
                getSupportLoaderManager().destroyLoader(FAVORITE_LOADER);
                fetchHelper.fetchMovies(MOST_POPULAR);
                item.setChecked(true);
                break;
            case R.id.sort_by_favorites:
                loadFavoriteFromProvider();
                item.setChecked(true);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavoriteFromProvider() {
        getSupportLoaderManager().initLoader(FAVORITE_LOADER, null, this);
    }

    @Override
    public void open (Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailFragment.PARAM_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save list
        List<Movie> movies = mAdapter.getMovieList();
        ArrayList<Movie> alMovies = new ArrayList<>(movies.size());
        alMovies.addAll(movies);
        if (!movies.isEmpty()) {
            outState.putParcelableArrayList (PARAM_MOVIES, alMovies);
        }
        // save filtering by
        outState.putString(PARAM_SORT_BY, mSortBy);
    }



    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        fetchHelper.fetchFavorite(data);
    }


    @Override
    public void onLoaderReset(Loader loader) {
        // do nothing
    }


}
