// admin/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { MovieService } from '../../shared/services/movie.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  movies: any[] = [];
  searchQuery: string = '';
  batchImdbIds: string = '';

  constructor(private movieService: MovieService) {}

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

  saveMovie(imdbId: string) {
    this.movieService.saveMovie(imdbId).subscribe(() => {
      this.loadMovies();
    });
  }

  removeMovie(imdbId: string) {
    this.movieService.removeMovie(imdbId).subscribe(() => {
      this.loadMovies();
    });
  }

  batchSaveMovies() {
    const imdbIds = this.batchImdbIds.split(',').map(id => id.trim());
    this.movieService.batchSaveMovies(imdbIds).subscribe(() => {
      this.loadMovies();
      this.batchImdbIds = '';
    });
  }
}
