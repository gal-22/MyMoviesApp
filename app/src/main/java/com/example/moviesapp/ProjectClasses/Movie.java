package com.example.moviesapp.ProjectClasses;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {
    private double rate;
    private String releaseDate;
    private String name;
    private String description;
    private int Id;
    private List<Integer> genreIds;
    private String posterPath;

    private String backdropPath;

    private int tmdbId;

    // Constructors
    public Movie() {}

    public Movie(double rate, String releaseDate, String name, String description,
                 List<Integer> genreIds, String posterPath, String backdropPath, int tmdbId,int Id) {
        this.rate = rate;
        this.releaseDate = releaseDate;
        this.name = name;
        this.description = description;
        this.Id = Id;
        this.genreIds = genreIds;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.tmdbId = tmdbId;
    }

    // Getters and Setters

    protected Movie(Parcel in) {
        rate = in.readDouble();
        releaseDate = in.readString();
        name = in.readString();
        description = in.readString();
        Id = in.readInt();
        genreIds = new ArrayList<>();
        in.readList(genreIds, Integer.class.getClassLoader());
        posterPath = in.readString();
        backdropPath = in.readString();
        tmdbId = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public double getRate() {
        return rate;
    }

    public void setTMDBId(int tmdbId) {
        this.tmdbId = tmdbId;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "rate=" + rate +
                ", releaseDate='" + releaseDate + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", Id=" + Id +
                ", genreIds=" + genreIds +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", tmdbId=" + tmdbId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(rate);
        dest.writeString(releaseDate);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(Id);
        dest.writeList(genreIds);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeInt(tmdbId);
    }
}