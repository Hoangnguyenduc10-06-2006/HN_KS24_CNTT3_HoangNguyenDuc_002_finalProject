package org.example.bookingservice.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;
}
