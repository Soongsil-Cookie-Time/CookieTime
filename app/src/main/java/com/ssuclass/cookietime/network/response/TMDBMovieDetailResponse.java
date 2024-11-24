package com.ssuclass.cookietime.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMDBMovieDetailResponse {
    @SerializedName("id")
    private int id; // 영화 ID

    @SerializedName("title")
    private String title; // 영화 제목

    @SerializedName("overview")
    private String overview; // 영화 설명

    @SerializedName("release_date")
    private String releaseDate; // 출시일 (YYYY-MM-DD)

    @SerializedName("poster_path")
    private String posterPath; // 포스터 이미지 경로

    @SerializedName("genres")
    private List<Genre> genres; // 영화 장르

    @SerializedName("runtime")
    private int runtime; // 영화 상영 시간 (분)

    @SerializedName("vote_average")
    private float voteAverage; // 평점

    @SerializedName("backdrop_path")
    private String backdropPath; // 배경 이미지 경로

    // Getter와 Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    // 내부 클래스 Genre
    public static class Genre {
        @SerializedName("id")
        private int id; // 장르 ID

        @SerializedName("name")
        private String name; // 장르 이름

        // Getter와 Setter
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
