package com.example.moviesapp.ProjectClasses;

import java.util.List;

public class Movie {
    private double rate;
    private String releaseDate;
    private String name;
    private String description;
    private List<Integer> genreIds;
    private String posterPath;

    // Constructors
    public Movie() {}

    public Movie(double rate, String releaseDate, String name, String description,
                 List<Integer> genreIds, String posterPath) {
        this.rate = rate;
        this.releaseDate = releaseDate;
        this.name = name;
        this.description = description;
        this.genreIds = genreIds;
        this.posterPath = posterPath;
    }

    // Getters and Setters

    public double getRate() {
        return rate;
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

    @Override
    public String toString() {
        return "Movie{" +
                "rate=" + rate +
                ", releaseDate='" + releaseDate + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", genreIds=" + genreIds +
                ", posterPath='" + posterPath + '\'' +
                '}';
        }
    }