package org.example.movie_managment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.movie_managment.dto.MovieDTO;
import org.example.movie_managment.dto.MovieResponse;
import org.example.movie_managment.exception.ResourceNotFoundException;
import org.example.movie_managment.mapper.MovieMapper;
import org.example.movie_managment.model.Movie;
import org.example.movie_managment.repository.MovieRepository;
import org.example.movie_managment.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {
    @Value("${omdb.api.key}")
    private String apiKey;

    private RestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieMapper movieMapper;

    public MovieServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::movieToMovieDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id)); // Custom exception
        return movieMapper.movieToMovieDTO(movie);
    }

    @Override
    public MovieDTO addMovie(MovieDTO movieDTO) {
        Movie movie = movieMapper.movieDTOToMovie(movieDTO);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.movieToMovieDTO(savedMovie);
    }

    @Override
    public void removeMovie(Long id) {
        movieRepository.deleteById(id);
    }

//    @Override
//    public List<MovieDTO> searchMovies(String query) {
//        // Implement OMDB API integration here
//        // This is just a mock for search functionality
//        return movieRepository.findByTitleContaining(query).stream()
//                .map(movieMapper::movieToMovieDTO)
//                .collect(Collectors.toList());
//    }

    public List<MovieDTO> getMoviesBySearch(String query) {
        String url = UriComponentsBuilder.fromHttpUrl("http://www.omdbapi.com/")
                .queryParam("i","tt3896198")
                .queryParam("apikey", apiKey)
                .queryParam("s", query)
                .toUriString();

        // Fetch the data from the OMDB API
        MovieResponse movieResponse = restTemplate.getForObject(url, MovieResponse.class);

        // Return the list of movies from the response
        if (movieResponse != null && movieResponse.getSearch() != null) {
            return Arrays.asList(movieResponse.getSearch());
        }
        return null;
    }

    @Override
    public void removeMovie_IMD(String imdbId) {
        Movie movie = movieRepository.findByImdbId(imdbId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        movieRepository.delete(movie);
    }

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    private static final String OMDB_API_URL = "http://www.omdbapi.com/";
//    http://www.omdbapi.com/?i=tt3896198&apikey=1d56bb56
    @Override
    public List<MovieDTO> searchMovies(String query) {
        String url = OMDB_API_URL +"?i=tt3896198"+ "?&apikey=" + omdbApiKey + "&s=" + query;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String responseBody = response.getBody();

        // Parse the JSON response and convert it to a list of MovieDTO objects
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<MovieDTO> movieList = new ArrayList<>();
        if (jsonNode.has("Search")) {
            for (JsonNode movieNode : jsonNode.get("Search")) {
                MovieDTO movie = new MovieDTO();
                movie.setTitle(movieNode.get("Title").asText());
                movie.setYear(movieNode.get("Year").asText());
                movie.setGenre(movieNode.get("Genre").asText());
                movie.setDirector(movieNode.get("Director").asText());
                movie.setPoster(movieNode.get("Poster").asText());
                movieList.add(movie);

            }
        }

        return movieList;
    }
}

