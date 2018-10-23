package com.example.hp.popularmovies.ResponseModels;

import com.example.hp.popularmovies.Models.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {
    @SerializedName("page")
    @Expose
    private int Page;

    @SerializedName("total_results")
    @Expose
    private int TotalResults;

    @SerializedName("results")
    @Expose
    private List<Movie> Movies;

    @SerializedName("total_pages")
    @Expose
    private int TotalPages;

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getTotalResults() {
        return TotalResults;
    }

    public void setTotalResults(int totalResults) {
        TotalResults = totalResults;
    }

    public List<Movie> getMovies() {
        return Movies;
    }

    public void setMovies(List<Movie> movies) {
        Movies = movies;
    }

    public int getTotalPages() {
        return TotalPages;
    }

    public void setTotalPages(int totalPages) {
        TotalPages = totalPages;
    }
}
