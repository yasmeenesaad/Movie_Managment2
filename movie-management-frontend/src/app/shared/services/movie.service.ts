// shared/services/movie.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MovieService {
  private apiUrl = 'http://localhost:8080/api/movies';

  constructor(private http: HttpClient) { }

  searchMovies(query: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/search?query=${query}`);
  }

  getMovieDetails(imdbId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${imdbId}`);
  }

  rateMovie(rating: {imdbId: string, rating: number}): Observable<any> {
    return this.http.post(`${this.apiUrl}/rate`, rating);
  }

  getSavedMovies(): Observable<any> {
    return this.http.get(`${this.apiUrl}/saved`);
  }
}
