package org.example.bookingservice.models.dto.responses;

public record MovieResponse(
        Long id,
        String title,
        Double ticketPrice
) {
}
