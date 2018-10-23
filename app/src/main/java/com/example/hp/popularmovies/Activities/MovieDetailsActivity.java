package com.example.hp.popularmovies.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hp.popularmovies.Models.Movie;
import com.example.hp.popularmovies.R;
import com.google.gson.Gson;

public class MovieDetailsActivity extends AppCompatActivity {

    // Views
    private ImageView iv_movieImage, iv_backArrow;
    private TextView tv_movieName, tv_releaseDate, tv_plot, tv_movieRate, tv_category;

    // Variables
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        initUI();

        Gson gson = new Gson();
        mMovie = gson.fromJson(getIntent().getStringExtra("movie"), Movie.class);

        updateUI();
    }

    private void initUI() {
        iv_backArrow = findViewById(R.id.iv_backArrow);
        iv_movieImage = findViewById(R.id.movieDetails_movieImage);
        tv_movieName = findViewById(R.id.movieDetails_movieName);
        tv_releaseDate = findViewById(R.id.movieDetails_releaseDate);
        tv_plot = findViewById(R.id.movieDetails_plot);
        tv_movieRate = findViewById(R.id.movieDetails_movieRate);
        tv_category = findViewById(R.id.movieDetails_MovieCategory);

        onClickBackArrow();
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

}
