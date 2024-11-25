package com.ssuclass.cookietime.network.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TMDBMovieDetailResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("genres")
    private List<Genre> genres;

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("vote_average")
    private float voteAverage;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("budget")
    private int budget;

    @SerializedName("revenue")
    private long revenue;

    @SerializedName("status")
    private String status;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("spoken_languages")
    private List<SpokenLanguage> spokenLanguages;

    @SerializedName("production_companies")
    private List<ProductionCompany> productionCompanies;

    @SerializedName("production_countries")
    private List<ProductionCountry> productionCountries;

    @SerializedName("belongs_to_collection")
    private BelongsToCollection belongsToCollection;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("popularity")
    private double popularity;

    // Getters와 Setters


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

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public BelongsToCollection getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(BelongsToCollection belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    // 내부 클래스: Genre
    public static class Genre {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        // Getters와 Setters

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

    // 내부 클래스: SpokenLanguage
    public static class SpokenLanguage {
        @SerializedName("iso_639_1")
        private String iso6391;

        @SerializedName("name")
        private String name;

        // Getters와 Setters

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    // 내부 클래스: ProductionCompany
    public static class ProductionCompany {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("logo_path")
        private String logoPath;

        @SerializedName("origin_country")
        private String originCountry;

        // Getters와 Setters

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

        public String getLogoPath() {
            return logoPath;
        }

        public void setLogoPath(String logoPath) {
            this.logoPath = logoPath;
        }

        public String getOriginCountry() {
            return originCountry;
        }

        public void setOriginCountry(String originCountry) {
            this.originCountry = originCountry;
        }
    }

    // 내부 클래스: ProductionCountry
    public static class ProductionCountry {
        @SerializedName("iso_3166_1")
        private String iso31661;

        @SerializedName("name")
        private String name;

        // Getters와 Setters

        public String getIso31661() {
            return iso31661;
        }

        public void setIso31661(String iso31661) {
            this.iso31661 = iso31661;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    // 내부 클래스: BelongsToCollection
    public static class BelongsToCollection {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("poster_path")
        private String posterPath;

        @SerializedName("backdrop_path")
        private String backdropPath;

        // Getters와 Setters

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

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }
    }
}
