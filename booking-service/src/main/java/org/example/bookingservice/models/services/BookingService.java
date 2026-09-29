package org.example.bookingservice.models.services;

import org.example.bookingservice.models.dto.requests.CreateBookingRequest;
import org.example.bookingservice.models.dto.responses.BookingResponse;

public interface BookingService {

    BookingResponse createBooking(CreateBookingRequest request);
}
