package com.ssuclass.cookietime.presentation.badgemanager.model;

public class BadgeModel {
    private String title;
    private String year;
    private String month;
    private String movieId;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return this.month;
    }

    public void setMonth(String month) {  // setDate 대신 setMonth로 변경
        this.month = month;
    }

    public String getMovieId() {
        return this.movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}