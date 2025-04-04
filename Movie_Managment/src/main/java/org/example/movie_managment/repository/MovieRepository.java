package org.example.movie_managment.repository;

import org.example.movie_managment.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<Movie> findByImdbIdIn(List<String> imdbIds);

    @Query("SELECT m FROM Movie m WHERE m.imdbId IN :imdbIds")
    List<Movie> findByImdbIds(@Param("imdbIds") List<String> imdbIds);
}

