package org.example.movieservice.models.repositories;

import org.example.movieservice.models.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
