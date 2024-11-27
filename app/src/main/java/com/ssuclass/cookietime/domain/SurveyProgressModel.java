package com.ssuclass.cookietime.domain;

public class SurveyProgressModel {
    private Integer cookieZeroCount = 0;
    private Integer cookieOneCount = 0;
    private Integer cookieTwoCount = 0;
    private Integer cookieThreeCount = 0;

    private Integer cookieLongCount = 0;
    private Integer cookieShortCount = 0;

    private Integer cookieImportantCount = 0;
    private Integer cookieNotImportantCount = 0;

    // 게터와 세터
    public Integer getCookieZeroCount() {
        return cookieZeroCount;
    }

    public void setCookieZeroCount(Integer cookieZeroCount) {
        this.cookieZeroCount = cookieZeroCount;
    }

    public Integer getCookieOneCount() {
        return cookieOneCount;
    }

    public void setCookieOneCount(Integer cookieOneCount) {
        this.cookieOneCount = cookieOneCount;
    }

    public Integer getCookieTwoCount() {
        return cookieTwoCount;
    }

    public void setCookieTwoCount(Integer cookieTwoCount) {
        this.cookieTwoCount = cookieTwoCount;
    }

    public Integer getCookieThreeCount() {
        return cookieThreeCount;
    }

    public void setCookieThreeCount(Integer cookieThreeCount) {
        this.cookieThreeCount = cookieThreeCount;
    }

    public Integer getCookieLongCount() {
        return cookieLongCount;
    }

    public void setCookieLongCount(Integer cookieLongCount) {
        this.cookieLongCount = cookieLongCount;
    }

    public Integer getCookieShortCount() {
        return cookieShortCount;
    }

    public void setCookieShortCount(Integer cookieShortCount) {
        this.cookieShortCount = cookieShortCount;
    }

    public Integer getCookieImportantCount() {
        return cookieImportantCount;
    }

    public void setCookieImportantCount(Integer cookieImportantCount) {
        this.cookieImportantCount = cookieImportantCount;
    }

    public Integer getCookieNotImportantCount() {
        return cookieNotImportantCount;
    }

    public void setCookieNotImportantCount(Integer cookieNotImportantCount) {
        this.cookieNotImportantCount = cookieNotImportantCount;
    }

    public SurveyProgressModel(Integer cookieZeroCount, Integer cookieOneCount, Integer cookieTwoCount, Integer cookieThreeCount, Integer cookieLongCount, Integer cookieShortCount, Integer cookieImportantCount, Integer cookieNotImportantCount) {
        this.cookieZeroCount = cookieZeroCount;
        this.cookieOneCount = cookieOneCount;
        this.cookieTwoCount = cookieTwoCount;
        this.cookieThreeCount = cookieThreeCount;
        this.cookieLongCount = cookieLongCount;
        this.cookieShortCount = cookieShortCount;
        this.cookieImportantCount = cookieImportantCount;
        this.cookieNotImportantCount = cookieNotImportantCount;
    }

    public SurveyProgressModel() {
    }
}
