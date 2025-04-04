package org.example.movie_managment.service.omdb;

import org.example.movie_managment.dto.MovieDto;
import org.example.movie_managment.dto.RatingDto;
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
import java.util.stream.Collectors;

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
                    movie.setImdbID(result.get("imdbID"));
                    movie.setTitle(result.get("Title"));
                    movie.setYear((String) parseYear(result.get("Year")));
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
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("apikey", apiKey)
                .queryParam("i", imdbId);

        Map<String, Object> response = restTemplate.getForObject(builder.toUriString(), Map.class);

        if (response == null || !"True".equals(response.get("Response"))) {
            throw new OmdbApiException(response != null ?
                    (String) response.get("Error") : "Unknown error");
        }

        MovieDto movieDto = new MovieDto();

        // Basic Information
        movieDto.setImdbID((String) response.get("imdbID"));
        movieDto.setTitle((String) response.get("Title"));
        movieDto.setYear((String) response.get("Year"));
        movieDto.setRated((String) response.get("Rated"));
        movieDto.setReleased((String) response.get("Released"));
        movieDto.setRuntime((String) response.get("Runtime"));
        movieDto.setGenre((String) response.get("Genre"));
        movieDto.setDirector((String) response.get("Director"));
        movieDto.setWriter((String) response.get("Writer"));
        movieDto.setActors((String) response.get("Actors"));
        movieDto.setPlot((String) response.get("Plot"));
        movieDto.setLanguage((String) response.get("Language"));
        movieDto.setCountry((String) response.get("Country"));
        movieDto.setAwards((String) response.get("Awards"));
        movieDto.setPoster((String) response.get("Poster"));

        // Ratings Information
        movieDto.setImdbRating((String) response.get("imdbRating"));
        movieDto.setImdbVotes((String) response.get("imdbVotes"));

        // External Ratings
        if (response.containsKey("Ratings")) {
            List<Map<String, String>> ratings = (List<Map<String, String>>) response.get("Ratings");
            movieDto.setExternalRatings(ratings.stream()
                    .map(r -> new RatingDto(r.get("Source"), r.get("Value")))
                    .collect(Collectors.toList()));
        }

        // Additional Metadata
        movieDto.setType((String) response.get("Type"));
        movieDto.setDvd((String) response.get("DVD"));
        movieDto.setBoxOffice((String) response.get("BoxOffice"));
        movieDto.setProduction((String) response.get("Production"));
        movieDto.setWebsite((String) response.get("Website"));

        // Handle potential null values for numeric fields
        try {
            if (response.containsKey("Metascore")) {
                movieDto.setMetascore(Integer.parseInt((String) response.get("Metascore")));
            }
        } catch (NumberFormatException e) {
            movieDto.setMetascore(null);
        }

        return movieDto;
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
        movie.setImdbID(response.get("imdbID"));
        movie.setTitle(response.get("Title"));
        movie.setYear((String) parseYear(response.get("Year")));
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

    private Object parseYear(String yearString) {
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