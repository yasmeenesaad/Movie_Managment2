package org.example.movie_managment.repository;

import org.example.movie_managment.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContaining(String query);

    Optional<Movie> findByImdbId(String imdbId);
}

