package com.example.moviesapp.ProjectClasses;

public enum MovieGenre {
    ACTION(28),
    ADVENTURE(12),
    ANIMATION(16),
    COMEDY(35),
    CRIME(80),
    DOCUMENTARY(99),
    DRAMA(18),
    FAMILY(10751);

    private final int id;

    MovieGenre(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}