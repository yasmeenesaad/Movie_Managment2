package org.example.movie_managment.service.impl;
import org.example.movie_managment.dto.MovieDto;
import org.example.movie_managment.dto.RatingDto;
import org.example.movie_managment.exception.ResourceNotFoundException;
import org.example.movie_managment.mapper.MovieMapper;
import org.example.movie_managment.model.Movie;
import org.example.movie_managment.model.User;
import org.example.movie_managment.model.UserRating;
import org.example.movie_managment.repository.MovieRepository;
import org.example.movie_managment.repository.UserRatingRepository;
import org.example.movie_managment.repository.UserRepository;
import org.example.movie_managment.service.MovieService;
import org.example.movie_managment.service.omdb.OmdbService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final OmdbService omdbService;
    private final MovieMapper movieMapper;
    private final UserRepository userRepository;
    private final UserRatingRepository userRatingRepository;

    public MovieServiceImpl(MovieRepository movieRepository,
                            OmdbService omdbService,
                            MovieMapper movieMapper,
                            UserRepository userRepository,
                            UserRatingRepository userRatingRepository) {
        this.movieRepository = movieRepository;
        this.omdbService = omdbService;
        this.movieMapper = movieMapper;
        this.userRepository = userRepository;
        this.userRatingRepository = userRatingRepository;
    }

    @Override
    public Page<MovieDto> searchMovies(String query, Pageable pageable) {
        return omdbService.searchMovies(query, pageable);
    }

    @Override
    public MovieDto getMovieDetails(String imdbId) {
        Movie movie = movieRepository.findById(imdbId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        MovieDto dto = movieMapper.toDto(movie);
        dto.setAverageRating(getAverageRating(imdbId));
        dto.setUserRating(getUserRating(imdbId));
        return dto;
    }

    @Override
    @Transactional
    public void saveMovie(String imdbId) {
        if (!movieRepository.existsById(imdbId)) {
            MovieDto movieDto = omdbService.getMovieDetails(imdbId);
            Movie movie = movieMapper.toEntity(movieDto);
            movieRepository.save(movie);
        }
    }

    @Override
    @Transactional
    public void removeMovie(String imdbId) {
        movieRepository.deleteById(imdbId);
    }

    @Override
    @Transactional
    public void batchSaveMovies(List<String> imdbIds) {
        List<String> existingIds = movieRepository.findByImdbIdIn(imdbIds)
                .stream()
                .map(Movie::getImdbId)
                .collect(Collectors.toList());

        List<String> newIds = imdbIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toList());

        if (!newIds.isEmpty()) {
            List<MovieDto> movieDtos = omdbService.getMovieDetailsBatch(newIds);
            List<Movie> movies = movieDtos.stream()
                    .map(movieMapper::toEntity)
                    .collect(Collectors.toList());
            movieRepository.saveAll(movies);
        }
    }

    @Override
    @Transactional
    public void batchRemoveMovies(List<String> imdbIds) {
        movieRepository.deleteAllById(imdbIds);
    }

    @Override
    @Transactional
    public void rateMovie(RatingDto ratingDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Movie movie = movieRepository.findById(ratingDto.getImdbId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Optional<UserRating> existingRating = userRatingRepository.findByUserAndMovie(user, movie);

        if (existingRating.isPresent()) {
            UserRating rating = existingRating.get();
            if (ratingDto.getRating() == null) {
                userRatingRepository.delete(rating);
            } else {
                rating.setRating(ratingDto.getRating());
                userRatingRepository.save(rating);
            }
        } else if (ratingDto.getRating() != null) {
            UserRating newRating = new UserRating();
            newRating.setUser(user);
            newRating.setMovie(movie);
            newRating.setRating(ratingDto.getRating());
            userRatingRepository.save(newRating);
        }
    }

    @Override
    public Double getAverageRating(String imdbId) {
        return movieRepository.findById(imdbId)
                .map(movie -> movie.getRatings().stream()
                        .mapToInt(UserRating::getRating)
                        .average()
                        .orElse(0.0))
                .orElse(null);
    }

    @Override
    public Integer getUserRating(String imdbId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .flatMap(user -> movieRepository.findById(imdbId)
                        .flatMap(movie -> userRatingRepository.findByUserAndMovie(user, movie)
                                .map(UserRating::getRating)))
                .orElse(null);
    }

    // Update the getSavedMovies method in MovieServiceImpl
    @Override
    public Page<MovieDto> getSavedMovies(String search, Pageable pageable) {
        Page<Movie> movies;
        if (search != null && !search.isEmpty()) {
            movies = movieRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else {
            movies = movieRepository.findAll(pageable);
        }

        // Get current user's ratings in one query
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        List<UserRating> userRatings = user != null ?
                userRatingRepository.findByUser(user) : new ArrayList<>();

        Map<String, Integer> userRatingMap = userRatings.stream()
                .collect(Collectors.toMap(
                        ur -> ur.getMovie().getImdbId(),
                        UserRating::getRating
                ));

        return movies.map(movie -> {
            MovieDto dto = movieMapper.toDto(movie);
            // Set user rating if exists
            dto.setUserRating(userRatingMap.get(movie.getImdbId()));
            return dto;
        });
    }
}