package com.ssuclass.cookietime.domain;

import java.util.ArrayList;

public class MovieDetailModel {
    private String title;
    private String poster_path;
    private ArrayList<Post> community;

    private SurveyProgressModel surveyProgressModel;
    private ArrayList<CookieKeywordModel> cookieKeywordCountArray;

    // 기본 생성자 (Firestore에서 필요)
    public MovieDetailModel() {
        // Firestore가 기본 생성자를 사용하여 객체를 초기화
    }

    // 필요한 생성자 추가
    public MovieDetailModel(String title, String poster_path) {
        this.title = title;
        this.poster_path = poster_path;
    }

    // 게터와 세터
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

    // 내부 클래스도 Firestore 직렬화를 위해 기본 생성자가 필요
    public static class Post {
        private String title;
        private String timeStamp;
        private ArrayList<Comment> comments;

        public Post() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public ArrayList<Comment> getComments() {
            return comments;
        }

        public void setComments(ArrayList<Comment> comments) {
            this.comments = comments;
        }

        public static class Comment {
            private String content;
            private String timeStamp;

            public Comment() {
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }
        }
    }
}
