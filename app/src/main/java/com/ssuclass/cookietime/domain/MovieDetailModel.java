package com.ssuclass.cookietime.domain;

import java.util.ArrayList;

public class MovieDetailModel {
    private String title;
    private String poster_path;
    private ArrayList<Post> community;

    private static class Post {
        private String title;
        private String timeStamp;
        private ArrayList<Comment> comments;

        private static class Comment {
            private String content;
            private String timeStamp;
        }
    }

    public MovieDetailModel(String title, String poster_path) {
        this.title = title;
        this.poster_path = poster_path;
    }

    private SurveyProgressModel surveyProgressModel;
    private ArrayList<CookieKeywordModel> cookieKeywordCountArray;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public ArrayList<Post> getCommunity() {
        return community;
    }

    public void setCommunity(ArrayList<Post> community) {
        this.community = community;
    }

    public SurveyProgressModel getSurveyProgressModel() {
        return surveyProgressModel;
    }

    public void setSurveyProgressModel(SurveyProgressModel surveyProgressModel) {
        this.surveyProgressModel = surveyProgressModel;
    }

    public ArrayList<CookieKeywordModel> getCookieKeywordCountArray() {
        return cookieKeywordCountArray;
    }

    public void setCookieKeywordCountArray(ArrayList<CookieKeywordModel> cookieKeywordCountArray) {
        this.cookieKeywordCountArray = cookieKeywordCountArray;
    }
}
