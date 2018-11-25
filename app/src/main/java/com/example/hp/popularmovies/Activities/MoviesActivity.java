package com.example.hp.popularmovies.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hp.popularmovies.Adapters.MovieAdapter;
import com.example.hp.popularmovies.Database.FavoriteMovie;
import com.example.hp.popularmovies.Database.MainViewModel;
import com.example.hp.popularmovies.Models.Movie;
import com.example.hp.popularmovies.NetworkUtils.Retrofit;
import com.example.hp.popularmovies.R;
import com.example.hp.popularmovies.ResponseModels.MoviesResponse;
import com.example.hp.popularmovies.Utils.Constants;
import com.example.hp.popularmovies.Utils.Utils;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hp.popularmovies.Utils.Constants.CURRENT_SORT_KEY;
import static com.example.hp.popularmovies.Utils.Constants.FAVOURITE;
import static com.example.hp.popularmovies.Utils.Constants.LIST_POS_KEY;
import static com.example.hp.popularmovies.Utils.Constants.LIST_STATE_KEY;
import static com.example.hp.popularmovies.Utils.Constants.POPULAR_MOVIES;
import static com.example.hp.popularmovies.Utils.Constants.TOP_RATED;
import static com.example.hp.popularmovies.Utils.Constants.mNumberOfColumnsInGridLayout;
import static com.example.hp.popularmovies.Utils.Utils.isConnectedToInternet;

public class MoviesActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    // Views
    private RecyclerView mMoviesList;
    private Toolbar toolbar;
    private TextView title, tv_noFavourite;
    private AVLoadingIndicatorView avi;
    private CoordinatorLayout moviesLayout;

    // Variables
    private  MovieAdapter movieAdapter;
    private List<Movie> tempMovieList;
    private Parcelable mMoviesListState;
    private String mMoviesListPositionKey = "LIST_POSITION_KEY";
    private GridLayoutManager gridLayoutManager;
    private String mCurrentSort;


    //Database
    private List<FavoriteMovie> favoriteMovieList;
    private Boolean isInTopRated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        // Toolbar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.mainTitle);

        avi = findViewById(R.id.avi);

        moviesLayout = findViewById(R.id.movies_constraintLayout);

        tv_noFavourite = findViewById(R.id.movies_noFavourite);

        mMoviesList = findViewById(R.id.rv_movies);
        tempMovieList = new ArrayList<>();

        if (savedInstanceState != null) {
            mCurrentSort = savedInstanceState.getString(CURRENT_SORT_KEY);
        } else {
            mCurrentSort = POPULAR_MOVIES;
        }

        // Recycler View and adapter.
        if(movieAdapter==null) {
            movieAdapter = new MovieAdapter(getApplicationContext(), tempMovieList, this);
            gridLayoutManager = new GridLayoutManager(this, mNumberOfColumnsInGridLayout);
            mMoviesList.setLayoutManager(gridLayoutManager);
            mMoviesList.setHasFixedSize(false);
            mMoviesList.setAdapter(movieAdapter);

            favoriteMovieList = new ArrayList<>();
            setupViewModel();

            sortMovies();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMoviesListState != null) {
            gridLayoutManager.onRestoreInstanceState(mMoviesListState);
        }
    }

    // Menu Functions.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mostPopular:
                mCurrentSort = POPULAR_MOVIES;
                tv_noFavourite.setVisibility(View.GONE);
                sortMovies();
                break;

            case R.id.highestRated:
                mCurrentSort = TOP_RATED;
                tv_noFavourite.setVisibility(View.GONE);
                isInTopRated = true;
                sortMovies();
                break;

            case R.id.favourite:
                mCurrentSort = FAVOURITE;
                sortMovies();
                break;
        }
        return true;
    }

    // Adapter Click Listener.
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Gson gson = new Gson();
        String movie = gson.toJson(tempMovieList.get(clickedItemIndex));
        startActivity(new Intent(MoviesActivity.this, MovieDetailsActivity.class).putExtra("movie", movie));
    }

    /*public void runnable() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPopularMovies();
            }
        }, 5000);
    }*/

    // Calling APIs Functions.
    public void getPopularMovies() {
        startAnim();
        Retrofit.getService(Constants.BASE_URL)
                .getPopularMovies(Constants.API_KEY, Constants.ENGLISH_LANGUAGE, 1)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        tempMovieList.clear();
                        int numOfMovies = response.body().getMovies().size();
                        for (int i = 0; i < numOfMovies; i++) {
                            int id = response.body().getMovies().get(i).getId();
                            String name = response.body().getMovies().get(i).getName();
                            String image = Constants.BASE_IMAGE_URL + response.body().getMovies().get(i).getImage();
                            String releaseDate = response.body().getMovies().get(i).getReleaseDate();
                            float rate = response.body().getMovies().get(i).getRate();
                            String plot = response.body().getMovies().get(i).getPlot();
                            Movie movie = new Movie(id, name, image, releaseDate, rate, plot, getString(R.string.most_popular));
                            tempMovieList.add(movie);
                        }
                        movieAdapter.notifyDataSetChanged();
                        stopAnim();
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        stopAnim();
                    }
                });
    }

    void getTopRatedMovies() {
        startAnim();
        Retrofit.getService(Constants.BASE_URL)
                .getTopRatedMovies(Constants.API_KEY, Constants.ENGLISH_LANGUAGE, 1)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        tempMovieList.clear();
                        int numOfMovies = response.body().getMovies().size();
                        for (int i = 0; i < numOfMovies; i++) {
                            int id = response.body().getMovies().get(i).getId();
                            String name = response.body().getMovies().get(i).getName();
                            String image = Constants.BASE_IMAGE_URL + response.body().getMovies().get(i).getImage();
                            String releaseDate = response.body().getMovies().get(i).getReleaseDate();
                            float rate = response.body().getMovies().get(i).getRate();
                            String plot = response.body().getMovies().get(i).getPlot();
                            Movie movie = new Movie(id, name, image, releaseDate, rate, plot, getString(R.string.top_rated));
                            tempMovieList.add(movie);
                        }
                        movieAdapter.notifyDataSetChanged();
                        stopAnim();
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        stopAnim();
                    }
                });
    }

    // Favourite Movies
    void getFavouriteMovies() {
        tempMovieList.clear();
        if (favoriteMovieList.size() == 0) {
            tv_noFavourite.setVisibility(View.VISIBLE);
        } else {
            tv_noFavourite.setVisibility(View.GONE);
            for (int i = 0; i < favoriteMovieList.size(); i++) {
                Movie favouriteMovie = new Movie(
                        favoriteMovieList.get(i).getId(),
                        favoriteMovieList.get(i).getName(),
                        favoriteMovieList.get(i).getImage(),
                        favoriteMovieList.get(i).getReleaseDate(),
                        favoriteMovieList.get(i).getRate(),
                        favoriteMovieList.get(i).getPlot(),
                        favoriteMovieList.get(i).getCategory()
                );
                tempMovieList.add(favouriteMovie);
            }
            movieAdapter.notifyDataSetChanged();
        }
        stopAnim();
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favoriteMovies) {
                if (favoriteMovies.size() > 0) {
                    favoriteMovieList.clear();
                    favoriteMovieList = favoriteMovies;
                } else if (favoriteMovies.size() == 0) {
                    favoriteMovieList.clear();
                }
                if (title.getText().toString().equals(getResources().getString(R.string.favourite)))
                    getFavouriteMovies();
            }
        });
    }

    //SnackBar
    public void showSuccessfulSnackBar(View view, int message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorSnackBar(final Context context, View view, final String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isConnectedToInternet(context) && !isInTopRated) {
                            showSuccessfulSnackBar(view, R.string.internet_successfully_connected);
                            getPopularMovies();
                        } else if (isConnectedToInternet(context) && isInTopRated) {
                            isInTopRated = false;
                            showSuccessfulSnackBar(view, R.string.internet_successfully_connected);
                            getTopRatedMovies();
                        } else {
                            showErrorSnackBar(context, moviesLayout, getResources().getString(R.string.check_Internet_connection));
                        }
                    }
                })
                .show();
    }

    // Loader.
    void startAnim() {
        avi.smoothToShow();
    }

    void stopAnim() {
        avi.smoothToHide();
    }

    // save list state.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_SORT_KEY, mCurrentSort);
        outState.putParcelable(mMoviesListPositionKey,
                mMoviesList.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mCurrentSort = state.getString(CURRENT_SORT_KEY);
        mMoviesListState = state.getParcelable(mMoviesListPositionKey);
        mMoviesList.getLayoutManager().onRestoreInstanceState(mMoviesListState);
    }


    public void sortMovies() {
        if (mCurrentSort.equals(POPULAR_MOVIES)) {
            title.setText(R.string.most_popular);
            if (!isConnectedToInternet(this)) {
                showErrorSnackBar(getApplicationContext(), moviesLayout, getResources().getString(R.string.check_Internet_connection));
            } else {
                getPopularMovies();
            }
        } else if (mCurrentSort.equals(TOP_RATED)) {
            title.setText(R.string.top_rated);
            if (!isConnectedToInternet(this)) {
                showErrorSnackBar(getApplicationContext(), moviesLayout, getResources().getString(R.string.check_Internet_connection));
            } else {
                getTopRatedMovies();
            }
        } else {
            title.setText(R.string.favourite);
            getFavouriteMovies();
        }

    }

}