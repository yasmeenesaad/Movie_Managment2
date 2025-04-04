package org.example.movie_managment.service;

import org.example.movie_managment.dto.MovieDto;
import org.example.movie_managment.dto.RatingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface MovieService {
    Page<MovieDto> searchMovies(String query, Pageable pageable);
    Page<MovieDto> getSavedMovies(String search, Pageable pageable);
    MovieDto getMovieDetails(String imdbId);
    void saveMovie(String imdbId);
    void removeMovie(String imdbId);
    void batchSaveMovies(List<String> imdbIds);
    void batchRemoveMovies(List<String> imdbIds);
    void rateMovie(RatingDto ratingDto);
    Double getAverageRating(String imdbId);
    Integer getUserRating(String imdbId);
}

