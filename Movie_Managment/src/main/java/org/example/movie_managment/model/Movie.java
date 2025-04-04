package org.example.movie_managment.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "movies")
public class Movie {
    @Id
    private String imdbId;

    @Column(nullable = false)
    private String title;

    private Integer year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private String poster;
    private String imdbRating;
    private String imdbVotes;
    private String type;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRating> ratings;

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Set<UserRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<UserRating> ratings) {
        this.ratings = ratings;
    }
}