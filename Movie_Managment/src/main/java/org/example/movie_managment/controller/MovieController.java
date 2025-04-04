package org.example.movie_managment.controller;

import org.example.movie_managment.dto.MovieDTO;
import org.example.movie_managment.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Get all movies
    @GetMapping("/all")
    public List<MovieDTO> getAllMovies() {
        return movieService.getAllMovies();
    }

    // Get a movie by ID
    @GetMapping("/{id}")
    public MovieDTO getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    // Add a new movie
    @PostMapping("/add")
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movie) {
        MovieDTO createdMovie = movieService.addMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdMovie);
    }

    // Remove a movie
    @DeleteMapping("/remove/{id}")
    public void removeMovie(@PathVariable Long id) {
        movieService.removeMovie(id);
    }

    // Search movies
    @GetMapping("/search")
    public List<MovieDTO> searchMovies(@RequestParam String query) {
        return movieService.searchMovies(query);
    }

    @GetMapping("/movies/search")
    public List<MovieDTO> searchMovies2(@RequestParam("query") String query) {
        return movieService.getMoviesBySearch(query);
    }


    // Add Movie
    @PostMapping
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movieDTO) {
        MovieDTO addedMovie = movieService.addMovie(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedMovie);
    }

    // Remove Movie by IMDb ID
    @DeleteMapping("/{imdbId}")
    public ResponseEntity<Void> removeMovie(@PathVariable String imdbId) {
        movieService.removeMovie_IMD(imdbId);
        return ResponseEntity.noContent().build();
    }
}

