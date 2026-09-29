package org.example.bookingservice.models.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateBookingDetailRequest(
        @NotNull(message = "Movie id is required")
        @Min(value = 1, message = "Movie id must be greater than 0")
        Long movieId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be greater than 0")
        Integer quantity
) {
}
