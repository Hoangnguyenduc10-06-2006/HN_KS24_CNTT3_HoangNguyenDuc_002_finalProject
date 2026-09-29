package org.example.movieservice.exceptions;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Long id) {
        super("Không tìm thấy phim với id: " + id);
    }
}
