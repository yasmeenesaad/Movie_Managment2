package org.example.movie_managment.service;

import org.example.movie_managment.dto.MovieDTO;

import java.util.List;

public interface MovieService {


    List<MovieDTO> getAllMovies();

    MovieDTO getMovieById(Long id);

    MovieDTO addMovie(MovieDTO movieDTO);

    void removeMovie(Long id);

    List<MovieDTO> searchMovies(String query);

    List<MovieDTO> getMoviesBySearch(String query);

    void removeMovie_IMD(String imdbId);
}

