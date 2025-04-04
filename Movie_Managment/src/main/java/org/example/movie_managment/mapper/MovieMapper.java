package org.example.movie_managment.mapper;

import org.example.movie_managment.dto.MovieDto;
import org.example.movie_managment.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    @Mapping(target = "averageRating", source = ".", qualifiedByName = "calculateAverageRating")
    @Mapping(target = "userRating", ignore = true) // Will be set separately
    MovieDto toDto(Movie movie);

    @Mapping(target = "ratings", ignore = true) // Ignore when mapping from DTO to entity
    Movie toEntity(MovieDto movieDto);

    List<MovieDto> toDtoList(List<Movie> movies);

    @Named("calculateAverageRating")
    default Double calculateAverageRating(Movie movie) {
        if (movie.getRatings() == null || movie.getRatings().isEmpty()) {
            return null;
        }
        return movie.getRatings().stream()
                .mapToInt(userRating -> userRating.getRating() != null ? userRating.getRating() : 0)
                .average()
                .orElse(0.0);
    }
}