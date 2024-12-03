package com.ssuclass.cookietime.presentation.badgemanager.model;

import java.util.ArrayList;
import java.util.List;

public class MonthGroup {
    private final String year;
    private final String month;
    private final List<BadgeModel> movies;

    public MonthGroup(String year, String month) {
        this.year = year;
        this.month = month;
        this.movies = new ArrayList<>();
    }

    public void addMovie(BadgeModel movie) {
        movies.add(movie);
    }

    // getters
    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public List<BadgeModel> getMovies() {
        return movies;
    }
}
