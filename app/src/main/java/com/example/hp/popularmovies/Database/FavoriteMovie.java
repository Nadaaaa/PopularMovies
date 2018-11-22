package com.example.hp.popularmovies.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "FavouriteMovies")
public class FavoriteMovie {

    @SerializedName("id")
    @Expose
    @PrimaryKey
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

    public FavoriteMovie(){

    }

    public FavoriteMovie(int id, String name, String image, String releaseDate, float rate, String plot, String category) {
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public float getRate() {
        return Rate;
    }

    public void setRate(float rate) {
        Rate = rate;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
