package org.example.movie_managment.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private User user;

    private int ratingValue;

    private LocalDateTime timestamp = LocalDateTime.now();

    public Rating(Movie movie, User user, int ratingValue, LocalDateTime timestamp) {
        this.movie = movie;
        this.user = user;
        this.ratingValue = ratingValue;
        this.timestamp = timestamp;
    }
}
