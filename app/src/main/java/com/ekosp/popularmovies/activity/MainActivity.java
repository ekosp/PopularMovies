package com.ekosp.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.ekosp.popularmovies.BuildConfig;
import com.ekosp.popularmovies.R;
import com.ekosp.popularmovies.fragment.MovieDetailFragment;
import com.ekosp.popularmovies.helper.FetchHelper;
import com.ekosp.popularmovies.helper.MoviesAdapter;
import com.ekosp.popularmovies.helper.MoviesApiService;
import com.ekosp.popularmovies.model.Movie;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.ekosp.popularmovies.R.id.recyclerView;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.movieCallbacks {

    private MoviesAdapter mAdapter;
    private final static String MOST_POPULAR = "popular";
    private final static String HIGHEST_RATED = "top_rated";
    private final static String FAVORITES = "favotires";
    private final String mSortBy = MOST_POPULAR;
    private FetchHelper fetchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView mRecyclerView;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_movie_list);
        setSupportActionBar(toolbar);

        // add shortcut icon to homescreen
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAppInstalled = false;

        if (!isAppInstalled) {
            //  create shortcut icon at homescreen
            Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource
                .fromContext(getApplicationContext(), R.drawable.app_icon));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intent);

            //make preference true
            SharedPreferences.Editor editor = appPreferences.edit();
            editor.putBoolean("isAppInstalled", true);
            editor.apply();
        }

        mRecyclerView = (RecyclerView) findViewById(recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new MoviesAdapter(this, this);

        mRecyclerView.setAdapter(mAdapter);

        fetchHelper = new FetchHelper();
        fetchHelper.setmAdapter(mAdapter);
        fetchHelper.fetchMovies(mSortBy);

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
                fetchHelper.fetchMovies(HIGHEST_RATED);
                item.setChecked(true);
                break;
            case R.id.sort_by_most_popular:
                fetchHelper.fetchMovies(MOST_POPULAR);
                item.setChecked(true);
                break;
            case R.id.sort_by_favorites:
                fetchHelper.fetchMovies(FAVORITES);
                item.setChecked(true);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void open (Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailFragment.PARAM_MOVIE, movie);
        startActivity(intent);
    }
}
