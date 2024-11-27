package com.ssuclass.cookietime.domain;

public class CookieKeywordModel {
    private String keyword;
    private Integer count;
    private Boolean isPositive;

    public CookieKeywordModel(String keyword, Integer count, Boolean isPositive) {
        this.keyword = keyword;
        this.count = count;
        this.isPositive = isPositive;
    }

    public CookieKeywordModel() {

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

    public Boolean getPositive() { return isPositive; }

    public void setPositive(Boolean positive) { this.isPositive = positive; }
}
