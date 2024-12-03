package com.ssuclass.cookietime.presentation.badgemanager;

public class BadgeModel {
    private String title;
    private String year;
    private String month;  // date를 month로 이름 변경하여 명확성 향상

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {  // 파라미터 이름 수정
        this.year = year;  // 올바른 할당
    }

    public String getMonth() {
        return this.month;  // date 대신 month 사용
    }

    public void setMonth(String month) {  // setDate 대신 setMonth로 변경
        this.month = month;
    }
}