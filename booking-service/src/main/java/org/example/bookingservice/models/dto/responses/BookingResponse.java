package org.example.bookingservice.models.dto.responses;

import org.example.bookingservice.models.constants.BookingStatus;

import java.util.List;

public record BookingResponse(
        Long id,
        String customerName,
        String customerEmail,
        Double total,
        BookingStatus status,
        List<BookingDetailResponse> items
) {
}
