package org.example.movie_managment.dto;

// DTO for OMDB response
public class MovieResponse {
    private MovieDTO[] Search;

    public MovieDTO[] getSearch() {
        return Search;
    }

    public void setSearch(MovieDTO[] search) {
        this.Search = search;
    }
}
