// user/movie-details/movie-details.component.ts
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MovieService } from '../../shared/services/movie.service';

@Component({
  selector: 'app-movie-details',
  templateUrl: './movie-details.component.html',
  styleUrls: ['./movie-details.component.scss']
})
export class MovieDetailsComponent implements OnInit {
  movie: any;
  userRating: number = 0;
  averageRating: number = 0;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private movieService: MovieService
  ) {
    this.movie = data.movie;
  }

  ngOnInit() {
    if (this.movie.averageRating) {
      this.averageRating = this.movie.averageRating;
    }
    if (this.movie.userRating) {
      this.userRating = this.movie.userRating;
    }
  }

  rateMovie(rating: number) {
    this.movieService.rateMovie({
      imdbId: this.movie.imdbID,
      rating: rating
    }).subscribe(() => {
      this.userRating = rating;
      // You might want to refresh the average rating here
    });
  }
}
