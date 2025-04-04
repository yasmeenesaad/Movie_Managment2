package org.example.movie_managment.mapper;

import org.example.movie_managment.dto.MovieDto;
import org.example.movie_managment.dto.RatingDto;
import org.example.movie_managment.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "externalRatings", source = "ratings")
    @Mapping(target = "averageRating", source = ".", qualifiedByName = "calculateAverageRating")
    MovieDto toDto(Movie movie);

    @Mapping(target = "ratings", ignore = true)
    Movie toEntity(MovieDto movieDto);

    @Named("mapRatings")
    default List<RatingDto> mapRatings(List<Map<String, String>> ratings) {
        if (ratings == null) return null;
        return ratings.stream()
                .map(rating -> new RatingDto(rating.get("Source"), rating.get("Value")))
                .collect(Collectors.toList());
    }

    @Named("calculateAverageRating")
    default Double calculateAverageRating(Movie movie) {
        if (movie.getRatings() == null || movie.getRatings().isEmpty()) {
            return null;
        }
        return movie.getRatings().stream()
                .mapToInt(ur -> ur.getRating() != null ? ur.getRating() : 0)
                .average()
                .orElse(0.0);
    }
}