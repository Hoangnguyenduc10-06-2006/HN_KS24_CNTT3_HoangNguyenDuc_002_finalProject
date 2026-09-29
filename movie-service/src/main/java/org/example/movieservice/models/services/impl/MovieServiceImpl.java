package org.example.movieservice.models.services.impl;

import org.example.movieservice.exceptions.MovieNotFoundException;
import org.example.movieservice.models.entities.Movie;
import org.example.movieservice.models.repositories.MovieRepository;
import org.example.movieservice.models.services.MovieService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    @Cacheable(cacheNames = "movies", key = "#id")
    @Transactional(readOnly = true)
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }
}
