package com.ssuclass.cookietime.presentation.community.entry;

public class CommunitiesModel {
    private String id;
    private String title;
    private String moviePosterUrl;

    public CommunitiesModel() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterPath(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }
}
