package org.example.bookingservice.models.repositories;

import org.example.bookingservice.models.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
