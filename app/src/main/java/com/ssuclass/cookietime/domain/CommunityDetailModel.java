package com.ssuclass.cookietime.domain;

import java.util.ArrayList;
import java.util.List;

public class CommunityDetailModel {
    private String title;
    private ArrayList<String> comments;

    // Getter와 Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }
}
