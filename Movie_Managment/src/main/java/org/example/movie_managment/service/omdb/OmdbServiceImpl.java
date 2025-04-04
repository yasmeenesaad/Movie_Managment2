package org.example.movie_managment.service.omdb;

import org.example.movie_managment.dto.MovieDto;
import org.example.movie_managment.exception.OmdbApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OmdbServiceImpl implements OmdbService {

    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.url}")
    private String apiUrl;

    @Value("${omdb.api.i}")
    private String apiI;

    private final RestTemplate restTemplate;

    public OmdbServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Page<MovieDto> searchMovies(String query, Pageable pageable) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("i"+apiI)
                    .queryParam("apikey", apiKey)
                    .queryParam("s", query)
                    .queryParam("page", pageable.getPageNumber() + 1);

            Map<String, Object> response = restTemplate.getForObject(builder.toUriString(), Map.class);

            if (response == null) {
                throw new OmdbApiException("No response from OMDB API");
            }

            if ("False".equals(response.get("Response"))) {
                String errorMessage = response.containsKey("Error") ?
                        (String) response.get("Error") : "Unknown error from OMDB API";
                throw new OmdbApiException(errorMessage);
            }

            List<Map<String, String>> searchResults = (List<Map<String, String>>) response.get("Search");
            if (searchResults == null) {
                return new PageImpl<>(new ArrayList<>(), pageable, 0);
            }

            List<MovieDto> movies = new ArrayList<>();
            for (Map<String, String> result : searchResults) {
                try {
                    MovieDto movie = new MovieDto();
                    movie.setImdbId(result.get("imdbID"));
                    movie.setTitle(result.get("Title"));
                    movie.setYear(parseYear(result.get("Year")));
                    movie.setType(result.get("Type"));
                    movie.setPoster(result.get("Poster"));
                    movies.add(movie);
                } catch (Exception e) {
                    // Skip problematic entries but log them
                    System.err.println("Error processing movie result: " + e.getMessage());
                }
            }

            int totalResults = response.containsKey("totalResults") ?
                    Integer.parseInt(response.get("totalResults").toString()) : 0;

            return new PageImpl<>(movies, pageable, totalResults);
        } catch (Exception e) {
            throw new OmdbApiException("Failed to search movies: " + e.getMessage(), e);
        }
    }

    @Override
    public MovieDto getMovieDetails(String imdbId) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("apikey", apiKey)
                    .queryParam("i", imdbId);

            Map<String, String> response = restTemplate.getForObject(builder.toUriString(), Map.class);

            if (response == null) {
                throw new OmdbApiException("No response from OMDB API");
            }

            if ("False".equals(response.get("Response"))) {
                String errorMessage = response.containsKey("Error") ?
                        response.get("Error") : "Unknown error from OMDB API";
                throw new OmdbApiException(errorMessage);
            }

            return mapResponseToMovieDto(response);
        } catch (Exception e) {
            throw new OmdbApiException("Failed to get movie details: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MovieDto> getMovieDetailsBatch(List<String> imdbIds) {
        List<MovieDto> movies = new ArrayList<>();
        for (String imdbId : imdbIds) {
            try {
                movies.add(getMovieDetails(imdbId));
            } catch (OmdbApiException e) {
                // Log error and continue with next movie
                System.err.println("Error fetching details for movie " + imdbId + ": " + e.getMessage());
            }
        }
        return movies;
    }

    private MovieDto mapResponseToMovieDto(Map<String, String> response) {
        MovieDto movie = new MovieDto();
        movie.setImdbId(response.get("imdbID"));
        movie.setTitle(response.get("Title"));
        movie.setYear(parseYear(response.get("Year")));
        movie.setRated(response.get("Rated"));
        movie.setReleased(response.get("Released"));
        movie.setRuntime(response.get("Runtime"));
        movie.setGenre(response.get("Genre"));
        movie.setDirector(response.get("Director"));
        movie.setWriter(response.get("Writer"));
        movie.setActors(response.get("Actors"));
        movie.setPlot(response.get("Plot"));
        movie.setLanguage(response.get("Language"));
        movie.setCountry(response.get("Country"));
        movie.setAwards(response.get("Awards"));
        movie.setPoster(response.get("Poster"));
        movie.setImdbRating(response.get("imdbRating"));
        movie.setImdbVotes(response.get("imdbVotes"));
        movie.setType(response.get("Type"));
        return movie;
    }

    private Integer parseYear(String yearString) {
        if (yearString == null || yearString.isEmpty()) {
            return null;
        }
        try {
            // Extract numbers from strings like "2001", "1999–2000", "2010–"
            String numericPart = yearString.replaceAll("[^0-9]", "");
            if (!numericPart.isEmpty()) {
                return Integer.parseInt(numericPart.substring(0, Math.min(4, numericPart.length())));
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}