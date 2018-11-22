package com.example.hp.popularmovies.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hp.popularmovies.Adapters.ReviewAdapter;
import com.example.hp.popularmovies.Adapters.VideoAdapter;
import com.example.hp.popularmovies.AppExecutors;
import com.example.hp.popularmovies.Database.FavoriteMovie;
import com.example.hp.popularmovies.Database.MovieDatabase;
import com.example.hp.popularmovies.Models.Movie;
import com.example.hp.popularmovies.Models.Review;
import com.example.hp.popularmovies.Models.Video;
import com.example.hp.popularmovies.NetworkUtils.Retrofit;
import com.example.hp.popularmovies.R;
import com.example.hp.popularmovies.ResponseModels.ReviewsResponse;
import com.example.hp.popularmovies.ResponseModels.VideosResponse;
import com.example.hp.popularmovies.ResponseModels.VideosResponse;
import com.example.hp.popularmovies.Utils.Constants;
import com.example.hp.popularmovies.Utils.Utils;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity
        implements VideoAdapter.ListItemClickListener {

    // Views
    private RecyclerView mVideosList, mReviewsList;
    private ImageView iv_movieImage, iv_backArrow, iv_shareImage;
    private TextView tv_movieName, tv_releaseDate, tv_plot, tv_movieRate, tv_category;
    private AVLoadingIndicatorView avi;
    private FloatingActionButton fab_favourite;

    // Variables
    private Movie mMovie;
    private VideoAdapter videoAdapter;
    private List<Video> tempVideoList;
    private ReviewAdapter reviewAdapter;
    private List<Review> tempReviewList;

    //Database Vars
    private MovieDatabase mDb;
    private boolean isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        initUI();

        Gson gson = new Gson();
        mMovie = gson.fromJson(getIntent().getStringExtra("movie"), Movie.class);

        updateUI();

        // Recycler View and adapter.
        mVideosList = findViewById(R.id.rv_videos);
        tempVideoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(getApplicationContext(), tempVideoList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mVideosList.setLayoutManager(linearLayoutManager);
        mVideosList.setHasFixedSize(false);
        mVideosList.setAdapter(videoAdapter);


        getTrailersURL();

        mReviewsList = findViewById(R.id.rv_reviews);
        tempReviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(getApplicationContext(), tempReviewList);
        linearLayoutManager = new LinearLayoutManager(this);
        mReviewsList.setLayoutManager(linearLayoutManager);
        mReviewsList.setHasFixedSize(false);
        mReviewsList.setAdapter(reviewAdapter);

        getReviews();

        mDb = MovieDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FavoriteMovie favoriteMovie = mDb.movieDao().loadMovieById(mMovie.getId());
                if (favoriteMovie != null)
                    updateFavoriteUI(true);
                else
                    updateFavoriteUI(false);
            }
        });
    }

    private void initUI() {
        avi = findViewById(R.id.avi);
        iv_backArrow = findViewById(R.id.iv_backArrow);
        iv_movieImage = findViewById(R.id.movieDetails_movieImage);
        tv_movieName = findViewById(R.id.movieDetails_movieName);
        tv_releaseDate = findViewById(R.id.movieDetails_releaseDate);
        tv_plot = findViewById(R.id.movieDetails_plot);
        tv_movieRate = findViewById(R.id.movieDetails_movieRate);
        tv_category = findViewById(R.id.movieDetails_MovieCategory);
        fab_favourite = findViewById(R.id.movieDetails_btnFavourite);
        iv_shareImage = findViewById(R.id.iv_shareImage);

        onClickBackArrow();
        onClickFavouriteButton();
        onClickShareImage();
    }

    private void updateUI() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_placeholder_image);

        Glide.with(this).load(mMovie.getImage()).thumbnail(0.5f).apply(requestOptions).into(iv_movieImage);
        tv_movieName.setText(mMovie.getName());
        tv_releaseDate.setText(mMovie.getReleaseDate());
        tv_plot.setText(mMovie.getPlot());
        tv_movieRate.setText(String.valueOf(mMovie.getRate()));
        tv_category.setText(mMovie.getCategory());
    }


    // onClick Methods
    private void onClickBackArrow() {
        iv_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    // Adapter Click Listener.
    @Override
    public void onListItemClick(int clickedItemIndex) {
        String url = tempVideoList.get(clickedItemIndex).getKey();
        Utils.watchYoutubeVideo(getApplicationContext(), url);
    }

    //Calling API Functions
    public void getTrailersURL() {
        startAnim();
        Retrofit.getService(Constants.BASE_URL)
                .getTrailer(mMovie.getId(), Constants.API_KEY)
                .enqueue(new Callback<VideosResponse>() {
                    @Override
                    public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                        tempVideoList.clear();

                        for (int i = 0; i < response.body().getResults().size(); i++) {
                            tempVideoList.add(response.body().getResults().get(i));
                        }
                        videoAdapter.notifyDataSetChanged();
                        stopAnim();
                    }

                    @Override
                    public void onFailure(Call<VideosResponse> call, Throwable t) {
                        stopAnim();
                    }
                });
    }

    public void getReviews() {
        startAnim();
        Retrofit.getService(Constants.BASE_URL)
                .getReview(mMovie.getId(), Constants.API_KEY)
                .enqueue(new Callback<ReviewsResponse>() {
                    @Override
                    public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                        tempReviewList.clear();

                        for (int i = 0; i < response.body().getResults().size(); i++) {
                            tempReviewList.add(response.body().getResults().get(i));
                        }
                        if (response.body().getResults().size() == 0) {
                            tempReviewList.add(null);
                        }
                        reviewAdapter.notifyDataSetChanged();
                        stopAnim();
                    }

                    @Override
                    public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                        stopAnim();
                    }
                });
    }

    //Share Movie
    private void onClickShareImage() {
        iv_shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != tempVideoList.get(0).getKey()) {
                    ShareCompat.IntentBuilder.from(MovieDetailsActivity.this)
                            .setType("text/plain")
                            .setChooserTitle("Share URL")
                            .setText("http://www.youtube.com/watch?v=" + tempVideoList.get(0).getKey())
                            .startChooser();
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "This movie cannot be shared", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Favourite Movie
    private void onClickFavouriteButton() {
        fab_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FavoriteMovie favoriteMovie = new FavoriteMovie(mMovie.getId(),
                        mMovie.getName(),
                        mMovie.getImage(),
                        mMovie.getReleaseDate(),
                        mMovie.getRate(),
                        mMovie.getPlot(),
                        mMovie.getCategory()
                );

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isFavourite) {
                            mDb.movieDao().deleteMovie(favoriteMovie);
                        } else {
                            mDb.movieDao().insertMovie(favoriteMovie);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateFavoriteUI(!isFavourite);
                            }
                        });
                    }
                });


            }
        });
    }

    private void updateFavoriteUI(Boolean favorite) {
        if (favorite) {
            isFavourite = true;
            fab_favourite.setImageResource(R.drawable.ic_favorite);
        } else {
            isFavourite = false;
            fab_favourite.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    // Loader.
    void startAnim() {
        avi.smoothToShow();
    }

    void stopAnim() {
        avi.smoothToHide();
    }

}
