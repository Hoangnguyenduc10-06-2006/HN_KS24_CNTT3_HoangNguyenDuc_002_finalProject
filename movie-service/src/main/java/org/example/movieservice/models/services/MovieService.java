package org.example.movieservice.models.services;

import org.example.movieservice.models.entities.Movie;

public interface MovieService {
    Movie getMovieById(Long id);
}
