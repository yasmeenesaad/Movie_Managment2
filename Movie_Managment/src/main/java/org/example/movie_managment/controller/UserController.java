package org.example.movie_managment.controller;
import org.example.movie_managment.dto.MovieDto;
import org.example.movie_managment.dto.RatingDto;
import org.example.movie_managment.service.MovieService;
import org.example.movie_managment.service.impl.MovieServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final MovieServiceImpl movieService;

    public UserController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String search,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<MovieDto> movies = movieService.getSavedMovies(search, pageable);

        model.addAttribute("movies", movies);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", movies.getTotalPages());
        model.addAttribute("search", search);
        return "user/dashboard";
    }

    @GetMapping("/movie/{imdbId}")
    public String movieDetails(@PathVariable String imdbId, Model model) {
        MovieDto movie = movieService.getMovieDetails(imdbId);
        model.addAttribute("movie", movie);
        return "user/movie-details";
    }

    @PostMapping("/rate")
    public String rateMovie(@ModelAttribute RatingDto ratingDto) {
        movieService.rateMovie(ratingDto);
        return "redirect:/user/movie/" + ratingDto.getImdbId();
    }
}