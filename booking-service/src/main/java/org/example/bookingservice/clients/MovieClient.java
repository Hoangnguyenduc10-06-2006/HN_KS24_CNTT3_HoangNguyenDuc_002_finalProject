package org.example.bookingservice.clients;

import org.example.bookingservice.models.dto.responses.MovieResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "movie-service", path = "/api/v1/movies")
public interface MovieClient {

    @GetMapping("/{id}")
    MovieResponse getMovieById(@PathVariable Long id);
}
