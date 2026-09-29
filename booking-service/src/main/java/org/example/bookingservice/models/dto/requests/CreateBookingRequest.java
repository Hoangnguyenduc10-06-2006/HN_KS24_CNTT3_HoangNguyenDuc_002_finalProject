package org.example.bookingservice.models.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

import java.util.List;

public record CreateBookingRequest(
        @NotBlank(message = "Customer name is required")
        @Size(max = 255, message = "Customer name must not exceed 255 characters")
        String customerName,

        @NotBlank(message = "Customer email is required")
        @Email(message = "Customer email must be valid")
        String customerEmail,

        @NotEmpty(message = "Booking must contain at least one item")
        List<@Valid CreateBookingDetailRequest> items
) {
}
