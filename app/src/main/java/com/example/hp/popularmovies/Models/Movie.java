package com.example.hp.popularmovies.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    @Expose
    private int Id;

    @SerializedName("title")
    @Expose
    private String Name;

    @SerializedName("poster_path")
    @Expose
    private String Image;

    @SerializedName("release_date")
    @Expose
    private String ReleaseDate;

    @SerializedName("vote_average")
    @Expose
    private float Rate;

    @SerializedName("overview")
    @Expose
    private String Plot;

    private String Category;

    public Movie(int id, String name, String image, String releaseDate, float rate, String plot, String category) {
        Id = id;
        Name = name;
        Image = image;
        ReleaseDate = releaseDate;
        Rate = rate;
        Plot = plot;
        Category = category;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public float getRate() {
        return Rate;
    }

    public void setRate(float rate) {
        Rate = rate;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
