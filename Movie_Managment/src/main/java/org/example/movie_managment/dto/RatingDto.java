package org.example.movie_managment.dto;

import lombok.Data;

@Data
public class RatingDto {
    private String source;
    private String value;
    private String ImdbId;
    private Integer Rating;

    public Integer getRating() {
        return Rating;
    }

    public void setRating(Integer ratings) {
        Rating = ratings;
    }

    public RatingDto(String source, String value) {
    }

    public String getImdbId() {
        return ImdbId;
    }

    public void setImdbId(String imdbId) {
        ImdbId = imdbId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}