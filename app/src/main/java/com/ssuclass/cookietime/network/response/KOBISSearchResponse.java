package com.ssuclass.cookietime.network.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class KOBISSearchResponse {

    @SerializedName("movieListResult")
    private MovieListResult movieListResult;

    // Getters and Setters
    public MovieListResult getMovieListResult() {
        return movieListResult;
    }

    public void setMovieListResult(MovieListResult movieListResult) {
        this.movieListResult = movieListResult;
    }

    @Override
    public String toString() {
        return "KOBISSearchResponse{" +
                "movieListResult=" + movieListResult +
                '}';
    }

    // Inner class for MovieListResult
    public static class MovieListResult {
        @SerializedName("totCnt")
        private int totCnt;

        @SerializedName("source")
        private String source;

        @SerializedName("movieList")
        private List<Movie> movieList;

        // Getters and Setters
        public int getTotCnt() {
            return totCnt;
        }

        public void setTotCnt(int totCnt) {
            this.totCnt = totCnt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public List<Movie> getMovieList() {
            return movieList;
        }

        public void setMovieList(List<Movie> movieList) {
            this.movieList = movieList;
        }

        @Override
        public String toString() {
            return "MovieListResult{" +
                    "totCnt=" + totCnt +
                    ", source='" + source + '\'' +
                    ", movieList=" + movieList +
                    '}';
        }
    }

    // Inner class for individual movie details
    public static class Movie {
        @SerializedName("movieCd")
        private String movieCd;

        @SerializedName("movieNm")
        private String movieNm;

        @SerializedName("movieNmEn")
        private String movieNmEn;

        @SerializedName("prdtYear")
        private String prdtYear;

        @SerializedName("openDt")
        private String openDt;

        @SerializedName("typeNm")
        private String typeNm;

        @SerializedName("prdtStatNm")
        private String prdtStatNm;

        @SerializedName("nationAlt")
        private String nationAlt;

        @SerializedName("genreAlt")
        private String genreAlt;

        @SerializedName("repNationNm")
        private String repNationNm;

        @SerializedName("repGenreNm")
        private String repGenreNm;

        @SerializedName("directors")
        private List<Director> directors;

        @SerializedName("companys")
        private List<Company> companys;

        // Getters and Setters for all fields
        public String getMovieCd() {
            return movieCd;
        }

        public void setMovieCd(String movieCd) {
            this.movieCd = movieCd;
        }

        public String getMovieNm() {
            return movieNm;
        }

        public void setMovieNm(String movieNm) {
            this.movieNm = movieNm;
        }

        public String getMovieNmEn() {
            return movieNmEn;
        }

        public void setMovieNmEn(String movieNmEn) {
            this.movieNmEn = movieNmEn;
        }

        public String getPrdtYear() {
            return prdtYear;
        }

        public void setPrdtYear(String prdtYear) {
            this.prdtYear = prdtYear;
        }

        public String getOpenDt() {
            return openDt;
        }

        public void setOpenDt(String openDt) {
            this.openDt = openDt;
        }

        public String getTypeNm() {
            return typeNm;
        }

        public void setTypeNm(String typeNm) {
            this.typeNm = typeNm;
        }

        public String getPrdtStatNm() {
            return prdtStatNm;
        }

        public void setPrdtStatNm(String prdtStatNm) {
            this.prdtStatNm = prdtStatNm;
        }

        public String getNationAlt() {
            return nationAlt;
        }

        public void setNationAlt(String nationAlt) {
            this.nationAlt = nationAlt;
        }

        public String getGenreAlt() {
            return genreAlt;
        }

        public void setGenreAlt(String genreAlt) {
            this.genreAlt = genreAlt;
        }

        public String getRepNationNm() {
            return repNationNm;
        }

        public void setRepNationNm(String repNationNm) {
            this.repNationNm = repNationNm;
        }

        public String getRepGenreNm() {
            return repGenreNm;
        }

        public void setRepGenreNm(String repGenreNm) {
            this.repGenreNm = repGenreNm;
        }

        public List<Director> getDirectors() {
            return directors;
        }

        public void setDirectors(List<Director> directors) {
            this.directors = directors;
        }

        public List<Company> getCompanys() {
            return companys;
        }

        public void setCompanys(List<Company> companys) {
            this.companys = companys;
        }

        @Override
        public String toString() {
            return "Movie{" +
                    "movieCd='" + movieCd + '\'' +
                    ", movieNm='" + movieNm + '\'' +
                    ", movieNmEn='" + movieNmEn + '\'' +
                    ", prdtYear='" + prdtYear + '\'' +
                    ", openDt='" + openDt + '\'' +
                    ", typeNm='" + typeNm + '\'' +
                    ", prdtStatNm='" + prdtStatNm + '\'' +
                    ", nationAlt='" + nationAlt + '\'' +
                    ", genreAlt='" + genreAlt + '\'' +
                    ", repNationNm='" + repNationNm + '\'' +
                    ", repGenreNm='" + repGenreNm + '\'' +
                    ", directors=" + directors +
                    ", companys=" + companys +
                    '}';
        }
    }

    // Inner class for director details
    public static class Director {
        @SerializedName("peopleNm")
        private String peopleNm;

        // Getters and Setters
        public String getPeopleNm() {
            return peopleNm;
        }

        public void setPeopleNm(String peopleNm) {
            this.peopleNm = peopleNm;
        }

        @Override
        public String toString() {
            return "Director{" +
                    "peopleNm='" + peopleNm + '\'' +
                    '}';
        }
    }

    // Inner class for company details
    public static class Company {
        @SerializedName("companyCd")
        private String companyCd;

        @SerializedName("companyNm")
        private String companyNm;

        // Getters and Setters
        public String getCompanyCd() {
            return companyCd;
        }

        public void setCompanyCd(String companyCd) {
            this.companyCd = companyCd;
        }

        public String getCompanyNm() {
            return companyNm;
        }

        public void setCompanyNm(String companyNm) {
            this.companyNm = companyNm;
        }

        @Override
        public String toString() {
            return "Company{" +
                    "companyCd='" + companyCd + '\'' +
                    ", companyNm='" + companyNm + '\'' +
                    '}';
        }
    }
}
