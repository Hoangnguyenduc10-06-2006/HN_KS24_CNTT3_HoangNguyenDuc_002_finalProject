package org.example.bookingservice.models.services.impl;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookingservice.clients.MovieClient;
import org.example.bookingservice.exceptions.MovieNotFoundException;
import org.example.bookingservice.exceptions.MovieServiceException;
import org.example.bookingservice.models.dto.responses.MovieResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieGatewayService {

    private final MovieClient movieClient;

    @CircuitBreaker(name = "movieService", fallbackMethod = "getMovieByIdFallback")
    public MovieResponse getMovieById(Long movieId) {
        try {
            log.debug("Calling movie-service to get movie with id: {}", movieId);
            return movieClient.getMovieById(movieId);
        } catch (FeignException.NotFound e) {
            log.warn("Movie not found with id: {}", movieId);
            throw new MovieNotFoundException(movieId);
        } catch (FeignException e) {
            log.error("Error calling movie-service for movie id: {}. Status: {}", movieId, e.status(), e);
            throw new MovieServiceException(
                    "Movie service is unavailable. Please try again later.", e);
        }
    }


    public MovieResponse getMovieByIdFallback(Long movieId, Throwable throwable) {
        if (throwable instanceof MovieNotFoundException ex) {
            throw ex;
        }
        log.error("Circuit breaker OPEN for movie-service. MovieId: {}. Cause: {}",
                movieId, throwable.getMessage());
        throw new MovieServiceException(
                "Movie service is currently unavailable. Circuit breaker is OPEN. Please try again later.",
                throwable);
    }
}

