package com.example.moviesapp.ProjectClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {
    private double rate;
    private String releaseDate;
    private String name;
    private String description;
    private int Id;
    private int tmdbId;
    private List<Integer> genreIds;
    private String posterPath;
    private String backdropPath;
    private boolean isFavorite;
    private boolean isRented;
    private boolean isRentedByUser;

    public Movie() {}

    public Movie(double rate, String releaseDate, String name, String description, List<Integer> genreIds,
                 String posterPath, String backdropPath, int tmdbId, int Id,
                 boolean isFavorite, boolean isRented, boolean isRentedByUser) {
        this.rate = rate;
        this.releaseDate = releaseDate;
        this.name = name;
        this.description = description;
        this.genreIds = genreIds;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.tmdbId = tmdbId;
        this.Id = Id;
        this.isFavorite = isFavorite;
        this.isRented = isRented;
        this.isRentedByUser = isRentedByUser;
    }

    protected Movie(Parcel in) {
        rate = in.readDouble();
        releaseDate = in.readString();
        name = in.readString();
        description = in.readString();
        Id = in.readInt();
        tmdbId = in.readInt();
        genreIds = new ArrayList<>();
        in.readList(genreIds, Integer.class.getClassLoader());
        posterPath = in.readString();
        backdropPath = in.readString();
        isFavorite = in.readByte() != 0;
        isRented = in.readByte() != 0;
        isRentedByUser = in.readByte() != 0;
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

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getId() { return Id; }
    public void setId(int id) { Id = id; }

    public int getTmdbId() { return tmdbId; }
    public void setTMDBId(int tmdbId) { this.tmdbId = tmdbId; }

    public List<Integer> getGenreIds() { return genreIds; }
    public void setGenreIds(List<Integer> genreIds) { this.genreIds = genreIds; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public boolean isRented() { return isRented; }
    public void setRented(boolean rented) { isRented = rented; }

    public boolean isRentedByUser() { return isRentedByUser; }
    public void setRentedByUser(boolean rentedByUser) { isRentedByUser = rentedByUser; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(rate);
        dest.writeString(releaseDate);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(Id);
        dest.writeInt(tmdbId);
        dest.writeList(genreIds);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeByte((byte) (isRented ? 1 : 0));
        dest.writeByte((byte) (isRentedByUser ? 1 : 0));
    }
}