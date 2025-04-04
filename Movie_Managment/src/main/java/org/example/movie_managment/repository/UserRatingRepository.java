package org.example.movie_managment.repository;

import org.example.movie_managment.model.Movie;
import org.example.movie_managment.model.User;
import org.example.movie_managment.model.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    Optional<UserRating> findByUserAndMovie(User user, Movie movie);
    List<UserRating> findByUser(User user);
}