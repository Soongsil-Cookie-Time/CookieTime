package com.ssuclass.cookietime.network.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class KMDbResponse {

    @SerializedName("Result")
    private Result result;

    @SerializedName("Data")
    private List<Data> data;

    // Getters and Setters
    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Result {
        @SerializedName("Collection")
        private String collection;

        @SerializedName("PageNo")
        private int pageNo;

        @SerializedName("NumOfRows")
        private int numOfRows;

        @SerializedName("TotalCount")
        private int totalCount;

        // Getters and Setters
        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(int numOfRows) {
            this.numOfRows = numOfRows;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    public static class Data {
        @SerializedName("Row")
        private List<Movie> row;

        // Getters and Setters
        public List<Movie> getRow() {
            return row;
        }

        public void setRow(List<Movie> row) {
            this.row = row;
        }
    }

    public static class Movie {
        @SerializedName("docid")
        private String docId;

        @SerializedName("movieId")
        private String movieId;

        @SerializedName("movieSeq")
        private String movieSeq;

        @SerializedName("title")
        private String title;

        @SerializedName("titleEng")
        private String titleEng;

        @SerializedName("titleOrg")
        private String titleOrg;

        @SerializedName("titleEtc")
        private String titleEtc;

        @SerializedName("directorNm")
        private String directorNm;

        @SerializedName("directorEnNm")
        private String directorEnNm;

        @SerializedName("directorId")
        private String directorId;

        @SerializedName("actorNm")
        private String actorNm;

        @SerializedName("actorEnNm")
        private String actorEnNm;

        @SerializedName("actorId")
        private String actorId;

        @SerializedName("nation")
        private String nation;

        @SerializedName("company")
        private String company;

        @SerializedName("prodYear")
        private String prodYear;

        @SerializedName("plot")
        private String plot;

        @SerializedName("runtime")
        private String runtime;

        @SerializedName("rating")
        private String rating;

        @SerializedName("genre")
        private String genre;

        @SerializedName("kmdbUrl")
        private String kmdbUrl;

        @SerializedName("type")
        private String type;

        @SerializedName("use")
        private String use;

        @SerializedName("posterUrl")
        private String posterUrl;

        @SerializedName("stillUrl")
        private String stillUrl;

        // Getters and Setters for all fields
        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public String getMovieId() {
            return movieId;
        }

        public void setMovieId(String movieId) {
            this.movieId = movieId;
        }

        public String getMovieSeq() {
            return movieSeq;
        }

        public void setMovieSeq(String movieSeq) {
            this.movieSeq = movieSeq;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleEng() {
            return titleEng;
        }

        public void setTitleEng(String titleEng) {
            this.titleEng = titleEng;
        }

        public String getTitleOrg() {
            return titleOrg;
        }

        public void setTitleOrg(String titleOrg) {
            this.titleOrg = titleOrg;
        }

        public String getTitleEtc() {
            return titleEtc;
        }

        public void setTitleEtc(String titleEtc) {
            this.titleEtc = titleEtc;
        }

        public String getDirectorNm() {
            return directorNm;
        }

        public void setDirectorNm(String directorNm) {
            this.directorNm = directorNm;
        }

        public String getDirectorEnNm() {
            return directorEnNm;
        }

        public void setDirectorEnNm(String directorEnNm) {
            this.directorEnNm = directorEnNm;
        }

        public String getDirectorId() {
            return directorId;
        }

        public void setDirectorId(String directorId) {
            this.directorId = directorId;
        }

        public String getActorNm() {
            return actorNm;
        }

        public void setActorNm(String actorNm) {
            this.actorNm = actorNm;
        }

        public String getActorEnNm() {
            return actorEnNm;
        }

        public void setActorEnNm(String actorEnNm) {
            this.actorEnNm = actorEnNm;
        }

        public String getActorId() {
            return actorId;
        }

        public void setActorId(String actorId) {
            this.actorId = actorId;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getProdYear() {
            return prodYear;
        }

        public void setProdYear(String prodYear) {
            this.prodYear = prodYear;
        }

        public String getPlot() {
            return plot;
        }

        public void setPlot(String plot) {
            this.plot = plot;
        }

        public String getRuntime() {
            return runtime;
        }

        public void setRuntime(String runtime) {
            this.runtime = runtime;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getKmdbUrl() {
            return kmdbUrl;
        }

        public void setKmdbUrl(String kmdbUrl) {
            this.kmdbUrl = kmdbUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUse() {
            return use;
        }

        public void setUse(String use) {
            this.use = use;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        public String getStillUrl() {
            return stillUrl;
        }

        public void setStillUrl(String stillUrl) {
            this.stillUrl = stillUrl;
        }
    }
}
