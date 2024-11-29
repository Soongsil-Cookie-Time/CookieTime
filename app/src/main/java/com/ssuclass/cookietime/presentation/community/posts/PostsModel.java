package com.ssuclass.cookietime.presentation.community.posts;

public class PostsModel {
    private String title;
    private String postId;
    private String content;
    private String nickname;

    public PostsModel(String title, String postId, String content, String nickname) {
        this.nickname = nickname;
        this.content = content;
        this.title = title;
        this.postId = postId;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
