// user/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { MovieService } from '../../shared/services/movie.service';
import { MatDialog } from '@angular/material/dialog';
import { MovieDetailsComponent } from '../movie-details/movie-details.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  movies: any[] = [];
  searchQuery: string = '';

  constructor(
    private movieService: MovieService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.loadMovies();
  }

  loadMovies() {
    this.movieService.getSavedMovies().subscribe(movies => {
      this.movies = movies;
    });
  }

  searchMovies() {
    if (this.searchQuery) {
      this.movieService.searchMovies(this.searchQuery).subscribe(movies => {
        this.movies = movies;
      });
    }
  }

  openMovieDetails(movie: any) {
    this.dialog.open(MovieDetailsComponent, {
      width: '800px',
      data: { movie }
    });
  }
}
