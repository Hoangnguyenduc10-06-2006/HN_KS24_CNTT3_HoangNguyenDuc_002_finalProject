package org.example.movieservice.models.services;

import org.example.movieservice.exceptions.MovieNotFoundException;
import org.example.movieservice.models.entities.Movie;
import org.example.movieservice.models.repositories.MovieRepository;
import org.example.movieservice.models.services.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovieServiceImplTest {

    private MovieRepository movieRepository;
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieRepository = mock(MovieRepository.class);
        movieService = new MovieServiceImpl(movieRepository);
    }

    @Test
    void getMovieByIdReturnsMovieWhenItExists() {
        Movie movie = new Movie(1L, "Laptop", "Laptop văn phòng", new BigDecimal("15000000.00"), 5);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Movie result = movieService.getMovieById(1L);

        assertSame(movie, result);
        verify(movieRepository).findById(1L);
    }

    @Test
    void getMovieByIdThrowsExceptionWhenItDoesNotExist() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.getMovieById(99L)
        );

        assertEquals("Không tìm thấy phim với id: 99", exception.getMessage());
    }

    @Test
    void getMovieByIdUsesMoviesCacheWithIdAsKey() throws NoSuchMethodException {
        Cacheable cacheable = MovieServiceImpl.class
                .getMethod("getMovieById", Long.class)
                .getAnnotation(Cacheable.class);

        assertEquals("movies", cacheable.cacheNames()[0]);
        assertEquals("#id", cacheable.key());
        assertTrue(cacheable.sync());
        assertTrue(Serializable.class.isAssignableFrom(Movie.class));
    }
}
