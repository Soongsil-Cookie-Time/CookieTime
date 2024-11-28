package com.ssuclass.cookietime.domain.community;

public class CommunityModel {
    private String id;
    private String title;
    private String moviePosterUrl;

    public CommunityModel() {
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
