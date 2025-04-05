package org.example.movie_managment.controller;


import org.example.movie_managment.dto.MovieDto;
import org.example.movie_managment.service.MovieService;
import org.example.movie_managment.service.impl.MovieServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final MovieServiceImpl movieService;

    public AdminController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String search,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<MovieDto> movies;

        if (search != null && !search.isEmpty()) {
            movies = movieService.searchMovies(search, pageable);
        } else {
            movies = movieService.getSavedMovies(null, pageable);
        }

        model.addAttribute("movies", movies);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", movies.getTotalPages());
        model.addAttribute("search", search);
        return "admin/dashboard";
    }

    @PostMapping("/movies/save")
    public String saveMovie(@RequestParam String imdbId) {
        movieService.saveMovie(imdbId);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/movies/remove")
    public String removeMovie(@RequestParam String imdbId) {
        movieService.removeMovie(imdbId);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/movies/batch-save")
    public String batchSaveMovies(@RequestParam List<String> imdbIds) {
        movieService.batchSaveMovies(imdbIds);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/movies/batch-remove")
    public String batchRemoveMovies(@RequestParam List<String> imdbIds) {
        movieService.batchRemoveMovies(imdbIds);
        return "redirect:/admin/dashboard";
    }
}

