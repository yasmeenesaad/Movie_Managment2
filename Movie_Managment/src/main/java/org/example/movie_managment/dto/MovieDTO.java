package org.example.movie_managment.dto;

import lombok.Data;

@Data
public class MovieDTO {

    private Long id;
    private String title;
    private String year;
    private String genre;
    private String director;
    private String plot;
    private String imdbId;
    private String poster;

}
