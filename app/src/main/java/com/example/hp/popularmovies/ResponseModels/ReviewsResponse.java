package com.example.hp.popularmovies.ResponseModels;

import com.example.hp.popularmovies.Models.Review;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("page")
    @Expose
    private int page;


    @SerializedName("results")
    @Expose
    List<Review> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
