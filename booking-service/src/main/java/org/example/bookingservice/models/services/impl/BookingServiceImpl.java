package org.example.bookingservice.models.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookingservice.models.constants.BookingStatus;
import org.example.bookingservice.models.dto.requests.CreateBookingDetailRequest;
import org.example.bookingservice.models.dto.requests.CreateBookingRequest;
import org.example.bookingservice.models.dto.responses.BookingDetailResponse;
import org.example.bookingservice.models.dto.responses.BookingResponse;
import org.example.bookingservice.models.dto.responses.MovieResponse;
import org.example.bookingservice.models.entities.Booking;
import org.example.bookingservice.models.entities.BookingDetail;
import org.example.bookingservice.models.repositories.BookingDetailRepository;
import org.example.bookingservice.models.repositories.BookingRepository;
import org.example.bookingservice.models.services.BookingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final MovieGatewayService movieGatewayService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${booking.kafka.booking-created-topic:booking-created}")
    private String bookingCreatedTopic;

    @Override
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        log.info("Creating booking for customer: {}", request.customerEmail());

        // Bước 1: Xác thực tất cả phim và tính tổng tiền
        List<MovieResponse> movies = new ArrayList<>();
        double total = 0.0;

        for (CreateBookingDetailRequest item : request.items()) {
            MovieResponse movie = movieGatewayService.getMovieById(item.movieId());
            movies.add(movie);
            total += movie.ticketPrice() * item.quantity();
        }

        Booking booking = Booking.builder()
                .customerName(request.customerName())
                .customerEmail(request.customerEmail())
                .total(total)
                .status(BookingStatus.PENDING)
                .build();

        booking = bookingRepository.save(booking);
        log.info("Saved booking with id: {}", booking.getId());

        List<BookingDetail> details = new ArrayList<>();
        List<BookingDetailResponse> detailResponses = new ArrayList<>();

        for (int i = 0; i < request.items().size(); i++) {
            CreateBookingDetailRequest item = request.items().get(i);
            MovieResponse movie = movies.get(i);
            double unitPrice = movie.ticketPrice();
            double subtotal = unitPrice * item.quantity();

            BookingDetail detail = BookingDetail.builder()
                    .booking(booking)
                    .movieId(item.movieId())
                    .quantity(item.quantity())
                    .unitPrice(unitPrice)
                    .build();

            details.add(detail);
            detailResponses.add(new BookingDetailResponse(
                    null,
                    item.movieId(),
                    movie.title(),
                    item.quantity(),
                    unitPrice,
                    subtotal
            ));
        }

        List<BookingDetail> savedDetails = bookingDetailRepository.saveAll(details);

        List<BookingDetailResponse> finalDetailResponses = new ArrayList<>();
        for (int i = 0; i < savedDetails.size(); i++) {
            BookingDetailResponse old = detailResponses.get(i);
            finalDetailResponses.add(new BookingDetailResponse(
                    savedDetails.get(i).getId(),
                    old.movieId(),
                    old.movieTitle(),
                    old.quantity(),
                    old.unitPrice(),
                    old.subtotal()
            ));
        }

        publishBookingCreatedEvent(booking);

        log.info("Booking created successfully with id: {}, total: {}", booking.getId(), total);

        return new BookingResponse(
                booking.getId(),
                booking.getCustomerName(),
                booking.getCustomerEmail(),
                booking.getTotal(),
                booking.getStatus(),
                finalDetailResponses
        );
    }


    private void publishBookingCreatedEvent(Booking booking) {
        try {
            kafkaTemplate.send(bookingCreatedTopic, booking.getId().toString(), booking.getCustomerEmail());
            log.info("Published booking-created event to topic '{}' for booking id: {}",
                    bookingCreatedTopic, booking.getId());
        } catch (Exception e) {
            log.error("Failed to publish booking-created event for booking id: {}. Error: {}",
                    booking.getId(), e.getMessage(), e);
        }
    }
}

