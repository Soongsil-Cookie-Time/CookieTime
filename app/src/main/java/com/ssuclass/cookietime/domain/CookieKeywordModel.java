package com.ssuclass.cookietime.domain;

public class CookieKeywordModel {
    private String keyword;
    private Integer count;

    public CookieKeywordModel(String keyword, Integer count) {
        this.keyword = keyword;
        this.count = count;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
