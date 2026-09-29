package org.example.notifyservice.consumer;

import org.example.notifyservice.service.EmailService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BookingCreatedConsumerTests {

    @Test
    void delegatesMessageToEmailService() {
        EmailService emailService = mock(EmailService.class);
        BookingCreatedConsumer consumer = new BookingCreatedConsumer(emailService);

        consumer.consume("customer@example.com");

        verify(emailService).sendBookingCreatedEmail("customer@example.com");
    }
}
