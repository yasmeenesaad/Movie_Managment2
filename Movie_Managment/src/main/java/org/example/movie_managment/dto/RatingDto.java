package org.example.movie_managment.dto;

import lombok.Data;

@Data
public class RatingDto {
    private String imdbId;
    private Integer rating;

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
