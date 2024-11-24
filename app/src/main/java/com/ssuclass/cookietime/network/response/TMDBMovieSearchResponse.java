package com.ssuclass.cookietime.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMDBMovieSearchResponse {
    @SerializedName("page")
    private int page; // 현재 페이지 번호

    @SerializedName("total_results")
    private int totalResults; // 검색 결과의 총 개수

    @SerializedName("total_pages")
    private int totalPages; // 결과의 총 페이지 수

    @SerializedName("results")
    private List<Movie> results; // 검색된 영화 목록

    // Getter와 Setter
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    // 내부 클래스 Movie
    public static class Movie {
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

        @SerializedName("vote_average")
        private float voteAverage; // 평점

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

        public float getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(float voteAverage) {
            this.voteAverage = voteAverage;
        }
    }
}
