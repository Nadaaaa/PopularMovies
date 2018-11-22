package com.example.hp.popularmovies.NetworkUtils;

import com.example.hp.popularmovies.ResponseModels.MoviesResponse;
import com.example.hp.popularmovies.ResponseModels.ReviewsResponse;
import com.example.hp.popularmovies.ResponseModels.VideosResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkUtils {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}/videos")
    Call<VideosResponse> getTrailer(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getReview(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );
}
