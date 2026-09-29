package org.example.bookingservice.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookingservice.models.constants.BookingStatus;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "total")
    private Double total;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

}
