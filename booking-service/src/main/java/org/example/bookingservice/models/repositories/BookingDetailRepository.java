package org.example.bookingservice.models.repositories;

import org.example.bookingservice.models.entities.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {
}
