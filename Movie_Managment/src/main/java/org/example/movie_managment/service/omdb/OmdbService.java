package org.example.movie_managment.service.omdb;

import org.example.movie_managment.dto.MovieDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OmdbService {
    Page<MovieDto> searchMovies(String query, Pageable pageable);
    MovieDto getMovieDetails(String imdbId);
    List<MovieDto> getMovieDetailsBatch(List<String> imdbIds);
}