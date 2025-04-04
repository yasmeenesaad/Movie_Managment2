package org.example.movie_managment.mapper;

import org.example.movie_managment.dto.MovieDTO;
import org.example.movie_managment.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    // Convert Movie entity to MovieDTO
    MovieDTO movieToMovieDTO(Movie movie);

    // Convert MovieDTO to Movie entity
    Movie movieDTOToMovie(MovieDTO movieDTO);
}
